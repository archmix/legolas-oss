package legolas.async.api.interfaces;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Promise<T> {
  private final CompletableFuture<T> future;

  private Throwable cause;

  private Boolean succeded = Boolean.FALSE;

  private Boolean failed = Boolean.FALSE;

  private T result;

  private Promise(Supplier<T> supplier) {
    this(CompletableFuture.supplyAsync(supplier));
  }

  Promise(CompletableFuture<T> future) {
    this.future = future;

    BiConsumer<T, Throwable> completionConsumer = (result, cause) -> {
      if (cause != null) {
        this.failed = Boolean.TRUE;
        this.cause = cause;
      } else {
        this.succeded = Boolean.TRUE;
        this.result = result;
      }
    };

    this.future.whenComplete(completionConsumer).whenCompleteAsync(completionConsumer);
  }

  public static <T> Promise<T> create() {
    return new Promise<>(new CompletableFuture<>());
  }

  public static <T> Promise<T> create(Supplier<T> supplier) {
    return new Promise<>(supplier);
  }

  public static <T> Promise<T> failed(Throwable throwable) {
    CompletableFuture future = new CompletableFuture();
    future.completeExceptionally(throwable);
    return new Promise<T>(future);
  }

  public static Promise<Void> succeeded() {
    return succeeded(null);
  }

  public static <T> Promise<T> succeeded(T value) {
    CompletableFuture future = new CompletableFuture();
    future.complete(value);
    return new Promise<T>(future);
  }

  public void complete() {
    this.future.complete(null);
  }

  public void complete(T value) {
    this.future.complete(value);
  }

  public void fail(Throwable throwable) {
    this.future.completeExceptionally(throwable);
  }

  public T get() {
    try {
      return this.future.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Boolean succeded() {
    return this.succeded;
  }

  public Boolean failed() {
    return failed;
  }

  public Throwable cause() {
    return cause;
  }

  public T result() {
    return result;
  }

  CompletableFuture<T> asFuture() {
    return future;
  }

  static class EmptyConsumer<T> implements Consumer {
    @Override
    public void accept(Object o) {

    }
  }
}
