package resourcesTest;

import legolas.common.interfaces.Resources;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;

public class ResourcesTest {
    @Test
    public void givenFileWhenReadThenReadAsFile() {
        String content = Resources.create().read(Paths.get("src/test/file.txt")).get();
        Assert.assertEquals("file.txt read as file", content);
    }

    @Test
    public void givenResourceWhenReadThenReadAsResource() {
        String content = Resources.create().read(Paths.get("resource.txt")).get();
        Assert.assertEquals("resource.txt read as resource", content);
    }
}
