package org.hydrogen;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClasspathDirectoryTest {
    private final String expectedContents = "Lorem ipsum dolor sit amet.";

    private String loadString(ClasspathDirectory directory, String path) {
        return new String(directory.load(path),
                Charset.forName("UTF-8"));
    }

    @Test
    public void testFlatFile() {
        ClasspathDirectory directory = new ClasspathDirectory("");
        assertTrue(directory.isPathValid("TestFile.txt"));
        assertEquals(expectedContents, loadString(directory, "TestFile.txt"));
    }

    @Test
    public void testNestedFile() {
        ClasspathDirectory directory = new ClasspathDirectory("dir");
        assertTrue(directory.isPathValid("TestFile.txt"));
        assertEquals(expectedContents, loadString(directory, "TestFile.txt"));
    }
}
