package legolas.postgre.infra;

import legolas.config.api.interfaces.Configuration;
import legolas.migration.api.interfaces.MigrationComponent;
import legolas.migration.api.interfaces.MigrationId;
import legolas.postgre.interfaces.PostgreSQLEntry;
import legolas.postgre.interfaces.PostgreSQLServiceId;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatabaseConfiguration;
import legolas.sql.interfaces.SQLMigration;

@MigrationComponent
public class PostgreSQLMigration extends SQLMigration {

  @Override
  protected DatabaseConfiguration toDatabaseConfiguration(Configuration configuration) {
    String url = configuration.getString(PostgreSQLEntry.URL).get();
    String user = configuration.getString(PostgreSQLEntry.USERNAME).get();
    String password = configuration.getString(PostgreSQLEntry.PASSWORD).get();

    return DatabaseConfiguration.create(url, user, password);
  }

  @Override
  protected ServiceId targetService() {
    return PostgreSQLServiceId.INSTANCE;
  }

  @Override
  protected String migrationPath() {
    return "postgre/migration";
  }

  @Override
  public MigrationId id() {
    return () -> "migration.postgre";
  }
}
