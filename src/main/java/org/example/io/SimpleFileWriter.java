package org.example.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SimpleFileWriter {
    public static void write(String path, String content) throws IOException {
        Files.writeString(Path.of(path), content);
    }
}
