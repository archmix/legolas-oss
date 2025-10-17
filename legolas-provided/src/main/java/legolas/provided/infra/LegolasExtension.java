package legolas.provided.infra;

import legolas.runtime.core.interfaces.RunningEnvironment;
import legolas.runtime.core.interfaces.RuntimeEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public final class LegolasExtension implements ParameterResolver, BeforeAllCallback {
  private static RunningEnvironment environment;

  @Override
  public synchronized void beforeAll(ExtensionContext context) {
    if(environment != null) {
      return;
    }

    log.info("Starting Legolas global test environment...");
    var executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "legolas-global-thread"));
    environment = RuntimeEnvironment.TEST.start(executor).get();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Shutting down Legolas global test environment...");
      executor.shutdownNow();
    }));
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return RunningEnvironment.class.equals(parameterContext.getParameter().getType());
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return environment;
  }
}