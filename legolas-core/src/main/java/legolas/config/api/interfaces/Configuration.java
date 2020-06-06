package legolas.config.api.interfaces;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@RequiredArgsConstructor(staticName = "create")
public class Configuration {
  private final Map<String, Object> values;

  public static Configuration create(Properties properties){
    Map<String, Object> configMap = new HashMap<>();
    properties.entrySet().forEach(entry ->{
      configMap.put(entry.getKey().toString(), entry.getValue());
    });
    return create(configMap);
  }

  public static Configuration create() {
    return new Configuration(new HashMap<>());
  }

  public Configuration set(Entry entry, Object value) {
    this.values.put(entry.value(), value);
    return this;
  }

  public Optional<Object> getObject(final Entry entry) {
    return Optional.ofNullable(this.values.get(entry.value()));
  }

  public Optional<Boolean> getBoolean(final Entry entry) {
    return Optional.ofNullable(this.<Boolean>get(entry));
  }

  public Optional<Number> getNumber(final Entry entry) {
    final Object value = this.values.get(entry.value());

    if (value == null) {
      return Optional.empty();
    }

    if (value instanceof String) {
      return Optional.of(Integer.valueOf((String) value));
    }

    return Optional.of((Number) value);
  }

  public Optional<String> getString(final Entry entry) {
    final Object value = this.values.get(entry.value());

    if (value == null) {
      return Optional.empty();
    }

    if (value instanceof Number) {
      String toStringValue = String.valueOf(((Number) value).intValue());
      return Optional.of(toStringValue);
    }

    return Optional.of(value.toString());
  }

  @SuppressWarnings("unchecked")
  private <T> T get(Entry entry) {
    return (T) this.values.get(entry.value());
  }
}
