package legolas.postgre.interfaces;

import legolas.runtime.core.interfaces.ServiceId;

public enum PostgreSQLServiceId implements ServiceId {
  INSTANCE;

  @Override
  public String value() {
    return "PostgreSQL.Id";
  }
}
