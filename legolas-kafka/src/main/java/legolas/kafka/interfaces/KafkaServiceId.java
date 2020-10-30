package legolas.kafka.interfaces;

import legolas.runtime.core.interfaces.ServiceId;

public enum KafkaServiceId implements ServiceId {
  INSTANCE;

  @Override
  public String value() {
    return "Kafka.Id";
  }
}
