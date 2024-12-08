package aoc.year2021.day1;

import aoc.utils.Utils;

import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2021, "day1_input.txt");
        List<Integer> inputAsInts = inputList.stream().map(Integer::valueOf).toList();

        int counter = 0;
        for (int i = 1; i < inputAsInts.size(); i++) {
            if (inputAsInts.get(i) > inputAsInts.get(i - 1)) {
                counter++;
            }
        }

        System.out.println("Part 1: " + counter);

        counter = 0;

        for (int i = 1; i < inputAsInts.size() - 2; i++) {
            int lastSum = inputAsInts.get(i - 1) + inputAsInts.get(i) + inputAsInts.get(i + 1);
            int currentSum = inputAsInts.get(i) + inputAsInts.get(i + 1) + inputAsInts.get(i + 2);

            if (currentSum > lastSum) {
                counter++;
            }
        }

        System.out.println("Part 2: " + counter);
    }
}
