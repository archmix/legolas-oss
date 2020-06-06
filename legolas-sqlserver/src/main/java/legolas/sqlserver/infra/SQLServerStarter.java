package legolas.sqlserver.infra;

import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatabaseConfiguration;
import legolas.sql.interfaces.SQLStarter;
import legolas.sql.interfaces.TargetDatabase;
import legolas.sqlserver.interfaces.SQLServerEntry;
import legolas.sqlserver.interfaces.SQLServerServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.MSSQLServerContainer;

import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class SQLServerStarter extends SQLStarter<MSSQLServerContainer> {
  static final String DEFAULT_PASSWORD = "A_Str0ng_Required_Password";
  static final String JDBC_DRIVER_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  static final int DEFAULT_PORT = 1433;

  public SQLServerStarter() {
    String url = String.format("jdbc:sqlserver://%s:%d", this.dockerHost(), DEFAULT_PORT);
    this.configuration
      .set(SQLServerEntry.HOST, this.dockerHost())
      .set(SQLServerEntry.PORT, DEFAULT_PORT)
      .set(SQLServerEntry.USERNAME, "SA")
      .set(SQLServerEntry.PASSWORD, DEFAULT_PASSWORD)
      .set(SQLServerEntry.DRIVER, JDBC_DRIVER_NAME)
      .set(SQLServerEntry.URL, url);
  }

  @Override
  protected MSSQLServerContainer container() {
    return new MSSQLServerContainer().withPassword(DEFAULT_PASSWORD);
  }

  @Override
  protected void setConfiguration(MSSQLServerContainer container) {
    this.configuration
      .set(SQLServerEntry.USERNAME, container.getUsername())
      .set(SQLServerEntry.URL, container.getJdbcUrl())
      .set(SQLServerEntry.DRIVER, container.getDriverClassName());
  }

  @Override
  protected void setConfiguration(DatabaseConfiguration databaseConfiguration) {
    this.configuration
      .set(SQLServerEntry.USERNAME, databaseConfiguration.getUsername())
      .set(SQLServerEntry.PASSWORD, databaseConfiguration.getPassword())
      .set(SQLServerEntry.URL, databaseConfiguration.getUrl())
      .set(SQLServerEntry.DRIVER, "org.h2.Driver");
  }

  @Override
  protected TargetDatabase targetDatabase() {
    return TargetDatabase.SQL_SERVER;
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
    return SQLServerServiceId.INSTANCE;
  }

  @Override
  public String name() {
    return "SQL Server Container";
  }
}
