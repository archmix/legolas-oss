package legolas.oracle.infra;

import legolas.docker.interfaces.DockerStarter;
import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.oracle.interfaces.OracleEntry;
import legolas.oracle.interfaces.OracleServiceId;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.Arrays;
import java.util.stream.Stream;

@StarterComponent
public class OracleStarter extends DockerStarter {
    static final String DEFAULT_PASSWORD = "A_Str0ng_Required_Password";
    static final String JDBC_DRIVER_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final int DEFAULT_PORT = 1521;

    public OracleStarter() {
        String url = String.format("jdbc:oracle:thin:@%s/:%d:xe", this.dockerHost(), DEFAULT_PORT);
        this.configuration
                .set(OracleEntry.HOST, this.dockerHost())
                .set(OracleEntry.PORT, DEFAULT_PORT)
                .set(OracleEntry.USERNAME, "system")
                .set(OracleEntry.PASSWORD, DEFAULT_PASSWORD)
                .set(OracleEntry.DRIVER, JDBC_DRIVER_NAME)
                .set(OracleEntry.URL, url);
    }

    @Override
    protected GenericContainer startContainer() {
        OracleContainer container = new OracleContainer("owncloudci/oracle-xe").withPassword(DEFAULT_PASSWORD);
        container.setPortBindings(Arrays.asList("1521:1521"));
        container.withLogConsumer(new Slf4jLogConsumer(logger)).start();
        this.configuration
                .set(OracleEntry.USERNAME, container.getUsername())
                .set(OracleEntry.URL, container.getJdbcUrl())
                .set(OracleEntry.DRIVER, container.getDriverClassName());
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
        return OracleServiceId.INSTANCE;
    }

    @Override
    public String name() {
        return "SQL Server Container";
    }
}
