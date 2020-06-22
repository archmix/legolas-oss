package legolas.mysql.interfaces;

import legolas.config.api.interfaces.Entry;

public enum MySQLEntry implements Entry {
  HOST("mysql.host"),
  PORT("mysql.port"),
  URL("mysql.url"),
  DRIVER("mysql.driver"),
  USERNAME("mysql.username"),
  PASSWORD("mysql.password");

  private final String value;

  MySQLEntry(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return this.value;
  }
}
