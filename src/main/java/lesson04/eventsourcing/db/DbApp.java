package lesson04.eventsourcing.db;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import lesson04.DbUtil;
import lesson04.RabbitMQUtil;
import lesson04.eventsourcing.Person;
import lesson04.eventsourcing.RubbitMessage;

public class DbApp {

  private final static String QUEUE_PERSON = "queuePerson";

  public static void main(String[] args) throws Exception {
    DataSource dataSource = initDb();
    ConnectionFactory factory = initMQ();

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
      throw new RuntimeException(e); //персон не может быть добавлен в БД
    }
  }

  private static void removePersonById(DataSource dataSource, Long idPerson) {
    String queryDelete = "DELETE FROM person WHERE id = ? ";
    try (java.sql.Connection connectionDb = dataSource.getConnection();
        PreparedStatement statement = connectionDb.prepareStatement(queryDelete)) {
      statement.setLong(1, idPerson);
      System.out.println(idPerson);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e); //нельзя удалить по ID
    }
  }

  private static Person parseJsonToPerson(String jsonString) {
    Gson gson = new Gson();
    return gson.fromJson(jsonString, Person.class);
  }

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }

  private static DataSource initDb() throws SQLException {
    String ddl = ""
        + "drop table if exists person;"
        + "create table if not exists person (\n"
        + "id bigint primary key,\n"
        + "first_name varchar,\n"
        + "last_name varchar,\n"
        + "middle_name varchar\n"
        + ")";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(ddl, dataSource);
    return dataSource;
  }
}
