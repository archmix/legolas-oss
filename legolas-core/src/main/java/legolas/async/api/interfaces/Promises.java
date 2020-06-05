package legolas.async.api.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Promises {
    private final List<CompletableFuture<?>> promises;

    private Promises() {
        this.promises = new ArrayList<>();
    }

    public static Promises create() {
        return new Promises();
    }

    public <T> Promise<T> add(){
        return this.add(() -> null);
    }

    public <T> Promise<T> add(Supplier<T> supplier){
        Promise<T> promise = Promise.create(supplier);
        this.promises.add(promise.asFuture());
        return promise;
    }

    public Promise<Void> all(){
        CompletableFuture<Void> future = CompletableFuture.allOf(this.promises.toArray(new CompletableFuture[this.promises.size()]));
        return new Promise<>(future);
    }

    public Promise<Object> any(){
        CompletableFuture<Object> future = CompletableFuture.anyOf(this.promises.toArray(new CompletableFuture[this.promises.size()]));
        return new Promise(future);
    }
}
