package lesson05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class EndPoint {

  protected Channel channel;
  protected Connection connection;
  protected ConnectionFactory factory;
  protected String endPointName;

  public EndPoint(String endpointName, ConnectionFactory factory)
      throws IOException, TimeoutException {
    this.endPointName = endpointName;
    this.factory = factory;

    connection = factory.newConnection();

    channel = connection.createChannel();

    channel.queueDeclare(endpointName, false, false, false, null);
  }

  public void close() throws IOException, TimeoutException {
    this.channel.close();
    this.connection.close();
  }


}
