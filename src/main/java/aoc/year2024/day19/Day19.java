package aoc.year2024.day19;

import aoc.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day19_input.txt");

        List<String> availableTowelPatterns = Arrays.asList(inputList.getFirst().split(", "));

        List<String> desiredDesigns = inputList.subList(2, inputList.size());

        int numberOfPossibleDesigns = 0;
        for (String design : desiredDesigns) {
            Map<String, Boolean> memoize = new HashMap<>();
            boolean possible = checkIfDesignIsPossible(design, availableTowelPatterns, memoize);
            if (possible) {
                System.out.println("Design '" + design + "'" + " is possible.");
                numberOfPossibleDesigns++;
            } else {
                System.out.println("Design '" + design + "'" + " is NOT possible.");
            }
        }
        System.out.println("Part 1: " + numberOfPossibleDesigns); // 400
    }

    /* This can be speed up even further but using the memoize map is good enough */
    private static boolean checkIfDesignIsPossible(String design, List<String> availableTowelPatterns, Map<String, Boolean> memoize) {
        for (String pattern: availableTowelPatterns) {
            Boolean memory = memoize.get(design.length() + "|" + pattern);
            if (memory == null) {
                if (design.length() == pattern.length() && design.equals(pattern)) {
                    memoize.put(design.length() + "|" + pattern, true);
                    return true;
                }

                if (design.startsWith(pattern)) {
                    String subDesign = design.substring(pattern.length());
                    boolean subIsDesignIsPossible = checkIfDesignIsPossible(subDesign, availableTowelPatterns, memoize);
                    if (subIsDesignIsPossible) {
                        return true;
                    }
                }
                memoize.put(design.length() + "|" + pattern, false);
            } else {
                return Boolean.TRUE.equals(memory);
            }

        }
        return false;
    }
}
