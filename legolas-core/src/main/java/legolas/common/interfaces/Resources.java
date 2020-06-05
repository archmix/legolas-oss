package legolas.common.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class Resources {
    private final Logger logger = LoggerFactory.getLogger(Resources.class);
    private final ClassLoader classLoader;
    private final Charset charset;

    public Resources(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.charset = StandardCharsets.UTF_8;
    }

    public static Resources create() {
        return create(Thread.currentThread().getContextClassLoader());
    }

    public static Resources create(ClassLoader classLoader) {
        return new Resources(classLoader);
    }

    public Optional<String> read(Path path) {
        String content = this.openResourceAsString(path);
        if (content == null) {
            content = this.openFileAsString(path);
        }

        return Optional.ofNullable(content);
    }

    private String openFileAsString(Path path) {
        File file = new File(path.toUri());
        if (!file.exists()) {
            logger.info("File does not exists on path {}", path.toString());
            return null;
        }

        try {
            byte[] available = Files.readAllBytes(path);
            return this.toString(available);
        } catch (IOException e) {
            log(path, e);
            return null;
        }
    }

    private String openResourceAsString(Path path) {
        try (InputStream inputStream = this.openResourceStream(path)) {
            if (inputStream == null) {
                return null;
            }

            byte[] available = new byte[inputStream.available()];
            inputStream.read(available);
            return new String(available);
        } catch (IOException e) {
            log(path, e);
            return null;
        }
    }

    private void log(Path path, IOException e) {
        logger.warn("It was not possible to read file on path {}", path.toString());
        logger.warn("Read error ", e);
    }

    private String toString(byte[] bytes) {
        return new String(bytes, this.charset);
    }

    private InputStream openResourceStream(Path path) {
        return this.classLoader.getResourceAsStream(path.toString());
    }

}
