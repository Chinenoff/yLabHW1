package lesson05.eventsourcing.api;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
  private final static String QUEUE_PERSON = "queuePerson";

  public static void main(String[] args) throws Exception {

    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    PersonApi personApi = applicationContext.getBean(PersonApi.class);
    ConnectionFactory factory = applicationContext.getBean(ConnectionFactory.class);
    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()){
      channel.queueDeclare(QUEUE_PERSON, false, false, false, null);

      //Test
      personApi.savePerson(1111L, "Ivan", "Ivanov", "Ivanovich");
      personApi.savePerson(2222L, "Ivanka", "Trump", "Marie");
      personApi.savePerson(3333L, "Keanu", "Reeves", "Charles");
      personApi.savePerson(4444L, "Lara", "Croft", "Tomb Raider");
      personApi.savePerson(5555L, "Thomas", "Cruise", "Mapother");
      personApi.savePerson(6666L, "Monica", "Bellucci", "Anna Maria");

      /*for (int i = 0; i < 10; i++) {
        personApi.savePerson(random.nextLong(), generateBotName() , generateBotName(), generateBotName());
      }*/

      try {
        Thread.sleep(20000);
      } catch (InterruptedException ex) {
      }
      personApi.deletePerson(1111L);
      System.out.println(personApi.findPerson(6666L));
      System.out.println(personApi.findAll());
      try {
        Thread.sleep(20000);
      } catch (InterruptedException ex) {
      }
      personApi.deletePerson(1111L);
    }
  }
}
