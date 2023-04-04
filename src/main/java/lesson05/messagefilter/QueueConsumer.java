package lesson05.messagefilter;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class QueueConsumer extends EndPoint implements Runnable, Consumer {

  CensorMessageHandler messageHandler;
  Producer producer;

  public QueueConsumer(String endPointName, ConnectionFactory factory,
      CensorMessageHandler messageHandler, Producer producer)
      throws IOException, TimeoutException {
    super(endPointName, factory);
    this.messageHandler = messageHandler;
    this.producer = producer;
  }

  public void run() {
    try {
      channel.basicConsume(endPointName, true, this);
      AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
          Config.class);
      applicationContext.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void handleConsumeOk(String consumerTag) {
    System.out.println(" [*] Waiting messages from INPUT queue. To exit press CTRL+C");
  }

  @Override
  public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties,
      byte[] bytes) throws IOException {
    String receivedMessage = (String) SerializationUtils.deserialize(bytes);
    System.out.printf("Message: %s was received.%n", receivedMessage);

    String censoredMessage = Arrays.stream(
            receivedMessage.split("(?<=\\p{Punct}|\\s)|(?=\\p{Punct}|\\s)"))
        .map(word -> messageHandler.containsWord(word) ? messageHandler.replaceMiddleWithAsterisk(
            word) : word)
        .collect(Collectors.joining());
    System.out.println(censoredMessage); //для проверки отправляемого сообщения

    producer.sendMessage("CensoredMessage: " + censoredMessage);
  }

  public void handleCancel(String consumerTag) {
  }

  public void handleCancelOk(String consumerTag) {
  }

  public void handleRecoverOk(String consumerTag) {
  }

  public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {
  }
}
