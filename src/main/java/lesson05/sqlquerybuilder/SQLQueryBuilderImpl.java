package lesson05.sqlquerybuilder;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class SQLQueryBuilderImpl implements SQLQueryBuilder {

  DatabaseMetaData databaseMetaData;

  public SQLQueryBuilderImpl(DatabaseMetaData databaseMetaData) {
    this.databaseMetaData = databaseMetaData;
  }

  @Override
  public String queryForTable(String tableName) throws SQLException {
    StringBuilder builder = new StringBuilder();
    if (getTables().contains(tableName)) {
      try (ResultSet rs = databaseMetaData.getColumns(null, null, tableName, null)) {
        builder.append("SELECT ");
        while (rs.next()) {
          String s = rs.getString("COLUMN_NAME");
          builder.append(s + ", ");
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append(" FROM " + tableName);

      }
    } else {
      return null;
    }
    return builder.toString();
  }

  @Override
  public List<String> getTables() throws SQLException {
    List<String> tables = new ArrayList<>();
    try (ResultSet rs = databaseMetaData.getTables(null, null, "%", null)) {
      while (rs.next()) {
        tables.add(rs.getString("TABLE_NAME"));
      }
    }
    return tables;
  }

    /*ResultSet result = databaseMetaData.getTables(null, null, "%", null);
    List<String> tables = new ArrayList<String>();
    while(result.next()) {
      tables.add(result.getString(2) + "." + result.getString(3));
    }
    return tables;
  }*/
}
