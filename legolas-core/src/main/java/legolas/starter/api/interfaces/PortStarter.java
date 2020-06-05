package legolas.starter.api.interfaces;

import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;

import java.util.stream.Stream;

public interface PortStarter extends Starter {
    Stream<Port> ports();

    SocketType socketType();
}
