package lesson05.messagefilter;

import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang.SerializationUtils;

public class Producer extends EndPoint {

  public Producer(String endPointName, ConnectionFactory factory)
      throws IOException, TimeoutException {
    super(endPointName, factory);
  }

  public void sendMessage(Serializable object) throws IOException {
    channel.basicPublish("", endPointName, null, SerializationUtils.serialize(object));
  }
}
