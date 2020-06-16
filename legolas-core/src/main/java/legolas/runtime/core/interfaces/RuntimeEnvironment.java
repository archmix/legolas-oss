package legolas.runtime.core.interfaces;

import legolas.async.api.interfaces.Promise;
import legolas.config.api.interfaces.Configuration;

import java.util.Properties;
import java.util.concurrent.ExecutorService;

public enum RuntimeEnvironment {
  LOCAL {
    @Override
    protected LifecycleEnvironment lifecycleEnvironment() {
      return new LocalEnvironment();
    }
  },
  SERVER {
    @Override
    protected LifecycleEnvironment lifecycleEnvironment() {
      return new ServerEnvironment();
    }
  },
  TEST {
    @Override
    protected LifecycleEnvironment lifecycleEnvironment() {
      return new TestEnvironment();
    }
  };

  public Promise<RunningEnvironment> start(ExecutorService executorService) {
    return this.start(executorService, new Properties());
  }

  public Promise<RunningEnvironment> start(ExecutorService executorService, Properties properties) {
    Promise<RunningEnvironment> promise = Promise.create();
    Configuration configuration = Configuration.create(properties);

    executorService.execute(() -> {
      this.lifecycleEnvironment().start(this, configuration, promise);
    });

    return promise;
  }

  protected abstract LifecycleEnvironment lifecycleEnvironment();
}
