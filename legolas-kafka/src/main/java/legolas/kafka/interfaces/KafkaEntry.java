package legolas.kafka.interfaces;

import legolas.config.api.interfaces.Entry;

public enum KafkaEntry implements Entry {

  KAFKA_BROKER("broker"),
  ;

  private final String value;

  KafkaEntry(final String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return this.value;
  }
}
