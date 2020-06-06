package legolas.net.core.interfaces;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
@EqualsAndHashCode
public class Port {
  private static final int ANY_PORT_VALUE = -1;
  private final Integer value;

  public static Port anyPort() {
    return Port.create(ANY_PORT_VALUE);
  }

  public Integer value() {
    return value;
  }

  public Boolean any() {
    return this.value == ANY_PORT_VALUE;
  }

  public Port next() {
    return Port.create(this.value + 1);
  }

  public String toBinding() {
    return String.format("%d:%d", this.value, this.value);
  }

  @Override
  public String toString() {
    return this.value.toString();
  }
}
