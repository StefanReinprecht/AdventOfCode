package aoc.year2023.day13;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Day13 {

    public static void main(String[] args) {
        //List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input13_sample.txt");
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input13.txt");

        List<Puzzle> puzzles = new ArrayList<>();

        List<String> puzzleLines = new ArrayList<>();
        for (String line : inputList) {
            if (line.isEmpty()) {
                puzzles.add(new Puzzle(puzzleLines));
                puzzleLines = new ArrayList<>();
                continue;
            }
            puzzleLines.add(line);
        }
        puzzles.add(new Puzzle(puzzleLines));

        System.out.println("Num of puzzles: " + puzzles.size());

        long startPart1 = System.currentTimeMillis();
        long answer1 = puzzles.stream().mapToInt(Puzzle::getPart1Value).sum();

        System.out.println("Answer1: " + answer1);
        System.out.println("Time1: " + (System.currentTimeMillis() - startPart1));
    }
}
