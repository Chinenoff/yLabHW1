package lesson05.messagefilter;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class QueueConsumer extends EndPoint implements Runnable, Consumer {
  Producer producer;


  public QueueConsumer(String endPointName, ConnectionFactory factory, Producer producer)
      throws IOException, TimeoutException {
    super(endPointName, factory, producer);
  }

  public void run() {
    try {
      //start consuming messages. Auto acknowledge messages.
      channel.basicConsume(endPointName, true,this);
      AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
          Config.class);
      applicationContext.start();
      producer = applicationContext.getBean(Producer.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Called when consumer is registered.
   */
  public void handleConsumeOk(String consumerTag) {
    System.out.println("Consumer "+consumerTag +" registered");
  }

  /**
   * Called when new message is available.
   */
  @Override
  public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties,
      byte[] bytes) throws IOException {
    //Map map = (HashMap) SerializationUtils.deserialize(bytes);
    String receivedMessage = (String) SerializationUtils.deserialize(bytes);
    System.out.println("Message: "+ receivedMessage + " was received.");
    producer.sendMessage(receivedMessage);
  }

  /*public String sendMessage(Serializable object) throws IOException {
    channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
  }*/

 /* public String getNonCensorMessage() {
    return nonCensorMessage;
  }

  public void setNonCensorMessage(String nonCensorMessage) {
    this.nonCensorMessage = nonCensorMessage;
  }*/

  public void handleCancel(String consumerTag) {}
  public void handleCancelOk(String consumerTag) {}
  public void handleRecoverOk(String consumerTag) {}
  public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {}
}
