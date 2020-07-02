package legolas.mysql.interfaces;

import legolas.runtime.core.interfaces.ServiceId;

public enum MySQLServiceId implements ServiceId {
  INSTANCE;

  @Override
  public String value() {
    return "MySQL.Id";
  }
}
