package lesson05.messagefilter;

import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestProducerSendTestMessage {

  public static void main(String[] args) throws IOException, TimeoutException {

    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
        Config.class);
    applicationContext.start();

    ConnectionFactory factory = applicationContext.getBean(ConnectionFactory.class);
    Producer producer = new Producer("input", factory);

    try (Scanner scan = new Scanner(System.in)) {

      while (true) {
        System.out.print("Введите соолбщение: ");

        String message = scan.nextLine();

        producer.sendMessage(message);
        System.out.println("Message: " + message + " was sent.");
      }
    }
  }

}
