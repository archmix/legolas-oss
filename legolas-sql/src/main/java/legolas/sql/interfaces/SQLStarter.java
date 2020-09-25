package legolas.sql.interfaces;

import legolas.docker.interfaces.DockerStarter;
import legolas.docker.interfaces.Username;
import org.testcontainers.containers.GenericContainer;

import java.util.regex.Pattern;

public abstract class SQLStarter<C extends GenericContainer> extends DockerStarter<C> {
  protected String username() {
    return Username.valueOf().value();
  }

  protected abstract TargetDatabase targetDatabase();
}
