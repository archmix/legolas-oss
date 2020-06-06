package legolas.sql.interfaces;

import java.util.HashMap;
import java.util.Map;

public class DataObject {
  private Map<String, Object> data;

  public DataObject() {
    this.data = new HashMap<>();
  }

  public <T> T get(String property) {
    return (T) this.data.get(property);
  }

  public void set(String property, Object data) {
    this.data.put(property.toLowerCase(), data);
  }

  public Integer size() {
    return this.data.size();
  }

  public Map<String, Object> toMap() {
    return new HashMap<>(data);
  }

  public String toString() {
    return this.data.toString();
  }
}