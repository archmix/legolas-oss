package legolas.runtime.core.interfaces;

import legolas.migration.api.interfaces.Migration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class MigrationRunner {
    private static final Logger logger = LoggerFactory.getLogger(MigrationRunner.class);

    public void run(RunningEnvironment environment) {

        Map<String, MigrationTask> tasks = new HashMap<>();

        ServiceLoader.load(Migration.class).forEach(service -> {
            tasks.put(service.id().value(), MigrationTask.of(service));
        });

        if (tasks.isEmpty()) {
            return;
        }

        List<MigrationTask> dependentTasks = new ArrayList<>();
        tasks.values().forEach(task -> {
            task.dependsOn().ifPresent(id -> {
                tasks.get(id.value()).add(task);
                dependentTasks.add(task);
            });
        });

        dependentTasks.forEach(task -> {
            tasks.remove(task.id().value());
        });

        tasks.values().forEach(task -> {
            logger.info("Running migration {}", task.id().value());
            task.run(environment);
        });
    }
}
