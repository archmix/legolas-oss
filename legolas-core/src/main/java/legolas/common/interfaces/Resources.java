package legolas.common.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toolbox.resources.interfaces.ResourceStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class Resources {
  private final Logger logger = LoggerFactory.getLogger(Resources.class);
  private final ResourceStream resourceStream;

  public Resources(ClassLoader classLoader) {
    this.resourceStream = ResourceStream.create(classLoader);
  }

  public static Resources create() {
    return create(Thread.currentThread().getContextClassLoader());
  }

  public static Resources create(ClassLoader classLoader) {
    return new Resources(classLoader);
  }

  public Optional<String> read(Path path) {
    String content = this.openFileAsString(path);
    if (content == null) {
      content = this.openResourceAsString(path);
    }

    return Optional.ofNullable(content);
  }

  private String openFileAsString(Path path) {
    File file = new File(path.toUri());
    if (!file.exists()) {
      logger.info("File does not exists on path {}. Reading from classpath.", path.toString());
      return null;
    }

    try {
      byte[] available = Files.readAllBytes(path);
      return resourceStream.toString(available);
    } catch (IOException e) {
      log(path, e);
      return null;
    }
  }

  private String openResourceAsString(Path path) {
    return resourceStream.read(path).orElse(null);
  }

  private void log(Path path, IOException e) {
    logger.warn("It was not possible to read file on path {}", path.toString());
    logger.warn("Read error ", e);
  }
}
