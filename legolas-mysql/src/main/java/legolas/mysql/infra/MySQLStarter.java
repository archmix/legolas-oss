package legolas.mysql.infra;

import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatasourceFactory;
import legolas.sql.interfaces.SQLExecutor;
import legolas.sql.interfaces.SQLStarter;
import legolas.sql.interfaces.TargetDatabase;
import legolas.mysql.interfaces.MySQLEntry;
import legolas.mysql.interfaces.MySQLServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class MySQLStarter extends SQLStarter<MySQLContainer> {
  static final String DEFAULT_PASSWORD = "my-secret-pw";
  static final String DEFAULT_USER = "root";
  static final String JDBC_DRIVER_NAME = "com.mysql.jdbc.Driver";
  static final Integer DEFAULT_PORT = 3306;

  public MySQLStarter() {
    String url = String.format("jdbc:mysql://%s:%s/%s", this.dockerHost(), DEFAULT_PORT, this.databaseName());
    this.configuration
      .set(MySQLEntry.HOST, this.dockerHost())
      .set(MySQLEntry.PORT, DEFAULT_PORT)
      .set(MySQLEntry.USERNAME, DEFAULT_USER)
      .set(MySQLEntry.PASSWORD, DEFAULT_PASSWORD)
      .set(MySQLEntry.DRIVER, JDBC_DRIVER_NAME)
      .set(MySQLEntry.URL, url);
  }

  @Override
  protected MySQLContainer container() {
    return new MySQLContainer().withPassword(DEFAULT_PASSWORD);
  }

  @Override
  protected void setConfiguration(MySQLContainer container) {
    DataSource dataSource = DatasourceFactory.toDataSource(container.getJdbcUrl(), JDBC_DRIVER_NAME, this.username(), DEFAULT_PASSWORD);
    SQLExecutor sqlExecutor = SQLExecutor.create(dataSource);

    String databaseExists = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
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
    return DEFAULT_USER;
  }

  @Override
  protected TargetDatabase targetDatabase() {
    return TargetDatabase.MYSQL;
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
    return MySQLServiceId.INSTANCE;
  }

  @Override
  public String name() {
    return "My SQL Container";
  }
}
