package legolas.docker.interfaces;

import java.util.regex.Pattern;

public class Username {
  private static final Pattern usernamePattern = Pattern.compile("[^A-Za-z0-9]");
  private static final Username valueOf = new Username();
  private final String value;

  public static Username valueOf() {
    return valueOf;
  }

  public String value() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  private Username() {
    this.value = this.username();
  }

  private String username() {
    String username = System.getProperty("user.name");
    return usernamePattern.matcher(username).replaceAll("_").trim();
  }
}
