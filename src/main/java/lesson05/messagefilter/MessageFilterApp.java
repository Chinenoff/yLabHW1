package lesson05.messagefilter;

import com.rabbitmq.client.ConnectionFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageFilterApp {

  private final static String FILE_NAME = "src/main/java/lesson05/censor_ok.txt";
  String nonCensoredMessage;
  //private final static String INPUT_QUEUE = "input";

  public static void main(String[] args) throws IOException, SQLException {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
        Config.class);
    applicationContext.start();

    DataSource dataSource = applicationContext.getBean(DataSource.class);
    ConnectionFactory factory = applicationContext.getBean(ConnectionFactory.class);
    QueueConsumer queueConsumer = applicationContext.getBean(QueueConsumer.class);

    readCensorFile(dataSource);
    //containsWord("неебет", dataSource);
    System.out.println(containsWord("Неебет", dataSource));

    /*List<String> lines = Files.readAllLines(Paths.get(FILE_NAME), StandardCharsets.UTF_8);
    for (String line : lines) {
      System.out.println(line);
    }*/



    /*Thread consumerThread = new Thread(queueConsumer);
    consumerThread.start();
    System.out.println("----->" );*/


    /*System.out.println(consumerThread.getState());
    System.out.println(consumerThread.isAlive());*/

    //Producer producer = applicationContext.getBean(Producer.class);

    /*for (int i = 0; i < 50; i++) {
      String message = "Number " + i;
      producer.sendMessage(message);
      System.out.println("Message: " + message + " was sent.");
    }*/

      /*List<String> lines = Files.readAllLines(Paths.get(FILE_NAME), StandardCharsets.UTF_8);
        for (String line : lines) {
          System.out.println(line);
      }*/
    /*System.out.println(consumerThread.getState());
    System.out.println(consumerThread.isAlive());*/
  }

  private static void readCensorFile(DataSource dataSource) throws SQLException {
    Connection connection = dataSource.getConnection();
    String sql = "INSERT INTO censor (uncensored_word) VALUES (?)";

    try (PreparedStatement statement = connection.prepareStatement(sql);
        BufferedReader lineReader = new BufferedReader(new FileReader(new File(FILE_NAME)))){
      /*PreparedStatement statement = connection.prepareStatement(sql);
      BufferedReader lineReader = new BufferedReader(new FileReader(data));*/
      String lineText;
      int numRows = 0;
      while ((lineText = lineReader.readLine()) != null) {
        ++numRows;
        //long number = Long.parseLong(lineText);
        statement.setString(1, lineText);
        statement.addBatch();
        if (numRows % 10 == 0) {
          statement.executeBatch();
        }
      }
    } catch (IOException | SQLException ex ) {
      ex.printStackTrace();
    }
  }

  /*public static void writeResultSetToWriter(ResultSet resultSet, PrintWriter writer)
      throws SQLException {
    int numRows = 0;

    while (resultSet.next()) {
      ++numRows;
      String valueStr = resultSet.getString(1);
      writer.println(valueStr);
      if (numRows % 1000 == 0) {
        writer.flush();
      }
    }
  }*/

  public static boolean containsWord(String word, DataSource dataSource) throws SQLException {
    /*String sql = "SELECT value FROM persistent_map WHERE KEY = ? AND map_name = ? ";*/
    String sql = "SELECT EXISTS(SELECT 1 FROM censor WHERE uncensored_word ILIKE ?) ";
    Boolean result = null;
    try (
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, word.trim());
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        result = resultSet.getBoolean(1);
      }
    }
    return result;
  }

  public String getNonCensoredMessage() {
    return nonCensoredMessage;
  }

  public void setNonCensoredMessage(String nonCensoredMessage) {
    this.nonCensoredMessage = nonCensoredMessage;
  }
}

//**
/*QueueConsumer consumer = new QueueConsumer("queue");
  Thread consumerThread = new Thread(consumer);
		consumerThread.start();

        Producer producer = new Producer("queue");

        for (int i = 0; i < 100000; i++) {
    HashMap message = new HashMap();
    message.put("message number", i);
    producer.sendMessage(message);
    System.out.println("Message Number "+ i +" sent.");
    }*/
