package aoc.year2023.day1;

import aoc.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input1.txt");

        int resultOne = 0;
        for (String line : inputList) {
            Pattern pattern = Pattern.compile("([0-9])", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            List<MatchResult> collect = matcher.results().toList();
            long count = collect.size();

            int add;
            if (count == 1) {
                MatchResult matchResult = collect.get(0);
                String substring = line.substring(matchResult.start(), matchResult.start() + 1);
                add = Integer.parseInt(substring + substring);
            } else {
                MatchResult matchResult = collect.get(0);
                String first = line.substring(matchResult.start(), matchResult.start() + 1);

                MatchResult lastResult = collect.getLast();
                String second = line.substring(lastResult.start(), lastResult.start() + 1);
                add = Integer.parseInt(first + second);
            }
            resultOne += add;
        }

        System.out.println("Answer1: " + resultOne);

        Map<String, String> converter = Map.of(
                "one", "1",
                "two", "2",
                "three", "3",
                "four", "4",
                "five", "5",
                "six", "6",
                "seven", "7",
                "eight", "8",
                "nine", "9"
        );

        int resultTwo = 0;
        for (String line : inputList) {
            Pattern firstDigitPattern = Pattern.compile("([0-9]|one|two|three|four|five|six|seven|eight|nine)");
            Pattern lastDigitPattern = Pattern.compile(".*([0-9]|one|two|three|four|five|six|seven|eight|nine)");

            Matcher matcherFirst = firstDigitPattern.matcher(line);
            boolean foundFirst = matcherFirst.find();
            Matcher matcherSecond = lastDigitPattern.matcher(line);
            boolean foundSecond = matcherSecond.find();

            if (!foundFirst || !foundSecond) {
                throw new IllegalStateException("No pattern in line found");
            }

            String first = matcherFirst.group(1);
            String second = matcherSecond.group(1);

            String convertedValFirst = converter.get(first);
            if (convertedValFirst != null) {
                first = convertedValFirst;
            }

            String convertedValSecond = converter.get(second);
            if (convertedValSecond != null) {
                second = convertedValSecond;
            }

            resultTwo += Integer.parseInt(first + second);
        }

        System.out.println("Answer2: " + resultTwo);
    }
}
