package oracleServiceTest;

import legolas.config.api.interfaces.Configuration;
import legolas.oracle.interfaces.OracleEntry;
import legolas.oracle.interfaces.OracleServiceId;
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
public class OracleServiceTest {

  @Test
  public void shouldStartOracleAndMigrate(RunningEnvironment environment) {
    Configuration configuration = environment.get(OracleServiceId.INSTANCE).get().configuration();
    String url = configuration.getString(OracleEntry.URL).get();
    String driver = configuration.getString(OracleEntry.DRIVER).get();
    String username = configuration.getString(OracleEntry.USERNAME).get();
    String password = configuration.getString(OracleEntry.PASSWORD).get();

    Assert.assertEquals("xe", configuration.getString(OracleEntry.SID).get());

    DataSource dataSource = DatasourceFactory.toDataSource(url, driver, username, password);
    assertMigration(dataSource);
  }

  private void assertMigration(DataSource dataSource) {
    try {
      Connection connection = dataSource.getConnection();
      ResultSet rs = connection.getMetaData().getTables(null, null, "TABLENAME", null);

      Assert.assertTrue(rs.next());

      rs.close();
      connection.close();
      Assert.assertTrue(true);
    } catch (SQLException e) {
      Assert.fail(e.getMessage());
    }
  }
}
