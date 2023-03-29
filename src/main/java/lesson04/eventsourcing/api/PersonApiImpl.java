package lesson04.eventsourcing.api;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import lesson04.eventsourcing.Person;
import lesson04.eventsourcing.RubbitMessage;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
  Channel channel;
  private final static String QUEUE_PERSON = "queuePerson";

  public PersonApiImpl(Channel channel) {
    this.channel = channel;
  }

  @Override
  public void deletePerson(Long personId) {
    RubbitMessage rubbitMessage = new RubbitMessage("deletePerson",Long.toString(personId));
    try {
      channel.basicPublish("", QUEUE_PERSON, null, rubbitMessage.toString().getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println(" [x] Sent '" + rubbitMessage + "'");
  }

  @Override
  public void savePerson(Long personId, String firstName, String lastName, String middleName) {
    Person person = new Person(personId, firstName, lastName, middleName);
    String jsonPerson = parsePersonToJson(person);
    RubbitMessage rubbitMessage = new RubbitMessage("savePerson",jsonPerson);
    try {
      channel.basicPublish("", QUEUE_PERSON, null, rubbitMessage.toString().getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println(" [x] Sent '" + rubbitMessage + "'");

  }

  @Override
  public Person findPerson(Long personId) {
    return new Person();
  }

  @Override
  public List<Person> findAll() {
    List<Person> personList = new ArrayList<>();
    return personList;
  }

 /* private static void runRubbitQueue(ConnectionFactory connectionFactory){
    try (Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {
    } catch (IOException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }*/

  private String parsePersonToJson(Person person) {
    Gson gson = new Gson();
    return gson.toJson(person);
  }

  /*private static String createRubbitMessage(String metod, String message){
    return message + ";" + message;
  }

  private static String getMetodFromRubbitMessage(String message){
    String[] items = message.split(";");
    return items[0];
  }

  private static String getMessageFromRubbitMessage(String message){
    String[] items = message.split(";");
    return items[1];
  }*/

}
