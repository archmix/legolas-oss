package legolas.postgre.infra;

import legolas.config.api.interfaces.Entry;
import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.postgre.interfaces.PostgreSQLEntry;
import legolas.postgre.interfaces.PostgreSQLServiceId;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatabaseConfiguration;
import legolas.sql.interfaces.SQLStarter;
import legolas.sql.interfaces.TargetDatabase;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class PostgreSQLStarter extends SQLStarter<PostgreSQLContainer> {
  static final int DEFAULT_PORT = 5432;
  private final PostgreSQLContainer container = new PostgreSQLContainer(DockerImageName.parse("postgres:9.6.12"));

  @Override
  protected PostgreSQLContainer container() {
    return this.container;
  }

  @Override
  protected Integer defaultPort() {
    return PostgreSQLContainer.POSTGRESQL_PORT;
  }

  @Override
  protected Entry urlEntry() {
    return PostgreSQLEntry.URL;
  }

  @Override
  protected Entry hostEntry() {
    return PostgreSQLEntry.HOST;
  }

  @Override
  protected Entry portEntry() {
    return PostgreSQLEntry.PORT;
  }

  @Override
  protected Entry usernameEntry() {
    return PostgreSQLEntry.USERNAME;
  }

  @Override
  protected Entry passwordEntry() {
    return PostgreSQLEntry.PASSWORD;
  }

  @Override
  protected Entry driverEntry() {
    return PostgreSQLEntry.DRIVER;
  }

  @Override
  protected TargetDatabase targetDatabase() {
    return TargetDatabase.POSTGRESQL;
  }

  @Override
  public ServiceId id() {
    return PostgreSQLServiceId.INSTANCE;
  }

  @Override
  public String name() {
    return "PostgreSQL Container";
  }
}
