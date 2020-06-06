package legolas.net.core.interfaces;

import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor(staticName = "create")
public class LocalPortBinding {
  private final LocalPortAvailability localPortAvailability;

  public boolean available(Port port) {
    return this.localPortAvailability.isPortAvailable(port);
  }

  public Predicate<Port> availablePredicate() {
    return ((port) -> {
      return this.localPortAvailability.isPortAvailable(port);
    });
  }

  public Port nextPortAvailable(Port port) {
    Port candidatePort = port;

    while (!this.localPortAvailability.isPortAvailable(candidatePort)) {
      candidatePort = candidatePort.next();
    }

    return candidatePort;
  }
}
