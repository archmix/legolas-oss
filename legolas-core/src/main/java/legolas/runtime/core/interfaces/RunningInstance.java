package legolas.runtime.core.interfaces;

import legolas.config.api.interfaces.Configuration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RunningInstance<Reference> {
  private final ServiceId id;

  private final Configuration configuration;

  private final Optional<Reference> reference;

  public static <Reference> RunningInstance create(ServiceId id, Configuration configuration, Reference reference) {
    return new RunningInstance(id, configuration, Optional.of(reference));
  }

  public static RunningInstance<?> create(ServiceId id, Configuration configuration) {
    return new RunningInstance<>(id, configuration, Optional.empty());
  }

  public ServiceId id() {
    return this.id;
  }

  public Configuration configuration() {
    return this.configuration;
  }

  public Optional<Reference> reference() {
    return this.reference;
  }
}
