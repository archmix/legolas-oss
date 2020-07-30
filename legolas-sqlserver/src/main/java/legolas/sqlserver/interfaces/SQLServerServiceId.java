package legolas.sqlserver.interfaces;

import legolas.runtime.core.interfaces.ServiceId;

public enum SQLServerServiceId implements ServiceId {
  INSTANCE;

  @Override
  public String value() {
    return "SQLServer.Id";
  }
}
