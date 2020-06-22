package legolas.sqlserver.infra;

import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatasourceFactory;
import legolas.sql.interfaces.SQLExecutor;
import legolas.sql.interfaces.SQLStarter;
import legolas.sql.interfaces.TargetDatabase;
import legolas.sqlserver.interfaces.SQLServerEntry;
import legolas.sqlserver.interfaces.SQLServerServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.MSSQLServerContainer;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class SQLServerStarter extends SQLStarter<MSSQLServerContainer> {
  static final String DEFAULT_PASSWORD = "$qlS3rver";
  static final String JDBC_DRIVER_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  static final Integer DEFAULT_PORT = 1433;

  public SQLServerStarter() {
    String url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s", this.dockerHost(), DEFAULT_PORT, this.databaseName());
    this.configuration
      .set(SQLServerEntry.HOST, this.dockerHost())
      .set(SQLServerEntry.PORT, DEFAULT_PORT)
      .set(SQLServerEntry.USERNAME, this.username())
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
    DataSource dataSource = DatasourceFactory.toDataSource(container.getJdbcUrl(), JDBC_DRIVER_NAME, this.username(), DEFAULT_PASSWORD);
    SQLExecutor sqlExecutor = SQLExecutor.create(dataSource);

    String databaseExists = "SELECT 1 FROM sys.databases WHERE name = ?";
    Integer size = sqlExecutor.query(databaseExists, this.databaseName()).size();
    if(size == 0){
      String createDatabase = MessageFormat.format("CREATE DATABASE {0}", this.databaseName());
      sqlExecutor.execute(createDatabase);
    }
  }

  public String databaseName() {
    return super.username().toLowerCase();
  }

  @Override
  protected String username() {
    return "sa";
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
