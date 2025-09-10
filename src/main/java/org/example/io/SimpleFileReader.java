package org.example.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SimpleFileReader {
    public static String read(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
