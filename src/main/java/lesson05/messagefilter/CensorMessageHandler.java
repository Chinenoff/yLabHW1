package lesson05.messagefilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class CensorMessageHandler {

  DataSource dataSource;

  public CensorMessageHandler(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public boolean containsWord(String word) {
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
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Boolean.TRUE.equals(result);
  }

  public String replaceMiddleWithAsterisk(String word) {
    return word.charAt(0) + "*".repeat(word.length() - 2) + word.charAt(word.length() - 1);
  }


}
