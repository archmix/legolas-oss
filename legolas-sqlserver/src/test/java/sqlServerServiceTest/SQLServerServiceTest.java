package sqlServerServiceTest;

import legolas.config.api.interfaces.Configuration;
import legolas.provided.infra.LegolasExtension;
import legolas.runtime.core.interfaces.RunningEnvironment;
import legolas.sql.interfaces.DatasourceFactory;
import legolas.sqlserver.interfaces.SQLServerEntry;
import legolas.sqlserver.interfaces.SQLServerServiceId;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@ExtendWith(LegolasExtension.class)
public class SQLServerServiceTest {

  @Test
  public void shouldStartSQLServerAndMigrate(RunningEnvironment environment) {
    Configuration configuration = environment.get(SQLServerServiceId.INSTANCE).get().configuration();
    String url = configuration.getString(SQLServerEntry.URL).get();
    String driver = configuration.getString(SQLServerEntry.DRIVER).get();
    String username = configuration.getString(SQLServerEntry.USERNAME).get();
    String password = configuration.getString(SQLServerEntry.PASSWORD).get();

    DataSource dataSource = DatasourceFactory.toDataSource(url, driver, username, password);
    assertMigration(dataSource);
  }

  private void assertMigration(DataSource dataSource) {
    try {
      Connection connection = dataSource.getConnection();
      ResultSet rs = connection.getMetaData().getTables(null, null, "Tablename", null);

      Assert.assertTrue(rs.next());

      rs.close();
      connection.close();
      Assert.assertTrue(true);
    } catch (SQLException e) {
      Assert.fail(e.getMessage());
    }
  }
}
