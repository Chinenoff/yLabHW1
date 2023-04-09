package lesson05.sqlquerybuilder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("lesson05.sqlquerybuilder")
public class Config {

  @Bean
  public DataSource dataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setServerName("localhost");
    dataSource.setUser("postgres");
    dataSource.setPassword("postgres");
    dataSource.setDatabaseName("postgres");
    dataSource.setPortNumber(5432);
    return dataSource;
  }

  @Bean
  public Connection connection() throws SQLException {
    return dataSource().getConnection();
  }

  @Bean
  public DatabaseMetaData metaData() throws SQLException {
    return connection().getMetaData();
  }

  @Bean
  public SQLQueryBuilder sqlQueryBuilder() throws SQLException {
    return new SQLQueryBuilderImpl(metaData());
  }

}
