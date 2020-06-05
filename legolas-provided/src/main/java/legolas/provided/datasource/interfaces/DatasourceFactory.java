package legolas.provided.datasource.interfaces;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatasourceFactory {
    public static DataSource toDataSource(String jdbcUrl, String driverClass, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName(driverClass);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}
