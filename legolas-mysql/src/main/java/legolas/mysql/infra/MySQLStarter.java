package legolas.mysql.infra;

import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatasourceFactory;
import legolas.sql.interfaces.SQLStarter;
import legolas.sql.interfaces.TargetDatabase;
import legolas.mysql.interfaces.MySQLEntry;
import legolas.mysql.interfaces.MySQLServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.MySQLContainer;
import toolbox.data.interfaces.SQLExecutor;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class MySQLStarter extends SQLStarter<MySQLContainer> {
  static final String DEFAULT_PASSWORD = "mysql";
  static final String JDBC_DRIVER_NAME = "com.mysql.jdbc.Driver";
  static final Integer DEFAULT_PORT = 3306;

  public MySQLStarter() {
    String url = String.format("jdbc:mysql://%s:%s/%s", this.dockerHost(), DEFAULT_PORT, this.databaseName());
    this.configuration
      .set(MySQLEntry.HOST, this.dockerHost())
      .set(MySQLEntry.PORT, DEFAULT_PORT)
      .set(MySQLEntry.USERNAME, this.username())
      .set(MySQLEntry.PASSWORD, DEFAULT_PASSWORD)
      .set(MySQLEntry.DRIVER, JDBC_DRIVER_NAME)
      .set(MySQLEntry.URL, url);
  }

  @Override
  protected MySQLContainer container() {
    return new MySQLContainer().withUsername(this.username()).withPassword(DEFAULT_PASSWORD);
  }

  @Override
  protected void setConfiguration(MySQLContainer container) {
    this.configuration.set(MySQLEntry.URL, container.getJdbcUrl());
  }

  public String databaseName() {
    return super.username().toLowerCase();
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
