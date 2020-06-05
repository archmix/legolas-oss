package legolas.bootstrapper.processor.infra;

import com.google.common.collect.Sets;
import compozitor.processor.core.interfaces.Processor;
import compozitor.processor.core.interfaces.ServiceProcessor;
import legolas.bootstrapper.api.interfaces.Bootstrapper;
import legolas.bootstrapper.api.interfaces.BootstrapperComponent;
import legolas.bootstrapper.api.interfaces.NetworkBootstrapper;

import java.util.Arrays;
import java.util.Set;

@Processor
public class BootstrapperComponentProcessor extends ServiceProcessor {

    @Override
    protected Iterable<Class<?>> serviceClasses() {
        return Arrays.asList(NetworkBootstrapper.class, Bootstrapper.class);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Sets.newHashSet(BootstrapperComponent.class.getName());
    }
}
