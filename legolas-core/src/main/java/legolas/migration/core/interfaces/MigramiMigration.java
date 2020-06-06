package legolas.migration.core.interfaces;

import legolas.common.interfaces.ResourcePath;
import legolas.migration.api.interfaces.Migration;
import legolas.runtime.core.interfaces.RunningEnvironment;
import migrami.core.interfaces.Migrami;
import migrami.core.interfaces.MigramiCategory;

public abstract class MigramiMigration implements Migration {
  private static final String SERVICES_PATH = "services";

  @Override
  public final void migrate(RunningEnvironment runningEnvironment) {
    ResourcePath servicesPath = ResourcePath.create(SERVICES_PATH);

    String path = servicesPath.append(this.migrationPath()).path();
    MigramiCategory category = MigramiCategory.MigramiCategoryAdapter.create("oracle", path);

    Migrami migrami = migrami(runningEnvironment, category);
    migrami.migrate();
  }

  protected abstract String migrationPath();

  protected abstract Migrami migrami(RunningEnvironment runningEnvironment, MigramiCategory category);
}
