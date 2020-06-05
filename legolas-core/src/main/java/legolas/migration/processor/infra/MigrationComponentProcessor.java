package legolas.migration.processor.infra;

import com.google.common.collect.Sets;
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
        return Sets.newHashSet(MigrationComponent.class.getName());
    }
}
