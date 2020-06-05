package legolas.docker.interfaces;

import legolas.config.api.interfaces.Configuration;
import legolas.starter.api.interfaces.PortStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;

public abstract class DockerStarter implements PortStarter {
    protected static final Logger logger = LoggerFactory.getLogger(DockerStarter.class);
    protected final Configuration configuration;

    public DockerStarter() {
        this.configuration = Configuration.create();
    }

    @Override
    public final void start() {
        GenericContainer container = this.startContainer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> container.stop()));
    }

    protected abstract GenericContainer startContainer();

    @Override
    public final Configuration configuration() {
        return this.configuration;
    }

    protected String dockerHost() {
        return DockerClientFactory.instance().dockerHostIpAddress();
    }
}
