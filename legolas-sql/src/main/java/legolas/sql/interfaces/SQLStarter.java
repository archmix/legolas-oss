package legolas.sql.interfaces;

import legolas.docker.interfaces.DockerStarter;
import org.testcontainers.containers.GenericContainer;

import java.util.regex.Pattern;

public abstract class SQLStarter<C extends GenericContainer> extends DockerStarter<C> {
  private static final Pattern usernamePattern = Pattern.compile("[^A-Za-z0-9]");

  protected String username() {
    String username =  System.getProperty("user.name");
    return usernamePattern.matcher(username).replaceAll("_").trim();
  }

  protected abstract TargetDatabase targetDatabase();
}
