package lesson04.filesort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {

  private static final String OUTPUTFILE = "sortedfile.txt";
  private final DataSource dataSource;

  public FileSortImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public File sort(File data) {
    Connection connection = null;
    File fileResult = new File(OUTPUTFILE);
    String sql = "INSERT INTO numbers (val) VALUES (?)";

    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(sql);
      BufferedReader lineReader = new BufferedReader(new FileReader(data));

      String lineText;
      int numRows = 0;
      while ((lineText = lineReader.readLine()) != null) {
        ++numRows;
        long number = Long.parseLong(lineText);
        statement.setLong(1, number);
        statement.addBatch();
        if (numRows % 1000 == 0) {
          statement.executeBatch();
        }
      }

      lineReader.close();
      statement.executeBatch();

      Statement stmt = connection.createStatement();
      PrintWriter printWriter = new PrintWriter(fileResult);
      String query = "Select * from numbers ORDER by val";
      ResultSet rs = stmt.executeQuery(query);
      writeResultSetToWriter(rs, printWriter);
      printWriter.close();

      connection.commit();
      connection.close();

    } catch (IOException  | SQLException ex ) {
      ex.printStackTrace();
    }
    return fileResult;
  }

  public static void writeResultSetToWriter(ResultSet resultSet, PrintWriter writer)
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
  }

}
