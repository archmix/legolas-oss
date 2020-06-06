package legolas.sql.interfaces;

import legolas.docker.interfaces.DockerStarter;
import org.testcontainers.containers.GenericContainer;

public abstract class SQLStarter<C extends GenericContainer> extends DockerStarter<C> {

  @Override
  protected final void fallbackStart(RuntimeException e) {
    logger.error("Docker reported", e);
    logger.warn("It was not possible to start docker container, so I am providing H2 instance as fallback");
    DatabaseConfiguration databaseConfiguration = DatabaseConfiguration.h2Fallback(this.targetDatabase());

    this.setConfiguration(databaseConfiguration);
  }

  protected String username() {
    return System.getProperty("user.name");
  }

  protected abstract void setConfiguration(DatabaseConfiguration databaseConfiguration);

  protected abstract TargetDatabase targetDatabase();
}
