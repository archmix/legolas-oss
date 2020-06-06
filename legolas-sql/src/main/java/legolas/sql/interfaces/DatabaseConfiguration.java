package legolas.sql.interfaces;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
@Getter
public class DatabaseConfiguration {
  private final String url;

  private final String username;

  private final String password;

  public static DatabaseConfiguration h2Fallback(TargetDatabase targetDatabase) {
    String mode = targetDatabase.h2Mode();
    String url = String.format("jdbc:h2:mem:db;MODE=%s", mode);
    return DatabaseConfiguration.create(url, "sa", "sa");
  }
}