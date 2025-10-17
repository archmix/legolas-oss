package legolas.starter.processor.infra;

import compozitor.processor.core.interfaces.Processor;
import compozitor.processor.core.interfaces.ServiceProcessor;
import legolas.starter.api.interfaces.Starter;
import legolas.starter.api.interfaces.StarterComponent;

import java.util.Arrays;
import java.util.Set;

@Processor
public class StarterComponentProcessor extends ServiceProcessor {

  public StarterComponentProcessor() {
    this.traverseAncestors();
  }

  @Override
  protected Iterable<Class<?>> serviceClasses() {
    return Arrays.asList(Starter.class);
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(StarterComponent.class.getName());
  }
}
