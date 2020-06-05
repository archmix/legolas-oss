package legolas.runtime.core.interfaces;

import legolas.async.api.interfaces.Promise;
import legolas.async.api.interfaces.Promises;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public abstract class LifecycleEnvironment {
    private static final Logger logger = LoggerFactory.getLogger(LifecycleEnvironment.class);

    final void start(final RuntimeEnvironment runtimeEnvironment, Promise<RunningEnvironment> promise) {
        try{
            RunningEnvironment environment = RunningEnvironment.create(runtimeEnvironment);
            this.run(environment);
            promise.complete(environment);
        } catch (RuntimeException e){
            promise.fail(e);
        }
    }

    protected abstract void run(RunningEnvironment environment);

    protected void bootstrappers(RunningEnvironment environment) {
        logger.info("Running bootstrapers...");
        new BootstrapRunner().run(environment);
    }

    protected void migrations(RunningEnvironment environment) {
        logger.info("Running migrations...");
        new MigrationRunner().run(environment);
    }

    protected void starters(RunningEnvironment environment) {
        logger.info("Running starters...");
        new StarterRunner().run(environment);
    }
}