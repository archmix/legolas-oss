package legolas.oracle.infra;

import legolas.config.api.interfaces.Configuration;
import legolas.migration.api.interfaces.MigrationComponent;
import legolas.migration.api.interfaces.MigrationId;
import legolas.oracle.interfaces.OracleEntry;
import legolas.oracle.interfaces.OracleServiceId;
import legolas.runtime.core.interfaces.ServiceId;
import legolas.sql.interfaces.DatabaseConfiguration;
import legolas.sql.interfaces.SQLMigration;

@MigrationComponent
public class OracleMigration extends SQLMigration {

  @Override
  protected DatabaseConfiguration toDatabaseConfiguration(Configuration configuration) {
    String url = configuration.getString(OracleEntry.URL).get();
    String user = configuration.getString(OracleEntry.USERNAME).get();
    String password = configuration.getString(OracleEntry.PASSWORD).get();

    return DatabaseConfiguration.create(url, user, password);
  }

  @Override
  protected ServiceId targetService() {
    return OracleServiceId.INSTANCE;
  }

  @Override
  protected String migrationPath() {
    return "oracle/migration";
  }

  @Override
  public MigrationId id() {
    return () -> "migration.oracle";
  }
}
