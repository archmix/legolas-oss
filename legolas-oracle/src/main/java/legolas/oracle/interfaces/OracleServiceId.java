package legolas.oracle.interfaces;

import legolas.runtime.core.interfaces.ServiceId;

public enum OracleServiceId implements ServiceId {
    INSTANCE;

    @Override
    public String value() {
        return "SQLServer.Id";
    }
}
