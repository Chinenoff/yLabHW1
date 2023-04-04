package lesson05.messagefilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageFilterApp {

  private final static String FILE_NAME = "src/main/java/lesson05/censor_ok.txt";

  public static void main(String[] args) throws SQLException, IOException {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
        Config.class);
    applicationContext.start();

    DataSource dataSource = applicationContext.getBean(DataSource.class);
    QueueConsumer queueConsumer = applicationContext.getBean(QueueConsumer.class);

    readCensorFile(dataSource);
    queueConsumer.run();
  }

  private static void readCensorFile(DataSource dataSource) throws SQLException {
    Connection connection = dataSource.getConnection();
    String sql = "INSERT INTO censor (uncensored_word) VALUES (?)";

    try (PreparedStatement statement = connection.prepareStatement(sql);
        BufferedReader lineReader = new BufferedReader(
            new FileReader(FILE_NAME))) {
      String lineText;
      int numRows = 0;
      while ((lineText = lineReader.readLine()) != null) {
        ++numRows;
        statement.setString(1, lineText);
        statement.addBatch();
        if (numRows % 10 == 0) {
          statement.executeBatch();
        }
      }
    } catch (IOException | SQLException ex) {
      ex.printStackTrace();
    }
  }


}
