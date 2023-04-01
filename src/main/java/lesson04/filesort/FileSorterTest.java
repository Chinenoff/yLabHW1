package lesson04.filesort;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Random;
import javax.sql.DataSource;
import lesson04.DbUtil;
import org.example.homework3.file.sort.Validator;

public class FileSorterTest {

  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    File data = new File("data.txt");
    long time = System.currentTimeMillis();
    try {
      generate(data, 100_000);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    FileSorter fileSorter = new FileSortImpl(dataSource);
    File res = fileSorter.sort(data);
    System.out.println("file check result: " + new Validator(res).isSorted());
    System.out.println("file sort time: " + (System.currentTimeMillis() - time));
  }

  public static DataSource initDb() throws SQLException {
    String createSortTable = ""
        + "drop table if exists numbers;"
        + "CREATE TABLE if not exists numbers (\n"
        + "\tval bigint\n"
        + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createSortTable, dataSource);
    return dataSource;
  }

  public static File generate(File file, int count) throws IOException {
    Random random = new Random();
    try (PrintWriter pw = new PrintWriter(file)) {
      for (int i = 0; i < count; i++) {
        pw.println(random.nextLong());
      }
      pw.flush();
    }
    return file;
  }

}
