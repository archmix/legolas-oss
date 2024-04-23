package legolas.sql.interfaces;

import legolas.config.api.interfaces.Entry;
import legolas.docker.interfaces.DockerStarter;
import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.util.Arrays;
import java.util.stream.Stream;

public abstract class SQLStarter<C extends JdbcDatabaseContainer> extends DockerStarter<C> {
  @Override
  protected final void setConfiguration(C container) {
    this.configuration
      .set(urlEntry(), container.getJdbcUrl())
      .set(hostEntry(), this.dockerHost())
      .set(portEntry(), container.getMappedPort(defaultPort()))
      .set(usernameEntry(), container.getUsername())
      .set(passwordEntry(), container.getPassword())
      .set(driverEntry(), container.getDriverClassName());

    this.setExtraConfiguration(container);
  }

  protected void setExtraConfiguration(C container) {
    return;
  }

  public final Stream<Port> ports() {
    return Arrays.asList(Port.create(defaultPort())).stream();
  }

  @Override
  public final SocketType socketType() {
    return SocketType.TCP;
  }

  protected abstract Integer defaultPort();

  protected abstract Entry urlEntry();

  protected abstract Entry hostEntry();

  protected abstract Entry portEntry();

  protected abstract Entry usernameEntry();

  protected abstract Entry passwordEntry();

  protected abstract Entry driverEntry();

  protected abstract TargetDatabase targetDatabase();
}
