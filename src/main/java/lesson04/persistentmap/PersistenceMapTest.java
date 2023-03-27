package lesson04.persistentmap;

import java.sql.SQLException;
import javax.sql.DataSource;
import lesson04.DbUtil;

public class PersistenceMapTest {

  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap = new PersistentMapImpl(dataSource);

    persistentMap.init("myDB");
    for (int i = 1; i < 6; i++) {
      persistentMap.put("key" + i, "value" + i);
      System.out.println("added value: key" + i + "-value" + i);
    }

    System.out.println("the meaning of the keys in map: " + persistentMap.getKeys());

    System.out.println("key value before replacement: " + persistentMap.get("key5"));
    persistentMap.put("key5", "changed value"); // PUT - замена значения
    System.out.println("key value after replacement: " + persistentMap.get("key5"));

    persistentMap.put("key6", "new value"); // PUT - добавление значения
    System.out.println("the meaning of the keys in map: " + persistentMap.getKeys());

    persistentMap.init("myNewDB");
    for (int i = 100; i < 106; i++) {
      persistentMap.put("key" + i, "value" + i);
      System.out.println("added value: key" + i + "-value" + i);
    }

    System.out.println("the meaning of the keys in map: " + persistentMap.getKeys());
    System.out.println("deleting a key: key105 ");
    persistentMap.remove("key105");
    System.out.println("the meaning of the keys in map: " + persistentMap.getKeys());

    System.out.println("clearing the base");
    persistentMap.clear();
    System.out.println("the meaning of the keys in map: " + persistentMap.getKeys());

  }

  public static DataSource initDb() throws SQLException {
    String createMapTable = ""
        + "drop table if exists persistent_map; "
        + "CREATE TABLE if not exists persistent_map (\n"
        + "   map_name varchar,\n"
        + "   KEY varchar,\n"
        + "   value varchar\n"
        + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
