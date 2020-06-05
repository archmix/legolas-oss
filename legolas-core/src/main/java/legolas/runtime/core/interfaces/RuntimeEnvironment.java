package legolas.runtime.core.interfaces;

import legolas.async.api.interfaces.Promise;

import java.util.concurrent.ExecutorService;

public enum RuntimeEnvironment {
    LOCAL {
        @Override
        protected LifecycleEnvironment lifecycleEnvironment() {
            return new LocalEnvironment();
        }
    },
    SERVER {
        @Override
        protected LifecycleEnvironment lifecycleEnvironment() {
            return new ServerEnvironment();
        }
    },
    TEST {
        @Override
        protected LifecycleEnvironment lifecycleEnvironment() {
            return new TestEnvironment();
        }
    };

    public Promise<RunningEnvironment> start(ExecutorService executorService) {
        Promise<RunningEnvironment> promise = Promise.create();

        executorService.execute(() ->{
            this.lifecycleEnvironment().start(this, promise);
        });

        return promise;
    }

    protected abstract LifecycleEnvironment lifecycleEnvironment();
}
