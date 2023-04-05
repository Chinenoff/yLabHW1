package lesson05.messagefilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageFilterApp {

  private final static String FILE_NAME = "src/main/java/lesson05/censor_ok.txt";
  private final static String TABLE_OF_OBSCENE_WORDS = "censor";

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
    if (!tableExists(connection, TABLE_OF_OBSCENE_WORDS)){
      connection.createStatement().executeUpdate("create table censor ( uncensored_word VARCHAR(255))");
      System.out.println ("table [" + TABLE_OF_OBSCENE_WORDS + "] has been created ");
    } else {
      cleanseTable(connection, TABLE_OF_OBSCENE_WORDS);
    }
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
      System.out.println ("data added to table: " + TABLE_OF_OBSCENE_WORDS);
    } catch (IOException | SQLException ex) {
      ex.printStackTrace();
    }
  }

  private static boolean tableExists(Connection connection, String tableName) throws SQLException {
    DatabaseMetaData meta = connection.getMetaData();
    ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});
    Boolean resultTableExists = resultSet.next();
    System.out.println ("check if the table [" + tableName + "] exists. Result: " + resultTableExists);
    return resultTableExists;
  }

  public static void cleanseTable (Connection connection, String tableName) {
    try {
      Statement stmt = connection.createStatement ();
      stmt.execute ("truncate table " + tableName);
      System.out.println ("cleanseTable [" + tableName + "] executed successfully.");
    } catch (Exception e) { e.printStackTrace(); }
  }


}
