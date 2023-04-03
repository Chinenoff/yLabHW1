package lesson05.messagefilter;

import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import javax.sql.DataSource;
import lesson05.DbUtil;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("lesson05.messagefilter")
public class Config {
  
  @Bean
  public ConnectionFactory connectionFactory() {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("localhost");
    connectionFactory.setPort(5672);
    connectionFactory.setUsername("guest");
    connectionFactory.setPassword("guest");
    connectionFactory.setVirtualHost("/");
    return connectionFactory;
  }
  
  @Bean
  public DataSource dataSource() throws SQLException {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setServerName("localhost");
    dataSource.setUser("postgres");
    dataSource.setPassword("postgres");
    dataSource.setDatabaseName("postgres");
    dataSource.setPortNumber(5432);

    String ddl = ""
        + "drop table if exists censor;"
        + "create table if not exists censor (\n"
        + "uncensored_word varchar\n"
        + ")";

    DbUtil.applyDdl(ddl, dataSource);

    return dataSource;
  }

  @Bean
 public QueueConsumer queueConsumer() throws IOException, TimeoutException {
    return new QueueConsumer("input", connectionFactory(), producer());
  }

  @Bean
  public Producer producer() throws IOException, TimeoutException {
    return new Producer("output", connectionFactory());
  }

  /*@Bean TestProducer testProducer(){
    return new TestProducer("input", connectionFactory());
  }*/
}
