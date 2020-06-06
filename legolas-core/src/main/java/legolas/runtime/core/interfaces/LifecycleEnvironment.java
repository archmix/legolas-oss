package legolas.runtime.core.interfaces;

import legolas.async.api.interfaces.Promise;
import legolas.config.api.interfaces.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

public abstract class LifecycleEnvironment {
  private static final Logger logger = LoggerFactory.getLogger(LifecycleEnvironment.class);

  final void start(final RuntimeEnvironment runtimeEnvironment, Configuration configuration, Promise<RunningEnvironment> promise) {
    try {
      RunningEnvironment environment = RunningEnvironment.create(runtimeEnvironment, configuration);
      this.run(environment);
      promise.complete(environment);
    } catch (RuntimeException e) {
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