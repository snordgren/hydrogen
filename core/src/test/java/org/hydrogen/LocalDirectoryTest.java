package org.hydrogen;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocalDirectoryTest {
    @Test
    public void testLocalFile() {
        String expectedContents = "Lorem ipsum dolor sit amet.";
        LocalDirectory localDirectory = new LocalDirectory("src/test/resources/");
        assertTrue(localDirectory.isPathValid("TestFile.txt"));
        assertEquals(expectedContents, new String(localDirectory.load("TestFile.txt"),
                Charset.forName("UTF-8")));
    }
}
