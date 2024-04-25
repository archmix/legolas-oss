package legolas.starter.api.interfaces;

import legolas.config.api.interfaces.Configuration;
import legolas.runtime.core.interfaces.LifecycleService;
import legolas.runtime.core.interfaces.RuntimeEnvironment;

public interface Starter extends LifecycleService {
  void start(RuntimeEnvironment runtimeEnvironment);

  void attach(RuntimeEnvironment runtimeEnvironment);

  Configuration configuration();
}
