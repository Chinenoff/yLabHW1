package lesson05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class EndPoint{

  protected Channel channel;
  protected Connection connection;
  protected ConnectionFactory factory;
  protected String endPointName;

  public EndPoint(String endpointName,ConnectionFactory factory) throws IOException, TimeoutException {
    this.endPointName = endpointName;
    this.factory = factory;

    //Create a connection factory
    //ConnectionFactory factory = new ConnectionFactory();

    //hostname of your rabbitmq server
    //factory.setHost("localhost");

    //getting a connection
    connection = factory.newConnection();

    //creating a channel
    channel = connection.createChannel();

    //declaring a queue for this channel. If queue does not exist,
    //it will be created on the server.
    channel.queueDeclare(endpointName, false, false, false, null);
  }

  public EndPoint(String endPointName, ConnectionFactory factory, Producer producer) {

  }

  public void close() throws IOException, TimeoutException {
    this.channel.close();
    this.connection.close();
  }

  public String getEndPointName() {
    return endPointName;
  }


}

//****
/*protected Channel channel;
  protected Connection connection;
  protected String endPointName;

  public EndPoint(String endpointName) throws IOException, TimeoutException {
    this.endPointName = endpointName;

    //Create a connection factory
    ConnectionFactory factory = new ConnectionFactory();

    //hostname of your rabbitmq server
    factory.setHost("localhost");

    //getting a connection
    connection = factory.newConnection();

    //creating a channel
    channel = connection.createChannel();

    //declaring a queue for this channel. If queue does not exist,
    //it will be created on the server.
    channel.queueDeclare(endpointName, false, false, false, null);
  }

  public void close() throws IOException, TimeoutException {
    this.channel.close();
    this.connection.close();
  }*/
