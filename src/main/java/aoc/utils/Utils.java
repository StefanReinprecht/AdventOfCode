package aoc.utils;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static List<String> readAllLinesFromStream(URI filename) {
        try {
            return Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readAllLinesFromResourceAsStream(int year, String filename) {
        String path = year + File.separator + filename;
        InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(path);

        // Check if the resource is found
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: ");
        }

        // Use BufferedReader to read the content efficiently
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Use Java 8+ Stream API to collect lines into a single string
            return reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
