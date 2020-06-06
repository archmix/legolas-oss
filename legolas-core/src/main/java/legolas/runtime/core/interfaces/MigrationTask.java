package legolas.runtime.core.interfaces;

import legolas.migration.api.interfaces.Migration;
import legolas.migration.api.interfaces.MigrationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class MigrationTask {
  private static final Logger logger = LoggerFactory.getLogger(MigrationTask.class);
  private final Migration task;
  private final List<MigrationTask> dependents;

  MigrationTask(Migration task) {
    this.task = task;
    this.dependents = new ArrayList<>();
  }

  public static MigrationTask of(Migration task) {
    return new MigrationTask(task);
  }

  public MigrationId id() {
    return this.task.id();
  }

  public MigrationTask add(MigrationTask task) {
    this.dependents.add(task);
    return task;
  }

  public Optional<MigrationId> dependsOn() {
    return this.task.dependsOn();
  }

  public void run(RunningEnvironment environment) {
    this.task.migrate(environment);
    this.dependents.forEach(task -> task.run(environment));
  }
}
