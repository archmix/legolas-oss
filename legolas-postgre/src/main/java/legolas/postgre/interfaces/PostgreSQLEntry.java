package legolas.postgre.interfaces;

import legolas.config.api.interfaces.Entry;

public enum PostgreSQLEntry implements Entry {
    HOST("postgre.host"),
    PORT("postgre.port"),
    URL("postgre.url"),
    DRIVER("postgre.driver"),
    USERNAME("postgre.username"),
    PASSWORD("postgre.password");

    private final String value;

    PostgreSQLEntry(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return this.value;
    }
}
