package legolas.postgre.infra;

import legolas.config.api.interfaces.Configuration;
import legolas.migration.api.interfaces.MigrationComponent;
import legolas.migration.api.interfaces.MigrationId;
import legolas.migration.core.interfaces.MigramiMigration;
import legolas.postgre.interfaces.PostgreSQLEntry;
import legolas.postgre.interfaces.PostgreSQLServiceId;
import legolas.runtime.core.interfaces.RunningEnvironment;
import migrami.core.interfaces.Migrami;
import migrami.core.interfaces.MigramiCategory;
import migrami.sql.interfaces.MigramiSQLEngineBuilder;

@MigrationComponent
public class PostgreSQLMigration extends MigramiMigration {

    @Override
    protected Migrami migrami(RunningEnvironment runningEnvironment, MigramiCategory category) {
        Configuration configuration = runningEnvironment.get(PostgreSQLServiceId.INSTANCE).get().configuration();
        String url = configuration.getString(PostgreSQLEntry.URL).get();
        String user = configuration.getString(PostgreSQLEntry.USERNAME).get();
        String password = configuration.getString(PostgreSQLEntry.PASSWORD).get();

        return MigramiSQLEngineBuilder.create().withDatasource(url, user, password)
            .withClasspathScriptLoader("", category)
            .withTableSnapshotRepository()
            .build();
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
