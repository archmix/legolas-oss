package legolas.migration.api.interfaces;

import legolas.runtime.core.interfaces.RunningEnvironment;

import java.util.Optional;

public interface Migration {
  void migrate(RunningEnvironment runningEnvironment);

  MigrationId id();

  default Optional<MigrationId> dependsOn() {
    return Optional.empty();
  }
}
