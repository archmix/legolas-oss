package legolas.starter.api.interfaces;

import legolas.config.api.interfaces.Configuration;
import legolas.runtime.core.interfaces.LifecycleService;

public interface Starter extends LifecycleService {
    void start();

    Configuration configuration();
}
