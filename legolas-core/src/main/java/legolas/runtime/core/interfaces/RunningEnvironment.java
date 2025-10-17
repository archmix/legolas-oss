package legolas.runtime.core.interfaces;

import legolas.config.api.interfaces.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class RunningEnvironment {
  private final RuntimeEnvironment runtime;
  private final Map<String, RunningInstance> instances;
  private final Configuration configuration;

  private RunningEnvironment(RuntimeEnvironment runtime, Configuration configuration) {
    this.runtime = runtime;
    this.instances = new HashMap<>();
    this.configuration = configuration;
  }

  static RunningEnvironment create(RuntimeEnvironment runtime, Configuration configuration) {
    return new RunningEnvironment(runtime, configuration);
  }

  RuntimeEnvironment runtimeEnvironment() {
    return this.runtime;
  }

  public Configuration configuration() {
    return this.configuration;
  }

  public RunningEnvironment add(RunningInstance instance) {
    this.instances.put(instance.id().value(), instance);
    return this;
  }

  public <T extends RunningInstance> Optional<T> get(ServiceId id) {
    return Optional.ofNullable((T) this.instances.get(id.value()));
  }
}
