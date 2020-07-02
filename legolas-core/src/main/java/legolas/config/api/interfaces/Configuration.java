package legolas.config.api.interfaces;

import toolbox.data.interfaces.HashObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configuration extends HashObject<Configuration, Entry> {
  private Configuration(Map<String, Object> values) {
    super(values);
  }

  public static Configuration create(Map<String, Object> configMap) {
    return new Configuration(configMap);
  }

  public static Configuration create(Properties properties) {
    Map<String, Object> configMap = new HashMap<>();
    properties.entrySet().forEach(entry -> {
      configMap.put(entry.getKey().toString(), entry.getValue());
    });
    return create(configMap);
  }

  @Override
  protected String toString(Entry entry) {
    return entry.value();
  }

  public static Configuration create() {
    return new Configuration(new HashMap<>());
  }
}
