package legolas.docker.interfaces;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.google.common.collect.Maps;
import legolas.config.api.interfaces.Configuration;
import legolas.runtime.core.interfaces.RuntimeEnvironment;
import legolas.starter.api.interfaces.PortStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public abstract class DockerStarter<C extends GenericContainer> implements PortStarter {
  protected static final Logger logger = LoggerFactory.getLogger(DockerStarter.class);
  private static final Long DEFAULT_STARTUP_TIMEOUT_SECONDS = 240L;

  static {
    setReusable();
  }

  protected final Configuration configuration;
  protected final DockerClientFactory dockerClientFactory;

  public DockerStarter() {
    this.configuration = Configuration.create();
    this.dockerClientFactory = DockerClientFactory.instance();
  }

  private static void setReusable() {
    try {
      File file = new File(System.getProperty("user.home"), ".testcontainers.properties");
      if (!file.exists()) {
        file.createNewFile();
      }

      Properties properties = new Properties();
      properties.load(new FileInputStream(file));
      properties.put("testcontainers.reuse.enable", "true");
      properties.store(new FileOutputStream(file), "Updated by Legolas OSS");
    } catch (Exception e) {
      logger.warn("It was not possible to activate reuse for containers", e);
    }
  }

  @Override
  public final void start(RuntimeEnvironment runtimeEnvironment) {
    Map<String, String> labels = labels();

    C targetContainer = this.container();

    if(runtimeEnvironment == RuntimeEnvironment.LOCAL) {
      startContainer(labels);
      targetContainer.withReuse(true).withNetwork(null).withLabels(labels);
    }

    List<String> portBindings = this.ports().map(port -> String.format("%d:%d", port.value(), port.value())).collect(Collectors.toList());
    targetContainer.setPortBindings(portBindings);

    targetContainer.withLogConsumer(new Slf4jLogConsumer(logger)).start();

    this.setConfiguration(targetContainer);
  }

  private Map<String, String> labels() {
    Map<String, String> labels = Maps.newHashMap();
    labels.put("legolas-starter", this.id().value());
    return labels;
  }

  private void startContainer(Map<String, String> labels) {
    DockerClient client = this.dockerClientFactory.client();
    List<Container> containers = client.listContainersCmd().withShowAll(true).withLabelFilter(labels).exec();
    for (Container container : containers) {
      String state = container.getState();
      if ("exited".equalsIgnoreCase(state)) {
        client.startContainerCmd(container.getId()).exec();
      }
    }
  }

  public void stop() {
    Map<String, String> labels = this.labels();
    DockerClient client = this.dockerClientFactory.client();
    List<Container> containers = client.listContainersCmd().withShowAll(true).withLabelFilter(labels).exec();
    for (Container container : containers) {
      String state = container.getState();
      if ("running".equalsIgnoreCase(state)) {
        client.stopContainerCmd(container.getId()).exec();
      }
    }
  }

  protected abstract void setConfiguration(C container);

  protected abstract C container();

  @Override
  public final Configuration configuration() {
    return this.configuration;
  }

  protected String dockerHost() {
    return this.dockerClientFactory.dockerHostIpAddress();
  }
}
