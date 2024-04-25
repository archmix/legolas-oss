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
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class DockerStarter<C extends GenericContainer> implements PortStarter {
  protected static final Logger logger = LoggerFactory.getLogger(DockerStarter.class);
  private static final Long DEFAULT_STARTUP_TIMEOUT_SECONDS = 240L;
  public static final String EXITED_STATE = "exited";
  public static final String RUNNING_STATE = "running";

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
        boolean created = file.createNewFile();
        if(!created) {
          logger.warn("*** Cannot create testcontainers properties file. Has user permission to create files? ***");
          return;
        }
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
    this.configure(runtimeEnvironment);
  }

  @Override
  public void attach(RuntimeEnvironment runtimeEnvironment) {
    this.configure(runtimeEnvironment);
  }

  private void configure(RuntimeEnvironment runtimeEnvironment){
    Map<String, String> labels = labels();

    if(runtimeEnvironment == RuntimeEnvironment.TEST){
      this.execute(this::stopContainer);
      this.remove();
    }

    C targetContainer = this.container();
    targetContainer.withNetwork(null).withLabels(labels);

    if(runtimeEnvironment == RuntimeEnvironment.LOCAL) {
      targetContainer.withReuse(true);
    }

    List<String> portBindings = this.ports().map(port -> String.format("%d:%d", port.value(), port.value())).collect(Collectors.toList());
    targetContainer.setPortBindings(portBindings);
    targetContainer.withLogConsumer(new Slf4jLogConsumer(logger));

    this.execute(this::startContainer);
    targetContainer.start();
    this.setConfiguration(targetContainer);
  }

  private Map<String, String> labels() {
    Map<String, String> labels = Maps.newHashMap();
    labels.put("legolas-starter", this.id().value());
    return labels;
  }

  @Override
  public void stop() {
    this.execute(this::stopContainer);
  }

  private void remove() {
    this.execute((client, container) ->{
      if(EXITED_STATE.equalsIgnoreCase(container.getState())){
        client.removeContainerCmd(container.getId()).exec();
      }
    });
  }

  private void startContainer(DockerClient client, Container container){
    if(EXITED_STATE.equalsIgnoreCase(container.getState())){
      logger.info("Starting container {}", container.getId());
      client.startContainerCmd(container.getId()).exec();
    }
  }

  private void stopContainer(DockerClient client, Container container){
    if(RUNNING_STATE.equalsIgnoreCase(container.getState())){
      client.stopContainerCmd(container.getId()).exec();
    }
  }

  private void execute(BiConsumer<DockerClient, Container> consumer){
    Map<String, String> labels = this.labels();
    DockerClient client = this.dockerClientFactory.client();
    List<Container> containers = client.listContainersCmd().withShowAll(true).withLabelFilter(labels).exec();
    for (Container container : containers) {
      consumer.accept(client, container);

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
