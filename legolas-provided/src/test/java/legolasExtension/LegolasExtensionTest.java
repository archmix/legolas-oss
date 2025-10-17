package legolasExtension;

import legolas.provided.infra.LegolasExtension;
import legolas.runtime.core.interfaces.RunningEnvironment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LegolasExtension.class)
public class LegolasExtensionTest {

  @Test
  public void test(RunningEnvironment environment) {
    assert environment != null;
  }
}
