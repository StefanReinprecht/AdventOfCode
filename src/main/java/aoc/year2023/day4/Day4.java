package aoc.year2023.day4;

import aoc.utils.Utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input4.txt");

        long startPart1 = System.currentTimeMillis();
        int totalPoints = 0;
        for (String card : inputList) {
            int matches = getMatchesForLine(card);

            if (matches != 0) {
                int cardPoints = (int) Math.pow(2, (matches - 1));
                totalPoints += cardPoints;
            }
        }

        System.out.println("Answer 1: " + totalPoints);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart1));

        // Second part can be speed-up massively by using recursion instead of looping and through a growing list
        long startPart2 = System.currentTimeMillis();
        int cursor = 0;
        int totalCards = 0;
        Map<Integer, Integer> additionalCardCounter = new HashMap<>();
        while (cursor != inputList.size()) {
            int countsPerLine = additionalCardCounter.computeIfAbsent(cursor, x -> 1);
            for (int lineRepeats = 0; lineRepeats < countsPerLine; lineRepeats++) {
                // get matches from current line
                String card = inputList.get(cursor);
                int matches = getMatchesForLine(card);

                for (int matchIndex = cursor + 1; matchIndex < matches + cursor + 1; matchIndex++) {
                    additionalCardCounter.putIfAbsent(matchIndex, 1);
                    additionalCardCounter.merge(matchIndex, 1, Integer::sum);
                }
                totalCards++;
            }

            cursor++;
        }

        System.out.println("Answer 2: " + totalCards);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart2));
    }

    private static int getMatchesForLine(String card) {
        Set<Integer> winningNumbers = Stream
                .of(
                        card
                                .split(" \\| ")[0]
                                .split(": ")[1]
                                .split(" ")
                ).filter(Predicate.not(String::isEmpty))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        Set<Integer> havingNumbers = Stream
                .of(
                        card
                                .split(" \\| ")[1]
                                .split(" ")
                ).filter(Predicate.not(String::isEmpty))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        return havingNumbers.stream().filter(winningNumbers::contains).toList().size();
    }
}
