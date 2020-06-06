package legolas.common.interfaces;

public class ResourcePath {
  private final StringBuilder path;

  private ResourcePath() {
    this.path = new StringBuilder();
  }

  public static ResourcePath create() {
    return new ResourcePath();
  }

  public static ResourcePath create(String path) {
    return new ResourcePath().append(path);
  }

  public ResourcePath append(String path) {
    this.path.append(path).append("/");
    return this;
  }

  public String path() {
    return this.path.toString();
  }

  public String toString() {
    return this.path();
  }
}
