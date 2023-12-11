package aoc.year2023.day11;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day11 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input11_sample.txt");
        //List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input11.txt");

        List<List<String>> allPoints = new ArrayList<>();

        for (String line : inputList) {
            List<String> row = new ArrayList<>(Arrays.asList(line.split("")));
            allPoints.add(row);
            if (!line.contains("#")) {
                // immediate space expansion of rows
                allPoints.add(row);
            }
        }

        List<Integer> numOfColsToExpand = findNumOfColsToExpand(allPoints);

        // Space expansion for cols
        for (int col : numOfColsToExpand.reversed()) {
            for (List<String> row : allPoints) {
                row.add(col, ".");
            }
        }

        //print(allPoints);

        List<Galaxy> galaxies = collectAllGalaxies(allPoints);

        System.out.println(galaxies.size());

        for (Galaxy g: galaxies) {
            for (Galaxy h : galaxies) {

            }
        }
    }

    private static List<Galaxy> collectAllGalaxies(List<List<String>> allPoints) {
        List<Galaxy> galaxies = new ArrayList<>();

        for (int y = 0; y < allPoints.size(); y++) {
            List<String> row = allPoints.get(y);
            for (int x = 0; x < row.size(); x++) {
                if (row.get(x).equals("#")) {
                    galaxies.add(new Galaxy(x, y));
                }
            }
        }

        return galaxies;
    }

    private static void print(List<List<String>> allPoints) {
        for (List<String> row : allPoints) {
            for (String s : row) {
                System.out.print(s);
            }
            System.out.println();
        }
    }

    private static List<Integer> findNumOfColsToExpand(List<List<String>> allPoints) {
        List<Integer> numOfColsToExpand = new ArrayList<>();
        int rowLenght = allPoints.get(0).size();
        for (int i = 0; i < rowLenght; i++) {
            boolean galaxyFound = false;
            for (List<String> row : allPoints) {
                if (row.get(i).equals("#")) {
                    galaxyFound = true;
                    break;
                }
            }

            if (!galaxyFound) {
                numOfColsToExpand.add(i);
            }
        }
        return numOfColsToExpand;
    }

    public record Galaxy(int x, int y){}
}
