package legolas.mysql.infra;

import legolas.config.api.interfaces.Configuration;
import legolas.migration.api.interfaces.MigrationComponent;
import legolas.migration.api.interfaces.MigrationId;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatabaseConfiguration;
import legolas.sql.interfaces.SQLMigration;
import legolas.mysql.interfaces.SQLServerEntry;
import legolas.mysql.interfaces.SQLServerServiceId;

@MigrationComponent
public class SQLServerMigration extends SQLMigration {

  @Override
  protected DatabaseConfiguration toDatabaseConfiguration(Configuration configuration) {
    String url = configuration.getString(SQLServerEntry.URL).get();
    String user = configuration.getString(SQLServerEntry.USERNAME).get();
    String password = configuration.getString(SQLServerEntry.PASSWORD).get();

    return DatabaseConfiguration.create(url, user, password);
  }

  @Override
  protected ServiceId targetService() {
    return SQLServerServiceId.INSTANCE;
  }

  @Override
  protected String migrationPath() {
    return "sqlserver/migration";
  }

  @Override
  public MigrationId id() {
    return () -> "migration.sqlserver";
  }
}
