package lesson04.persistentmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {

  private final DataSource dataSource;
  private String mapName;

  public PersistentMapImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void init(String name) {
    mapName = name;
    System.out.println("Initialization " + mapName + " successful!");
  }

  @Override
  public boolean containsKey(String key) throws SQLException {
    String sql = "SELECT value FROM persistent_map WHERE KEY = ? AND map_name = ? ";
    String result = null;
    try (
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, key);
      statement.setString(2, mapName);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        result = resultSet.getString(1);
      }
    }
    return result != null;
  }

  @Override
  public List<String> getKeys() throws SQLException {
    String sql = "SELECT KEY FROM persistent_map WHERE map_name = ? ";
    List<String> keyList = new LinkedList<>();
    try (
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, mapName);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        String str = resultSet.getString(1);
        keyList.add(str);
      }
    }
    return keyList;
  }

  @Override
  public String get(String key) throws SQLException {
    String sql = "SELECT value FROM persistent_map WHERE KEY = ? AND map_name = ?";
    String result = null;
    try (
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, key);
      statement.setString(2, mapName);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        result = resultSet.getString(1);
      }
    }
    return result;
  }

  @Override
  public void remove(String key) throws SQLException {
    String sql = "DELETE FROM persistent_map WHERE KEY = ? AND map_name = ?";
    try (
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, key);
      statement.setString(2, mapName);
      statement.executeUpdate();
    }
  }

  @Override
  public void put(String key, String value) throws SQLException {
    String sqlInsert = "INSERT INTO persistent_map (map_name, KEY, value) VALUES (?, ?, ?)";
    String sqlUpdate = "UPDATE persistent_map SET value = ? WHERE KEY = ? AND map_name = ? ";

    if (this.containsKey(key)) {
      try (Connection connection = dataSource.getConnection();
          PreparedStatement statement = connection.prepareStatement(sqlUpdate)) {
        statement.setString(1, value);
        statement.setString(2, key);
        statement.setString(3, mapName);
        statement.executeUpdate();
      }
    } else {
      try (Connection connection = dataSource.getConnection();
          PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
        statement.setString(1, mapName);
        statement.setString(2, key);
        statement.setString(3, value);
        statement.executeUpdate();
      }
    }
  }

  @Override
  public void clear() throws SQLException {
    String sql = "DELETE FROM persistent_map WHERE map_name = ?";
    try (
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, mapName);
      statement.executeUpdate();
    }
  }

}
