package src.main.java.aoc.y2023.utils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utils {

    public static List<String> readAllLinesFromStream(URI filename) {
        try {
            return Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
