package aoc.year2024.day7;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day7 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day7_input.txt");

//        solve(inputList, false);
//        solve(inputList, true);

        solveFast(inputList, false);
        solveFast(inputList, true);
    }

    private static void solveFast(List<String> inputList, boolean part2) {
        long startPart = System.currentTimeMillis();
        long partResult = 0;
        for (String line : inputList) {
            String[] split = line.split(": ");
            long expectedResult = Long.parseLong(split[0]);
            List<Long> numbers = Stream.of(split[1].split(" ")).map(Long::valueOf).toList();

           if ( doesEquationMatchResult(expectedResult, numbers, 0, numbers.getFirst(), part2)) {
               partResult += expectedResult;
           }
        }
        System.out.println("Part " + (part2 ? 2 : 1) + ": " + partResult + " in " + (System.currentTimeMillis() - startPart) + "ms");
    }

    private static boolean doesEquationMatchResult(long expectedResult, List<Long> numbers, int index, Long currentValue, boolean part2) {
        index++;
        if (currentValue > expectedResult) {
            // result is already too big
            return false;
        }
        if (index >= numbers.size()) {
            // check if result is matching
           return currentValue == expectedResult;
        }
        long nextValue = numbers.get(index);

        boolean doesMatch = doesEquationMatchResult(expectedResult, numbers, index, currentValue + nextValue, part2)
                || doesEquationMatchResult(expectedResult, numbers, index, currentValue * nextValue, part2);

        if (part2) {
            long concatValue = Long.parseLong(currentValue + "" + nextValue);
            doesMatch = doesMatch || doesEquationMatchResult(expectedResult, numbers, index, concatValue, part2);
        }

        return doesMatch;
    }

    private static void solve(List<String> inputList, boolean part2) {
        long startPart = System.currentTimeMillis();
        long partResult = 0;
        for (String line : inputList) {
            String[] split = line.split(": ");
            long expectedResult = Long.parseLong(split[0]);
            List<Integer> numbers = Stream.of(split[1].split(" ")).map(Integer::valueOf).toList();

            int binaryLength = numbers.size() - 1;
            String biggestBinaryString = part2 ? "2".repeat(binaryLength) : "1".repeat(binaryLength);

            // build all possible combinations
            List<List<String>> operatorCombinations = new ArrayList<>();
            for (long i = Long.parseLong(biggestBinaryString, part2 ? 3 : 2); i >= 0; i--) {
                String binaryString = Long.toUnsignedString(i, part2 ? 3 : 2);
                String paddedBinaryString = String.format("%0" + biggestBinaryString.length() + "d", Long.valueOf(binaryString));

                operatorCombinations.add(Arrays.asList(paddedBinaryString.split("")));
            }

            for (List<String> operators : operatorCombinations) {
                long potentialResult = getPotentialResult(operators, numbers);

                if (potentialResult == expectedResult) {
                    partResult += potentialResult;
                    break;
                }
            }

        }
        System.out.println("Part " + (part2 ? 2 : 1) + ": " + partResult + " in " + (System.currentTimeMillis() - startPart) + "ms");
    }

    private static long getPotentialResult(List<String> operators, List<Integer> numbers) {
        long potentialResult = numbers.getFirst();
        for (int i = 0; i < operators.size(); i++) {
            String operator = operators.get(i);
            if (operator.equals("0")) {
                potentialResult += numbers.get(i + 1);
            } else if (operator.equals("1")) {
                potentialResult *= numbers.get(i + 1);
            } else {
                potentialResult = Long.parseLong(potentialResult + "" + numbers.get(i + 1));
            }
        }
        return potentialResult;
    }
}
