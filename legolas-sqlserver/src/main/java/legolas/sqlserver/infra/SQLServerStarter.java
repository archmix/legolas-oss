package legolas.sqlserver.infra;

import legolas.config.api.interfaces.Entry;
import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatasourceFactory;
import legolas.sql.interfaces.SQLStarter;
import legolas.sql.interfaces.TargetDatabase;
import legolas.sqlserver.interfaces.SQLServerEntry;
import legolas.sqlserver.interfaces.SQLServerServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;
import toolbox.data.interfaces.SQLExecutor;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class SQLServerStarter extends SQLStarter<MSSQLServerContainer> {
  static final Integer DEFAULT_PORT = 1433;

  @Override
  protected MSSQLServerContainer container() {
    return new MSSQLServerContainer(DockerImageName.parse("mcr.microsoft.com/mssql/server:2017-CU12"));
  }

  @Override
  protected Integer defaultPort() {
    return MSSQLServerContainer.MS_SQL_SERVER_PORT;
  }

  @Override
  protected Entry urlEntry() {
    return SQLServerEntry.URL;
  }

  @Override
  protected Entry hostEntry() {
    return SQLServerEntry.HOST;
  }

  @Override
  protected Entry portEntry() {
    return SQLServerEntry.PORT;
  }

  @Override
  protected Entry usernameEntry() {
    return SQLServerEntry.USERNAME;
  }

  @Override
  protected Entry passwordEntry() {
    return SQLServerEntry.PASSWORD;
  }

  @Override
  protected Entry driverEntry() {
    return SQLServerEntry.DRIVER;
  }

  @Override
  protected TargetDatabase targetDatabase() {
    return TargetDatabase.SQL_SERVER;
  }

  @Override
  public ServiceId id() {
    return SQLServerServiceId.INSTANCE;
  }

  @Override
  public String name() {
    return "SQL Server Container";
  }
}
