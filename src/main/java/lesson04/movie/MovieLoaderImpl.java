package lesson04.movie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import javax.sql.DataSource;

public class MovieLoaderImpl implements MovieLoader {

  private final DataSource dataSource;

  public MovieLoaderImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void loadData(File file) {
    Charset win1251 = Charset.forName("Windows-1251");
    Connection connection = null;
    String sql = "INSERT INTO movie (year, length, title, subject, actors, actress, director, popularity, awards) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(sql);
      BufferedReader lineReader = new BufferedReader(new FileReader(file, win1251));

      lineReader.readLine(); // skip header line
      lineReader.readLine(); // skip type line
      processingLineReader(lineReader, statement);

      lineReader.close();
      statement.executeBatch();
      connection.commit();
      connection.close();

    } catch (IOException ex) {
      System.err.println(ex);
    } catch (SQLException ex) {
      ex.printStackTrace();
      try {
        if (connection != null) {
          connection.rollback();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private void processingLineReader(BufferedReader lineReader, PreparedStatement statement)
      throws IOException, SQLException {
    String lineText;
    while ((lineText = lineReader.readLine()) != null) {
      String[] data = lineText.split(";");
      Integer year = convertStringToInt(data[0]);
      Integer length = convertStringToInt(data[1]);
      String title = data[2];
      String subject = data[3];
      String actor = data[4];
      String actress = data[5];
      String director = data[6];
      Integer popularity = convertStringToInt(data[7]);
      Boolean awards = convertStringToBoolean(data[8]);

      setParamInt(statement, 1, year);
      setParamInt(statement, 2, length);
      setParamString(statement, 3, title);
      setParamString(statement, 4, subject);
      setParamString(statement, 5, actor);
      setParamString(statement, 6, actress);
      setParamString(statement, 7, director);
      setParamInt(statement, 8, popularity);
      statement.setBoolean(9, awards);

      statement.addBatch();

      statement.executeBatch();
    }
  }

  static void setParamInt(PreparedStatement stmt, int paramIndex, Integer value)
      throws SQLException {
    if (value == null || value == 0) {
      stmt.setNull(paramIndex, Types.INTEGER);
    } else {
      stmt.setInt(paramIndex, value);
    }
  }

  static void setParamString(PreparedStatement stmt, int paramIndex, String value)
      throws SQLException {
    if (value.equals("")) {
      stmt.setNull(paramIndex, Types.VARCHAR);
    } else {
      stmt.setString(paramIndex, value);
    }
  }

  private Integer convertStringToInt(String str) {
    String lineSplitBy = "";
    if (str.equals(lineSplitBy)) {
      return null;
    }
    return Integer.parseInt(str);
  }

  private Boolean convertStringToBoolean(String str) {
    return !str.equals("No") && !str.equals("");
  }

}



