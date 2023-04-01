package lesson04.eventsourcing.api;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lesson04.eventsourcing.Person;
import lesson04.eventsourcing.RubbitMessage;


/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {

  Channel channel;
  DataSource dataSource;
  private final static String QUEUE_PERSON = "queuePerson";

  public PersonApiImpl(Channel channel, DataSource dataSource) {
    this.channel = channel;
    this.dataSource = dataSource;
  }

  @Override
  public void deletePerson(Long personId) {
    RubbitMessage rubbitMessage = new RubbitMessage("deletePerson", Long.toString(personId));
    try {
      channel.basicPublish("", QUEUE_PERSON, null,
          rubbitMessage.toString().getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println(" [x] Sent '" + rubbitMessage + "'");
  }

  @Override
  public void savePerson(Long personId, String firstName, String lastName, String middleName) {
    Person person = new Person(personId, firstName, lastName, middleName);
    String jsonPerson = parsePersonToJson(person);
    RubbitMessage rubbitMessage = new RubbitMessage("savePerson", jsonPerson);
    try {
      channel.basicPublish("", QUEUE_PERSON, null,
          rubbitMessage.toString().getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println(" [x] Sent '" + rubbitMessage + "'");
  }

  @Override
  public Person findPerson(Long personId) {
    String querySelectById = "SELECT * FROM person WHERE id = ? ";
    Person person = null;
    try (java.sql.Connection connectionDb = dataSource.getConnection();
        PreparedStatement statement = connectionDb.prepareStatement(querySelectById)) {
      statement.setLong(1, personId);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        Long id = resultSet.getLong(1);
        String name = resultSet.getString(2);
        String lastName = resultSet.getString(3);
        String middleName = resultSet.getString(4);
        person = new Person(id, name, lastName, middleName);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e); //нельзя найти по id
    }
    return person;
  }

  @Override
  public List<Person> findAll() {
    List<Person> personList = new ArrayList<>();
    String querySelectAll = "SELECT * FROM person";
    try (java.sql.Connection connectionDb = dataSource.getConnection();
        Statement statement = connectionDb.createStatement()) {
      ResultSet resultSet = statement.executeQuery(querySelectAll);
      while (resultSet.next()) {
        Long id = resultSet.getLong(1);
        String name = resultSet.getString(2);
        String lastName = resultSet.getString(3);
        String middleName = resultSet.getString(4);
        Person person = new Person(id, name, lastName, middleName);
        personList.add(person);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e); //нельзя найти по id
    }
    return personList;
  }

  private String parsePersonToJson(Person person) {
    Gson gson = new Gson();
    return gson.toJson(person);
  }

}
