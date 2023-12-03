package aoc.year2023.day3;

import aoc.utils.Utils;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day3 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input3.txt");

        List<List<String>> parts = new ArrayList<>();
        for (String line : inputList) {
            String[] p = line.split("");
            parts.add(List.of(p));
        }

        List<Integer> foundNumbers = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            List<String> row = parts.get(i);

            List<String> partNumbers = new ArrayList<>();
            boolean anyNumberPartBorders = false;
            boolean endOfNumber = true;
            for (int j = 0; j < row.size(); j++) {
                String x = row.get(j);

                if (isNumeric(x)) {
                    endOfNumber = false;
                    // check if surrounded by special character
                    if (specialCharacterIsBordering(parts, i, j)) {
                        anyNumberPartBorders = true;
                    }

                    partNumbers.add(x);
                } else {
                    if (!endOfNumber) {
                        if (!partNumbers.isEmpty() && anyNumberPartBorders) {
                            foundNumbers.add(Integer.parseInt(String.join("", partNumbers)));
                        }
                        partNumbers.clear();
                        endOfNumber = true;
                        anyNumberPartBorders = false;
                    }
                }
            }

            if (!partNumbers.isEmpty() && anyNumberPartBorders) {
                foundNumbers.add(Integer.parseInt(String.join("", partNumbers)));
                partNumbers.clear();
            }
        }

        int sum = foundNumbers.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Answer1: " + sum);

        List<Integer> gearRatios = new ArrayList<>();
        for (int i = 0; i < parts.size(); i++) {
            List<String> row = parts.get(i);
            for (int j = 0; j < row.size(); j++) {
                String cursor = row.get(j);
                if ("*".equals(cursor)) {
                    // check if bordering two numbers
                    List<Pair<Integer, Integer>> gearNumberCoords = getGearNumberCoords(parts, i, j);
                    if (gearNumberCoords.size() >= 2) {
                        // gear found
                        Set<Integer> gearNumbers = new HashSet<>();
                        for (Pair<Integer, Integer> coord : gearNumberCoords) {
                            gearNumbers.add(getGearNumber(parts, coord.getValue0(), coord.getValue1()));
                        }

                        // check if the neighbor numbers are two unique ones
                        if (gearNumbers.size() == 2) {
                            Integer[] array = gearNumbers.toArray(new Integer[]{});
                            gearRatios.add(array[0] * array[1]);
                        }
                    }
                }
            }
        }

        int gearSum = gearRatios.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Answer2: " + gearSum);
    }

    public static Integer getGearNumber(List<List<String>> parts, int x, int y) {
        List<String> row = parts.get(x);

        int beginIndex = y;
        String cursor = row.get(y);
        while (isNumeric(cursor)) {
            beginIndex--;
            try {
                cursor = row.get(beginIndex);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }

        beginIndex++; // move index back to number begin
        cursor = row.get(beginIndex);

        List<String> numberParts = new ArrayList<>();
        while(isNumeric(cursor)) {
            numberParts.add(cursor);
            beginIndex++;
            try {
                cursor = row.get(beginIndex);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }

        return Integer.parseInt(String.join("", numberParts));
    }

    public static List<Pair<Integer, Integer>> getGearNumberCoords(List<List<String>> parts, int x, int y) {
        List<Pair<Integer, Integer>> numberCoords = new ArrayList<>();

        if (isNumeric(saveGet(parts, x - 1, y - 1))) {
            numberCoords.add(Pair.with(x - 1, y - 1));
        }
        if (isNumeric(saveGet(parts, x - 1, y))) {
            numberCoords.add(Pair.with(x - 1, y));
        }
        if (isNumeric(saveGet(parts, x - 1, y + 1))) {
            numberCoords.add(Pair.with(x - 1, y + 1));
        }
        if (isNumeric(saveGet(parts, x, y - 1))) {
            numberCoords.add(Pair.with(x, y - 1));
        }
        if (isNumeric(saveGet(parts, x, y + 1))) {
            numberCoords.add(Pair.with(x, y + 1));
        }
        if (isNumeric(saveGet(parts, x + 1, y - 1))) {
            numberCoords.add(Pair.with(x + 1, y - 1));
        }
        if (isNumeric(saveGet(parts, x + 1, y))) {
            numberCoords.add(Pair.with(x + 1, y));
        }
        if (isNumeric(saveGet(parts, x + 1, y + 1))) {
            numberCoords.add(Pair.with(x + 1, y + 1));
        }
        return numberCoords;
    }

    public static boolean specialCharacterIsBordering(List<List<String>> parts, int x, int y) {
        String s1 = saveGet(parts, x - 1, y - 1);
        String s2 = saveGet(parts, x - 1, y);
        String s3 = saveGet(parts, x - 1, y + 1);
        String s4 = saveGet(parts, x, y - 1);
        String s6 = saveGet(parts, x, y + 1);
        String s7 = saveGet(parts, x + 1, y - 1);
        String s8 = saveGet(parts, x + 1, y);
        String s9 = saveGet(parts, x + 1, y + 1);

        return Stream.of(s1, s2, s3, s4, s6, s7, s8, s9).anyMatch(s -> !isNumeric(s) && !".".equals(s));
    }

    public static String saveGet(List<List<String>> list, int x, int y) {
        try {
            return list.get(x).get(y);
        } catch (IndexOutOfBoundsException e) {
            return ".";
        }
    }

    public static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}
