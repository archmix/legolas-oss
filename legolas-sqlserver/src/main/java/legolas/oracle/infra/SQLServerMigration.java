package legolas.oracle.infra;

import legolas.config.api.interfaces.Configuration;
import legolas.migration.api.interfaces.MigrationComponent;
import legolas.migration.api.interfaces.MigrationId;
import legolas.migration.core.interfaces.MigramiMigration;
import legolas.oracle.interfaces.SQLServerEntry;
import legolas.oracle.interfaces.SQLServerServiceId;
import legolas.runtime.core.interfaces.RunningEnvironment;
import migrami.core.interfaces.Migrami;
import migrami.core.interfaces.MigramiCategory;
import migrami.sql.interfaces.MigramiSQLEngineBuilder;

@MigrationComponent
public class SQLServerMigration extends MigramiMigration {

    @Override
    protected Migrami migrami(RunningEnvironment runningEnvironment, MigramiCategory category) {
        Configuration configuration = runningEnvironment.get(SQLServerServiceId.INSTANCE).get().configuration();
        String url = configuration.getString(SQLServerEntry.URL).get();
        String user = configuration.getString(SQLServerEntry.USERNAME).get();
        String password = configuration.getString(SQLServerEntry.PASSWORD).get();

        return MigramiSQLEngineBuilder.create().withDatasource(url, user, password)
            .withClasspathScriptLoader("", category)
            .withTableSnapshotRepository()
            .build();
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
