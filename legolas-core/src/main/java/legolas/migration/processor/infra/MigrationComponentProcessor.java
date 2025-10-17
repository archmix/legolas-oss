package legolas.migration.processor.infra;

import compozitor.processor.core.interfaces.Processor;
import compozitor.processor.core.interfaces.ServiceProcessor;
import legolas.migration.api.interfaces.Migration;
import legolas.migration.api.interfaces.MigrationComponent;

import java.util.Arrays;
import java.util.Set;

@Processor
public class MigrationComponentProcessor extends ServiceProcessor {

  @Override
  protected Iterable<Class<?>> serviceClasses() {
    return Arrays.asList(Migration.class);
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(MigrationComponent.class.getName());
  }
}
