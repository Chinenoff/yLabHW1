package lesson04.eventsourcing.api;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Random;
import lesson04.RabbitMQUtil;

public class ApiApp {
  private final static String QUEUE_PERSON = "queuePerson";
  public static void main(String[] args) throws Exception {

    ConnectionFactory factory = initMQ();
    Random random = new Random();
    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.queueDeclare(QUEUE_PERSON, false, false, false, null);
      PersonApi personApi = new PersonApiImpl(channel);

      personApi.savePerson(1111L, "Ivan", "Ivanov", "Ivanovich");
      personApi.savePerson(2222L, "Ivanka", "Ivankovna", "Ivanovich");
      try {
        Thread.sleep(60000);
      } catch(InterruptedException ex) {}

      personApi.deletePerson(1111L);

      /*for (int i = 0; i < 10; i++) {
        personApi.savePerson(random.nextLong(), generateBotName() , generateBotName(), generateBotName());
      }*/

    }


    // Тут пишем создание PersonApi, запуск и демонстрацию работы
  }

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }

  private static String generateBotName (){
    Random random = new Random();
    return "Person-" + random.nextInt();
  }
}
