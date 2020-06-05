package legolas.runtime.core.interfaces;

class ServerEnvironment extends LifecycleEnvironment {
    @Override
    protected void run(RunningEnvironment environment) {
        this.bootstrappers(environment);
        this.migrations(environment);
    }
}
