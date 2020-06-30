package legolas.mysql.interfaces;

import legolas.config.api.interfaces.Entry;

public enum SQLServerEntry implements Entry {
  HOST("sqlserver.host"),
  PORT("sqlserver.port"),
  URL("sqlserver.url"),
  DRIVER("sqlserver.driver"),
  USERNAME("sqlserver.username"),
  PASSWORD("sqlserver.password");

  private final String value;

  SQLServerEntry(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return this.value;
  }
}
