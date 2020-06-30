package legolas.mysql.infra;

import legolas.config.api.interfaces.Configuration;
import legolas.migration.api.interfaces.MigrationComponent;
import legolas.migration.api.interfaces.MigrationId;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatabaseConfiguration;
import legolas.sql.interfaces.SQLMigration;
import legolas.mysql.interfaces.MySQLEntry;
import legolas.mysql.interfaces.MySQLServiceId;

@MigrationComponent
public class MySQLMigration extends SQLMigration {

  @Override
  protected DatabaseConfiguration toDatabaseConfiguration(Configuration configuration) {
    String url = configuration.getString(MySQLEntry.URL).get();
    String user = configuration.getString(MySQLEntry.USERNAME).get();
    String password = configuration.getString(MySQLEntry.PASSWORD).get();

    return DatabaseConfiguration.create(url, user, password);
  }

  @Override
  protected ServiceId targetService() {
    return MySQLServiceId.INSTANCE;
  }

  @Override
  protected String migrationPath() {
    return "mysql/migration";
  }

  @Override
  public MigrationId id() {
    return () -> "migration.mysql";
  }
}
