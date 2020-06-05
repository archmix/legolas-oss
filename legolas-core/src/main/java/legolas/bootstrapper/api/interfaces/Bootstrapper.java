package legolas.bootstrapper.api.interfaces;

import legolas.config.api.interfaces.Configuration;
import legolas.runtime.core.interfaces.LifecycleService;

public interface Bootstrapper<Reference> extends LifecycleService {
    Reference bootstrap(Configuration configuration);


}
