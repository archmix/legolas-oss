package legolas.runtime.core.interfaces;

import legolas.config.api.interfaces.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RunningEnvironment {
    private final RuntimeEnvironment runtime;
    private final Map<String, RunningInstance> instances;
    private final Configuration configuration;

    private RunningEnvironment(RuntimeEnvironment runtime) {
        this.runtime = runtime;
        this.instances = new HashMap<>();
        this.configuration = Configuration.create(new HashMap<>());
    }

    public static RunningEnvironment create(RuntimeEnvironment runtime) {
        return new RunningEnvironment(runtime);
    }

    public RuntimeEnvironment runtimeEnvironment() {
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
