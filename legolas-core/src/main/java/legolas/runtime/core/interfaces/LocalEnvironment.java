package legolas.runtime.core.interfaces;

class LocalEnvironment extends LifecycleEnvironment {
    @Override
    protected void run(RunningEnvironment environment) {
        this.starters(environment);
        this.bootstrappers(environment);
        this.migrations(environment);
    }
}
