package legolas.oracle.infra;

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
import toolbox.data.interfaces.DataSet;
import toolbox.data.interfaces.SQLExecutor;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class OracleStarter extends SQLStarter<OracleContainer> {
  static final String PASSWORD = "oracle";
  static final String JDBC_DRIVER_NAME = "oracle.jdbc.OracleDriver";
  static final Integer DEFAULT_PORT = 1521;

  public OracleStarter() {
    System.setProperty("oracle.jdbc.timezoneAsRegion", "false");
    String url = String.format("jdbc:oracle:thin:@%s:%d/xe", this.dockerHost(), DEFAULT_PORT);
    this.configuration
      .set(OracleEntry.HOST, this.dockerHost())
      .set(OracleEntry.PORT, DEFAULT_PORT)
      .set(OracleEntry.USERNAME, this.username())
      .set(OracleEntry.SCHEMA, this.username())
      .set(OracleEntry.PASSWORD, PASSWORD)
      .set(OracleEntry.DRIVER, JDBC_DRIVER_NAME)
      .set(OracleEntry.URL, url)
      .set(OracleEntry.SID, "xe");
  }

  @Override
  protected OracleContainer container() {
    return new OracleContainer("oracleinanutshell/oracle-xe-11g");
  }

  @Override
  protected void setConfiguration(OracleContainer container) {
    DataSource dataSource = DatasourceFactory.toDataSource(container.getJdbcUrl(), JDBC_DRIVER_NAME);
    SQLExecutor sqlExecutor = SQLExecutor.create(dataSource);

    String selectUser = "SELECT 1 FROM all_users WHERE username = ?";
    DataSet dataSet = sqlExecutor.query(selectUser, this.username().toUpperCase());
    if (!dataSet.isEmpty()) {
      return;
    }

    String createUser = String.format("CREATE USER %s IDENTIFIED BY %s", this.username(), PASSWORD);
    String grantDBA = String.format("GRANT DBA TO %s", this.username());
    sqlExecutor.execute(createUser);
    sqlExecutor.execute(grantDBA);
  }

  @Override
  protected TargetDatabase targetDatabase() {
    return TargetDatabase.ORACLE;
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
    return OracleServiceId.INSTANCE;
  }

  @Override
  public String name() {
    return "Oracle Container";
  }
}
