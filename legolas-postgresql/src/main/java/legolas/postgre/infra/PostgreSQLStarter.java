package legolas.postgre.infra;

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

import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class PostgreSQLStarter extends SQLStarter<PostgreSQLContainer> {
  static final String PASSWORD = "postgre";
  static final String JDBC_DRIVER_NAME = "org.postgresql.Driver";
  static final int DEFAULT_PORT = 5432;
  static final String DATABASE_NAME = "postgres";

  public PostgreSQLStarter() {
    String url = String.format("jdbc:postgresql://%s:%d/%s?loggerLevel=OFF", this.dockerHost(), DEFAULT_PORT, DATABASE_NAME);
    this.configuration
      .set(PostgreSQLEntry.HOST, this.dockerHost())
      .set(PostgreSQLEntry.PORT, DEFAULT_PORT)
      .set(PostgreSQLEntry.USERNAME, this.username())
      .set(PostgreSQLEntry.PASSWORD, PASSWORD)
      .set(PostgreSQLEntry.DRIVER, JDBC_DRIVER_NAME)
      .set(PostgreSQLEntry.URL, url);
  }

  @Override
  protected PostgreSQLContainer container() {
    return new PostgreSQLContainer().withUsername(this.username())
      .withPassword(PASSWORD)
      .withDatabaseName(DATABASE_NAME);
  }

  @Override
  protected void setConfiguration(PostgreSQLContainer container) {
    this.configuration
      .set(PostgreSQLEntry.USERNAME, container.getUsername())
      .set(PostgreSQLEntry.URL, container.getJdbcUrl())
      .set(PostgreSQLEntry.DRIVER, container.getDriverClassName());
  }

  @Override
  protected void setConfiguration(DatabaseConfiguration databaseConfiguration) {
    this.configuration
      .set(PostgreSQLEntry.USERNAME, databaseConfiguration.getUsername())
      .set(PostgreSQLEntry.PASSWORD, databaseConfiguration.getPassword())
      .set(PostgreSQLEntry.URL, databaseConfiguration.getUrl())
      .set(PostgreSQLEntry.DRIVER, "org.h2.Driver");
  }

  @Override
  protected TargetDatabase targetDatabase() {
    return TargetDatabase.POSTGRESQL;
  }

  @Override
  public Stream<Port> ports() {
    return Arrays.asList(Port.create(DEFAULT_PORT)).stream();
  }

  @Override
  public SocketType socketType() {
    return SocketType.TCP;
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
