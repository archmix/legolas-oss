package legolas.postgre.infra;

import legolas.docker.interfaces.DockerStarter;
import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.postgre.interfaces.PostgreSQLEntry;
import legolas.postgre.interfaces.PostgreSQLServiceId;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class PostgreSQLStarter extends DockerStarter {
    static final String DEFAULT_PASSWORD = "A_Str0ng_Required_Password";
    static final String JDBC_DRIVER_NAME = "org.postgresql.Driver";
    static final int DEFAULT_PORT = 5432;
    static final String DATABASE_NAME = "postgre";

    public PostgreSQLStarter() {
        String url = String.format("jdbc:postgresql://%s:%d/%s?loggerLevel=OFF", this.dockerHost(), DEFAULT_PORT, DATABASE_NAME);
        this.configuration
                .set(PostgreSQLEntry.HOST, this.dockerHost())
                .set(PostgreSQLEntry.PORT, DEFAULT_PORT)
                .set(PostgreSQLEntry.USERNAME, "SA")
                .set(PostgreSQLEntry.PASSWORD, DEFAULT_PASSWORD)
                .set(PostgreSQLEntry.DRIVER, JDBC_DRIVER_NAME)
                .set(PostgreSQLEntry.URL, url);
    }

    @Override
    protected GenericContainer startContainer() {
        PostgreSQLContainer container = new PostgreSQLContainer()
                .withPassword(DEFAULT_PASSWORD)
                .withDatabaseName(DATABASE_NAME);
        container.withLogConsumer(new Slf4jLogConsumer(logger)).start();
        this.configuration
                .set(PostgreSQLEntry.USERNAME, container.getUsername())
                .set(PostgreSQLEntry.URL, container.getJdbcUrl())
                .set(PostgreSQLEntry.DRIVER, container.getDriverClassName());
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
        return PostgreSQLServiceId.INSTANCE;
    }

    @Override
    public String name() {
        return "SQL Server Container";
    }
}
