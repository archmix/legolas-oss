package postgreServiceTest;

import legolas.config.api.interfaces.Configuration;
import legolas.postgre.interfaces.PostgreSQLEntry;
import legolas.postgre.interfaces.PostgreSQLServiceId;
import legolas.provided.infra.LegolasExtension;
import legolas.runtime.core.interfaces.RunningEnvironment;
import legolas.sql.interfaces.DatasourceFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@ExtendWith(LegolasExtension.class)
public class PostgreServiceTest {

  @Test
  public void shouldStartPostgreSQLAndMigrate(RunningEnvironment environment) {
    Configuration configuration = environment.get(PostgreSQLServiceId.INSTANCE).get().configuration();
    String url = configuration.getString(PostgreSQLEntry.URL).get();
    String driver = configuration.getString(PostgreSQLEntry.DRIVER).get();
    String username = configuration.getString(PostgreSQLEntry.USERNAME).get();
    String password = configuration.getString(PostgreSQLEntry.PASSWORD).get();

    DataSource dataSource = DatasourceFactory.toDataSource(url, driver, username, password);
    assertMigration(dataSource);
  }

  private void assertMigration(DataSource dataSource) {
    try {
      Connection connection = dataSource.getConnection();
      ResultSet rs = connection.getMetaData().getTables(null, null, "tablename", null);

      Assert.assertTrue(rs.next());

      rs.close();
      connection.close();
      Assert.assertTrue(true);
    } catch (SQLException e) {
      Assert.fail(e.getMessage());
    }
  }
}
