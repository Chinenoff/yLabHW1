package lesson05.eventsourcing.db;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import lesson05.eventsourcing.RubbitMessage;
import lesson05.eventsourcing.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DbApp {
  private final static String QUEUE_PERSON = "queuePerson";

  public static void main(String[] args) throws Exception {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
        Config.class);
    applicationContext.start();
    // тут пишем создание и запуск приложения работы с БД
    DataSource dataSource = applicationContext.getBean(DataSource.class);
    ConnectionFactory factory = applicationContext.getBean(ConnectionFactory.class);

    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_PERSON, false, false, false, null);
    System.out.println(" [*] Waiting from QUEUE_PERSON. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      RubbitMessage messageRybbit = new RubbitMessage(
          new String(delivery.getBody(), StandardCharsets.UTF_8));
      System.out.println(" [x] Received '" + messageRybbit + "'");
      String metod = messageRybbit.getMetod();
      String message = messageRybbit.getMessage();
      if (metod.equals("savePerson")) {
        addPersonToDb(dataSource, parseJsonToPerson(message));
      } else if (metod.equals("deletePerson")) {
        removePersonById(dataSource, Long.valueOf(message));
      }
    };
    channel.basicConsume(QUEUE_PERSON, true, deliverCallback, consumerTag -> {
    });
  }

  private static void addPersonToDb(DataSource dataSource, Person person) {
    String queryInsert = "INSERT INTO person (id, first_name, last_name, middle_name) VALUES (?, ?, ?, ?)";
    String queryInsertUpdate = "INSERT INTO person (id, first_name, last_name, middle_name) VALUES (?, ?, ?, ?)";
    try (java.sql.Connection connectionDb = dataSource.getConnection();
        PreparedStatement statement = connectionDb.prepareStatement(queryInsert)) {

      Long id = person.getId();
      String name = person.getName();
      String lastName = person.getLastName();
      String middleName = person.getMiddleName();

      statement.setLong(1, id);
      statement.setString(2, name);
      statement.setString(3, lastName);
      statement.setString(4, middleName);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void removePersonById(DataSource dataSource, Long idPerson) {
    String queryDelete = "DELETE FROM person WHERE id = ? ";
    try (java.sql.Connection connectionDb = dataSource.getConnection();
        PreparedStatement statement = connectionDb.prepareStatement(queryDelete)) {
      statement.setLong(1, idPerson);
      System.out.println(idPerson);
      int sqlResult = statement.executeUpdate();
      if (sqlResult == 0) {
        System.out.println("Ошибка удаления! Запись с id " + idPerson + " отсутствует в базе.");
      }
    } catch (SQLException e) {
      System.out.println("SQL Error removal method");
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private static Person parseJsonToPerson(String jsonString) {
    Gson gson = new Gson();
    return gson.fromJson(jsonString, Person.class);
  }

}
