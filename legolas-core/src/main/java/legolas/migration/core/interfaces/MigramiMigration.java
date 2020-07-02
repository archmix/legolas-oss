package legolas.migration.core.interfaces;

import legolas.migration.api.interfaces.Migration;
import legolas.runtime.core.interfaces.RunningEnvironment;
import migrami.core.interfaces.Migrami;
import migrami.core.interfaces.MigramiCategory;
import toolbox.resources.interfaces.ResourcePath;

public abstract class MigramiMigration implements Migration {
  private static final String SERVICES_PATH = "services";

  @Override
  public final void migrate(RunningEnvironment runningEnvironment) {
    String path = ResourcePath.create(SERVICES_PATH, this.migrationPath()).path();
    MigramiCategory category = MigramiCategory.MigramiCategoryAdapter.create("oracle", path);

    Migrami migrami = migrami(runningEnvironment, category);
    migrami.migrate();
  }

  protected abstract String migrationPath();

  protected abstract Migrami migrami(RunningEnvironment runningEnvironment, MigramiCategory category);
}
