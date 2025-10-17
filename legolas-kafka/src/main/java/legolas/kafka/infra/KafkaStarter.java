package legolas.kafka.infra;

import legolas.docker.interfaces.DockerStarter;
import legolas.kafka.interfaces.KafkaEntry;
import legolas.kafka.interfaces.KafkaServiceId;
import legolas.net.core.interfaces.Port;
import legolas.net.core.interfaces.SocketType;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.starter.api.interfaces.StarterComponent;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

@StarterComponent
public class KafkaStarter extends DockerStarter<KafkaContainer> {

  static final int DEFAULT_PORT = 9092;
  private final KafkaContainer container = new KafkaContainer();

  @Override
  protected void setConfiguration(final KafkaContainer container) {
    this.configuration
      .set(KafkaEntry.KAFKA_BROKER, container.getBootstrapServers());
  }

  @Override
  protected KafkaContainer container() {
    return container;
  }

  @Override
  public Stream<Port> ports() {
    return Stream.of(Port.create(DEFAULT_PORT));
  }

  @Override
  public SocketType socketType() {
    return SocketType.TCP;
  }

  @Override
  public ServiceId id() {
    return KafkaServiceId.INSTANCE;
  }

  @Override
  public String name() {
    return "Kafka Container";
  }
}
