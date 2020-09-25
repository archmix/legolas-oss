package oracleServiceTest;

import legolas.async.api.interfaces.Promise;
import legolas.config.api.interfaces.Configuration;
import legolas.oracle.interfaces.OracleEntry;
import legolas.oracle.interfaces.OracleServiceId;
import legolas.runtime.core.interfaces.RunningEnvironment;
import legolas.runtime.core.interfaces.RuntimeEnvironment;
import legolas.sql.interfaces.DatasourceFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OracleServiceTest {

  @Test
  public void shouldStartOracleAndMigrate() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Promise<RunningEnvironment> promise = RuntimeEnvironment.TEST.start(executorService);
    RunningEnvironment environment = promise.get();

    Configuration configuration = environment.get(OracleServiceId.INSTANCE).get().configuration();
    String url = configuration.getString(OracleEntry.URL).get();
    String driver = configuration.getString(OracleEntry.DRIVER).get();
    String username = configuration.getString(OracleEntry.USERNAME).get();
    String password = configuration.getString(OracleEntry.PASSWORD).get();

    Assert.assertEquals("xe", configuration.getString(OracleEntry.SID).get());
    Assert.assertEquals(username, configuration.getString(OracleEntry.SCHEMA).get());

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
