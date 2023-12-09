package aoc.year2023.day9;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day9 {
    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input9.txt");

        long startPart1 = System.currentTimeMillis();
        int result = 0;
        for (String line : inputList) {
            List<Integer> history = Stream.of(line.split(" ")).map(Integer::parseInt).toList();
            result += calculateNextValue(history);
        }

        System.out.println("Answer 1: " + result);
        System.out.println("Time 1: " + (System.currentTimeMillis() - startPart1));

        long startPart2 = System.currentTimeMillis();
        int result2 = 0;
        for (String line : inputList) {
            List<Integer> history = Stream.of(line.split(" ")).map(Integer::parseInt).toList();
            result2 += calculatePreviousValue(history);
        }

        System.out.println("=======");
        System.out.println("Answer 2: " + result2);
        System.out.println("Time 2: " + (System.currentTimeMillis() - startPart2));
    }

    private static int calculatePreviousValue(List<Integer> history) {
        List<List<Integer>> subSequences = new ArrayList<>();
        subSequences.add(history); // Add base sequence
        calculateSubSequences(subSequences);

        int previousSeqValue = 0;
        for (int i = subSequences.size() - 1; i >= 0; i--) {
            List<Integer> subSeq = subSequences.get(i);
            previousSeqValue = subSeq.getFirst() - previousSeqValue;
        }

        return previousSeqValue;
    }

    private static int calculateNextValue(List<Integer> history) {
        List<List<Integer>> subSequences = new ArrayList<>();
        subSequences.add(history); // Add base sequence
        calculateSubSequences(subSequences);

        int nextSeqValue = 0;
        for (int i = subSequences.size() - 1; i >= 0; i--) {
            List<Integer> subSeq = subSequences.get(i);
            nextSeqValue += subSeq.getLast();
        }

        return nextSeqValue;
    }

    private static void calculateSubSequences(List<List<Integer>> subSequences) {
        List<Integer> subSequence = new ArrayList<>();
        for (int i = 1; i < subSequences.getLast().size(); i++) {
            subSequence.add(subSequences.getLast().get(i) - subSequences.getLast().get(i - 1));
        }
        subSequences.add(subSequence);

        if (!subSequence.stream().allMatch(i -> i == 0)) {
            calculateSubSequences(subSequences);
        }
    }
}
