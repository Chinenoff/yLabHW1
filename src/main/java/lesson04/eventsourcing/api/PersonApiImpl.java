package lesson04.eventsourcing.api;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.sql.DataSource;
import lesson04.DbUtil;
import lesson04.eventsourcing.Person;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {

  ConnectionFactory connectionFactory;
  private final static String QUEUE_SAVE_PERSON = "queueSavePerson";
  private final static String QUEUE_DELETE_PERSON = "queueDeletePerson";
  private final static String QUEUE_DbApp_Recever = "queueSQL";

  public PersonApiImpl(ConnectionFactory connection) {
    this.connectionFactory = connection;
  }

  @Override
  public void deletePerson(Long personId) {

    try (Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.queueDeclare(QUEUE_DELETE_PERSON, false, false, false, null);
      channel.basicPublish("", QUEUE_DELETE_PERSON, null,
          Long.toString(personId).getBytes(StandardCharsets.UTF_8));
      System.out.println(" [x] Sent '" + personId + "'");
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void savePerson(Long personId, String firstName, String lastName, String middleName) {
    Person person = new Person(personId, firstName, lastName, middleName);
    String jsonPerson = parsePersonToJson(person);
    try {
      sendSavePersonMessage(jsonPerson);
    } catch (IOException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Person findPerson(Long personId) {
    String sql = "SELECT * FROM person WHERE ID = ?";
    DataSource dataSource = null;
    Person person = null;
    try {
      dataSource = DbUtil.buildDataSource();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    try (
        java.sql.Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      //statement.setString(1, String.valueOf(personId));
      statement.setLong(1, personId);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        person.setId(resultSet.getLong(1));
        person.setName(resultSet.getString(2));
        person.setLastName(resultSet.getString(3));
        person.setMiddleName(resultSet.getString(4));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return person;
  }

  @Override
  public List<Person> findAll() {

    String sql = "SELECT * FROM person";
    List<Person> personList = new ArrayList<>();
    DataSource dataSource = null;
    try {
      dataSource = DbUtil.buildDataSource();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    try (
        java.sql.Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        Person person = null;
        person.setId(resultSet.getLong(1));
        person.setName(resultSet.getString(2));
        person.setLastName(resultSet.getString(3));
        person.setMiddleName(resultSet.getString(4));
        personList.add(person);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return personList;
  }

  private String parsePersonToJson(Person person) {
    Gson gson = new Gson();
    return gson.toJson(person);
  }

  private void sendSavePersonMessage(String message) throws IOException, TimeoutException {
    try (Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.queueDeclare(QUEUE_SAVE_PERSON, false, false, false, null);
      channel.basicPublish("", QUEUE_SAVE_PERSON, null, message.getBytes(StandardCharsets.UTF_8));
      System.out.println(" [x] Sent '" + message + "'");
    }
  }

}
