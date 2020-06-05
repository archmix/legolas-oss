package legolas.bootstrapper.api.interfaces;

import legolas.config.api.interfaces.Configuration;
import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.runtime.core.interfaces.LifecycleService;

import java.util.stream.Stream;

public interface NetworkBootstrapper extends LifecycleService {
    Configuration bootstrap(Stream<Port> availablePorts);

    Stream<Port> ports();

    SocketType socketType();
}
