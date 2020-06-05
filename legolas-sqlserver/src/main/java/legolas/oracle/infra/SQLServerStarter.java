package legolas.oracle.infra;

import legolas.docker.interfaces.DockerStarter;
import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.oracle.interfaces.SQLServerEntry;
import legolas.oracle.interfaces.SQLServerServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class SQLServerStarter extends DockerStarter {
    static final String DEFAULT_PASSWORD = "A_Str0ng_Required_Password";
    static final String JDBC_DRIVER_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final int DEFAULT_PORT = 1433;

    public SQLServerStarter() {
        String url = String.format("jdbc:sqlserver://%s:%d", this.dockerHost(), DEFAULT_PORT);
        this.configuration
                .set(SQLServerEntry.HOST, this.dockerHost())
                .set(SQLServerEntry.PORT, DEFAULT_PORT)
                .set(SQLServerEntry.USERNAME, "SA")
                .set(SQLServerEntry.PASSWORD, DEFAULT_PASSWORD)
                .set(SQLServerEntry.DRIVER, JDBC_DRIVER_NAME)
                .set(SQLServerEntry.URL, url);
    }

    @Override
    protected GenericContainer startContainer() {
        MSSQLServerContainer container = new MSSQLServerContainer().withPassword(DEFAULT_PASSWORD);
        container.setPortBindings(Arrays.asList("1433:1433"));
        container.withLogConsumer(new Slf4jLogConsumer(logger)).start();
        this.configuration
                .set(SQLServerEntry.USERNAME, container.getUsername())
                .set(SQLServerEntry.URL, container.getJdbcUrl())
                .set(SQLServerEntry.DRIVER, container.getDriverClassName());
        return container;
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
