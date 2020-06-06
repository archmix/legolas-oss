package legolas.sql.interfaces;

import legolas.config.api.interfaces.Configuration;
import legolas.migration.core.interfaces.MigramiMigration;
import legolas.runtime.core.interfaces.RunningEnvironment;
import legolas.runtime.core.interfaces.RunningInstance;
import legolas.runtime.core.interfaces.ServiceId;
import migrami.core.interfaces.Migrami;
import migrami.core.interfaces.MigramiCategory;
import migrami.sql.interfaces.MigramiSQLEngineBuilder;

import java.util.Optional;

public abstract class SQLMigration extends MigramiMigration {

  @Override
  protected final Migrami migrami(RunningEnvironment runningEnvironment, MigramiCategory category) {
    Configuration configuration = runningEnvironment.configuration();

    Optional<RunningInstance> runningInstance = runningEnvironment.get(this.targetService());
    if (runningInstance.isPresent()) {
      configuration = runningInstance.get().configuration();
    }

    DatabaseConfiguration databaseConfiguration = this.toDatabaseConfiguration(configuration);
    String url = databaseConfiguration.getUrl();
    String user = databaseConfiguration.getUsername();
    String password = databaseConfiguration.getPassword();

    return MigramiSQLEngineBuilder.create().withDatasource(url, user, password)
      .withClasspathScriptLoader("", category)
      .withTableSnapshotRepository()
      .build();
  }

  protected abstract ServiceId targetService();

  protected abstract DatabaseConfiguration toDatabaseConfiguration(Configuration configuration);
}
