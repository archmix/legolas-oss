package legolas.runtime.core.interfaces;

import legolas.bootstrapper.api.interfaces.Bootstrapper;
import legolas.bootstrapper.api.interfaces.NetworkBootstrapper;
import legolas.config.api.interfaces.Configuration;
import legolas.net.core.interfaces.LocalPortBinding;
import legolas.net.core.interfaces.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

class BootstrapRunner {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public void run(RunningEnvironment environment) {
    ServiceLoader.load(Bootstrapper.class)
      .forEach(service -> {
        logger.info("{} initializing", service.name());
        RunningInstance instance = null;

        if (service instanceof NetworkBootstrapper) {
          environment.add(
            this.bootstrap((NetworkBootstrapper) service)
          );
        } else {
          environment.add(
            this.bootstrap(service, environment.configuration())
          );
          logger.info("{} started", service.name());
        }

        environment.add(instance);
      });
  }

  private RunningInstance bootstrap(Bootstrapper bootstrapper, Configuration configuration) {
    Object reference = bootstrapper.bootstrap(configuration);
    logger.info("{} properly bootstrapped", bootstrapper.name());
    return RunningInstance.create(bootstrapper.id(), configuration, reference);
  }

  private RunningInstance bootstrap(NetworkBootstrapper service) {
    LocalPortBinding localPortBinding = LocalPortBinding.create(service.socketType());

    List<Port> availablePorts = new ArrayList<>();
    service.ports().forEach(port -> {
      availablePorts.add(localPortBinding.nextPortAvailable(port));
    });

    Configuration configuration = service.bootstrap(availablePorts.stream());

    logger.info("{} started using ports {}", service.name(), availablePorts.stream().map(port -> port.toString()).collect(Collectors.joining(",")));
    return RunningInstance.create(service.id(), configuration);
  }
}
