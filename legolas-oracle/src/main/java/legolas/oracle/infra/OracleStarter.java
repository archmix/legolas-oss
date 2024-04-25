package legolas.oracle.infra;

import legolas.config.api.interfaces.Entry;
import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.oracle.interfaces.OracleEntry;
import legolas.oracle.interfaces.OracleServiceId;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatasourceFactory;
import legolas.sql.interfaces.SQLStarter;
import legolas.sql.interfaces.TargetDatabase;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.utility.DockerImageName;
import toolbox.data.interfaces.DataSet;
import toolbox.data.interfaces.SQLExecutor;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class OracleStarter extends SQLStarter<OracleContainer> {
  public static final int DEFAULT_PORT = 1521;
  private final OracleContainer container = new OracleContainer(DockerImageName.parse("gvenzl/oracle-xe:21-slim-faststart"));

  public OracleStarter() {
    System.setProperty("oracle.jdbc.timezoneAsRegion", "false");
  }

  @Override
  protected OracleContainer container() {
    return container;
  }

  @Override
  protected void setExtraConfiguration(OracleContainer container) {
    this.configuration.set(OracleEntry.SID, container.getSid());
  }

  @Override
  protected Integer defaultPort() {
    return DEFAULT_PORT;
  }

  @Override
  protected Entry urlEntry() {
    return OracleEntry.URL;
  }

  @Override
  protected Entry hostEntry() {
    return OracleEntry.HOST;
  }

  @Override
  protected Entry portEntry() {
    return OracleEntry.PORT;
  }

  @Override
  protected Entry usernameEntry() {
    return OracleEntry.USERNAME;
  }

  @Override
  protected Entry passwordEntry() {
    return OracleEntry.PASSWORD;
  }

  @Override
  protected Entry driverEntry() {
    return OracleEntry.DRIVER;
  }

  @Override
  protected TargetDatabase targetDatabase() {
    return TargetDatabase.ORACLE;
  }

  @Override
  public ServiceId id() {
    return OracleServiceId.INSTANCE;
  }

  @Override
  public String name() {
    return "Oracle Container";
  }
}
