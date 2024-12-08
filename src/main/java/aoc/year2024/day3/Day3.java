package aoc.year2024.day3;

import aoc.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day3_input.txt");

        int result = 0;
        for(String input : inputList) {
            Pattern pattern = Pattern.compile("(mul\\(\\d{1,3},\\d{1,3}\\))");
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String equation = matcher.group();
                String unwrapped = equation.substring(4).substring(0, equation.length() - 5);
                String[] numbers = unwrapped.split(",");
                result += (Integer.valueOf(numbers[0]) * Integer.valueOf(numbers[1]));
            }
        }
        System.out.println("Part 1: " + result);

        int result2 = 0;
        boolean enabled = true;
        for(String input : inputList) {
            Pattern pattern = Pattern.compile("(mul\\(\\d{1,3},\\d{1,3}\\))|do\\(\\)|don't\\(\\)");
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String equation = matcher.group();
                if (equation.equals("do()")) {
                    enabled = true;
                } else if (equation.equals("don't()")) {
                    enabled = false;
                } else if (enabled) {
                    String unwrapped = equation.substring(4).substring(0, equation.length() - 5);
                    String[] numbers = unwrapped.split(",");
                    result2 += (Integer.valueOf(numbers[0]) * Integer.valueOf(numbers[1]));
                }
            }
        }
        System.out.println("Part 2: " + result2);
    }
}
