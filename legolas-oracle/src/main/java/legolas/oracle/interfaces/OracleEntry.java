package legolas.oracle.interfaces;

import legolas.config.api.interfaces.Entry;

public enum OracleEntry implements Entry {
  HOST("oracle.host"),
  PORT("oracle.port"),
  URL("oracle.url"),
  DRIVER("oracle.driver"),
  USERNAME("oracle.username"),
  PASSWORD("oracle.password"),
  SCHEMA("oracle.schema"),
  SID("oracle.sid");

  private final String value;

  OracleEntry(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return this.value;
  }
}
