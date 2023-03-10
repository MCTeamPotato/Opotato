package com.teampotato.opotato.util.nec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Java11 {
    public static String readString(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

    public static void writeString(Path path, String string) throws IOException {
        Files.write(path, string.getBytes(StandardCharsets.UTF_8));
    }
}