package legolas.runtime.core.interfaces;

import legolas.config.api.interfaces.Configuration;
import legolas.net.core.interfaces.LocalPortBinding;
import legolas.starter.api.interfaces.PortStarter;
import legolas.starter.api.interfaces.Starter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

class StarterRunner {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public void run(RunningEnvironment environment) {
    ServiceLoader.load(Starter.class).forEach(service -> {
      logger.info("{} initializing", service.name());

      Configuration configuration = service.configuration();
      environment.add(RunningInstance.create(service.id(), configuration));

      boolean shouldStart = true;
      if (service instanceof PortStarter) {
        shouldStart = this.shouldStart((PortStarter) service, environment.runtimeEnvironment());
      }

      if (shouldStart) {
        service.start(environment.runtimeEnvironment());
      }
    });
  }

  private boolean shouldStart(PortStarter starter, RuntimeEnvironment runtimeEnvironment) {
    if (runtimeEnvironment == RuntimeEnvironment.TEST) {
      starter.stop();
    }

    LocalPortBinding localPortBinding = LocalPortBinding.create(starter.socketType());

    return starter.ports().allMatch(localPortBinding.availablePredicate());
  }
}