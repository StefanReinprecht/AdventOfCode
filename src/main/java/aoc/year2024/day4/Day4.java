package aoc.year2024.day4;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day4 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day4_input.txt");

        part1(inputList);

        part2(inputList);
    }

    private static void part2(List<String> inputList) {
        long totalCrossMasFound = 0;

        int rowLength = inputList.getFirst().length();
        for (int i = 1; i < inputList.size() - 1; i++) { // skip first and last row since we're just looking for the middle A
            for (int j = 1; j < rowLength - 1; j++) { // skip first and last column since we're just looking for the middle A
                if (inputList.get(i).charAt(j) == 'A') {
                    char topLeft = inputList.get(i - 1).charAt(j - 1);
                    char topRight = inputList.get(i - 1).charAt(j + 1);
                    char bottomLeft = inputList.get(i + 1).charAt(j - 1);
                    char bottomRight = inputList.get(i + 1).charAt(j + 1);

                    boolean firstPartIsMAS = topLeft == 'M' && bottomRight == 'S' || topLeft == 'S' && bottomRight == 'M';
                    boolean secondPartIsMAS = bottomLeft == 'M' && topRight == 'S' || bottomLeft == 'S' && topRight == 'M';

                    if (firstPartIsMAS && secondPartIsMAS) {
                        totalCrossMasFound++;
                    }
                }
            }
        }

        System.out.println("Part 2: " + totalCrossMasFound);
    }

    private static void part1(List<String> inputList) {
        long totalXmasFound = 0;

        // check vertically
        for (int i = 0; i < inputList.size(); i++) {
            totalXmasFound += Pattern.compile("XMAS").matcher(inputList.get(i)).results().count();
            totalXmasFound +=  Pattern.compile("SAMX").matcher(inputList.get(i)).results().count();
        }

        // check horizontally
        int length = inputList.getFirst().length();
        for (int i = 0; i < length; i++) {
            StringBuilder sb = new StringBuilder();
            for (String s : inputList) {
                sb.append(s.charAt(i));
            }
            String horizontallyInput = sb.toString();
            totalXmasFound += Pattern.compile("XMAS").matcher(horizontallyInput).results().count();
            totalXmasFound += Pattern.compile("SAMX").matcher(horizontallyInput).results().count();
        }

        // check diagonally right
        for (int row = 0; row < inputList.size() - 3; row++) {
            for (int posInRow = 0; posInRow < length - 3; posInRow++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    sb.append(inputList.get(row + i).charAt(posInRow + i));
                }
                String diagonallyInput = sb.toString();
                totalXmasFound += Pattern.compile("XMAS").matcher(diagonallyInput).results().count();
                totalXmasFound += Pattern.compile("SAMX").matcher(diagonallyInput).results().count();
            }
        }

        // check diagonally left
        for (int row = 0; row < inputList.size() - 3; row++) {
            for (int posInRow = length - 1; 2 < posInRow; posInRow--) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    sb.append(inputList.get(row + i).charAt(posInRow - i));
                }
                String diagonallyInput2 = sb.toString();
                totalXmasFound += Pattern.compile("XMAS").matcher(diagonallyInput2).results().count();
                totalXmasFound += Pattern.compile("SAMX").matcher(diagonallyInput2).results().count();
            }
        }

        System.out.println("Part 1: " + totalXmasFound);
    }
}
