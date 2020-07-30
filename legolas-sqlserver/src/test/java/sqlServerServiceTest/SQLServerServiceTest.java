package sqlServerServiceTest;

import legolas.async.api.interfaces.Promise;
import legolas.config.api.interfaces.Configuration;
import legolas.runtime.core.interfaces.RunningEnvironment;
import legolas.runtime.core.interfaces.RuntimeEnvironment;
import legolas.sql.interfaces.DatasourceFactory;
import legolas.sqlserver.interfaces.SQLServerEntry;
import legolas.sqlserver.interfaces.SQLServerServiceId;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SQLServerServiceTest {

  @Test
  public void shouldStartSQLServerAndMigrate() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Promise<RunningEnvironment> promise = RuntimeEnvironment.TEST.start(executorService);
    RunningEnvironment environment = promise.get();

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
