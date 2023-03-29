package lesson04.eventsourcing.db;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.GetResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import javax.sql.DataSource;
import lesson04.DbUtil;
import lesson04.RabbitMQUtil;
import lesson04.eventsourcing.Person;

public class DbApp {

  private final static String QUEUE_SAVE_PERSON = "queueSavePerson";
  private final static String QUEUE_DELETE_PERSON = "queueDeletePerson";

  public static void main(String[] args) throws Exception {
    DataSource dataSource = initDb();
    ConnectionFactory connectionFactory = initMQ();

    queueProcessingSavePerson(connectionFactory, dataSource);
    queueProcessingDeletePerson(connectionFactory, dataSource);

    // тут пишем создание и запуск приложения работы с БД
  }

  private static void queueProcessingDeletePerson(ConnectionFactory connectionFactory,
      DataSource dataSource)
      throws IOException, TimeoutException {
    Connection connection = connectionFactory.newConnection();

    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_DELETE_PERSON, false, false, false, null);
    System.out.println(" [*] Waiting from QUEUE_DELETE_PERSON. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      System.out.println(" [x] Received '" + message + "'");

      deletePersonById(dataSource, message);
    };
    channel.basicConsume(QUEUE_DELETE_PERSON, true, deliverCallback, consumerTag -> {
    });
  }

  private static void queueProcessingSavePerson(ConnectionFactory connectionFactory,
      DataSource dataSource)
      throws IOException, TimeoutException {

    Connection connection = connectionFactory.newConnection();

    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_SAVE_PERSON, false, false, false, null);
    System.out.println(" [*] Waiting from QUEUE_SAVE_PERSON. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      System.out.println(" [x] Received '" + message + "'");
      try {
        addPersonToDb(dataSource, parseJsonToPerson(message));
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    };
    channel.basicConsume(QUEUE_SAVE_PERSON, true, deliverCallback, consumerTag -> {
    });
  }

  private static Person parseJsonToPerson(String jsonString) {
    Gson gson = new Gson();
    return gson.fromJson(jsonString, Person.class);
  }

  private static void deletePersonById(DataSource dataSource, String idPerson) {
    String sql = "DELETE FROM person WHERE id = ? ";
    try (
        java.sql.Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, idPerson);
      System.out.println(idPerson);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void addPersonToDb(DataSource dataSource, Person person) throws SQLException {
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
    }
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