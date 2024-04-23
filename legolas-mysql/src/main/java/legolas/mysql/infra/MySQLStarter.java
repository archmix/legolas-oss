package legolas.mysql.infra;

import legolas.config.api.interfaces.Entry;
import legolas.mysql.interfaces.MySQLEntry;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.SQLStarter;
import legolas.sql.interfaces.TargetDatabase;
import legolas.mysql.interfaces.MySQLServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@StarterComponent
public class MySQLStarter extends SQLStarter<MySQLContainer> {

  @Override
  protected MySQLContainer container() {
    return new MySQLContainer(DockerImageName.parse("mysql:8.0.36"));
  }

  @Override
  protected TargetDatabase targetDatabase() {
    return TargetDatabase.MYSQL;
  }

  @Override
  public ServiceId id() {
    return MySQLServiceId.INSTANCE;
  }

  @Override
  public String name() {
    return "My SQL Container";
  }

  @Override
  protected Integer defaultPort() {
    return MySQLContainer.MYSQL_PORT;
  }

  @Override
  protected Entry urlEntry() {
    return MySQLEntry.URL;
  }

  @Override
  protected Entry hostEntry() {
    return MySQLEntry.HOST;
  }

  @Override
  protected Entry portEntry() {
    return MySQLEntry.PORT;
  }

  @Override
  protected Entry usernameEntry() {
    return MySQLEntry.USERNAME;
  }

  @Override
  protected Entry passwordEntry() {
    return MySQLEntry.PASSWORD;
  }

  @Override
  protected Entry driverEntry() {
    return MySQLEntry.DRIVER;
  }
}
