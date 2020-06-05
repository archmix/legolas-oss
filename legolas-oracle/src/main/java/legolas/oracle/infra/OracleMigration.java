package legolas.oracle.infra;

import legolas.config.api.interfaces.Configuration;
import legolas.migration.api.interfaces.MigrationComponent;
import legolas.migration.api.interfaces.MigrationId;
import legolas.migration.core.interfaces.MigramiMigration;
import legolas.oracle.interfaces.OracleEntry;
import legolas.oracle.interfaces.OracleServiceId;
import legolas.runtime.core.interfaces.RunningEnvironment;
import migrami.core.interfaces.Migrami;
import migrami.core.interfaces.MigramiCategory;
import migrami.sql.interfaces.MigramiSQLEngineBuilder;

@MigrationComponent
public class OracleMigration extends MigramiMigration {

    @Override
    protected Migrami migrami(RunningEnvironment runningEnvironment, MigramiCategory category) {
        Configuration configuration = runningEnvironment.get(OracleServiceId.INSTANCE).get().configuration();
        String url = configuration.getString(OracleEntry.URL).get();
        String user = configuration.getString(OracleEntry.USERNAME).get();
        String password = configuration.getString(OracleEntry.PASSWORD).get();

        return MigramiSQLEngineBuilder.create().withDatasource(url, user, password)
            .withClasspathScriptLoader("", category)
            .withTableSnapshotRepository()
            .build();
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
