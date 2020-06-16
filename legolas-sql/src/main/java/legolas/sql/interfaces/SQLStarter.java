package legolas.sql.interfaces;

import legolas.docker.interfaces.DockerStarter;
import org.testcontainers.containers.GenericContainer;

public abstract class SQLStarter<C extends GenericContainer> extends DockerStarter<C> {

  protected String username() {
    return System.getProperty("user.name");
  }

  protected abstract TargetDatabase targetDatabase();
}
