package main.java.aoc.y2023.day3;

import main.java.aoc.y2023.utils.Utils;

import java.util.List;

public class Day3 {

    public static void main(String[] args) throws Exception {
        List<String> inputList = Utils.readAllLinesFromStream(Day3.class.getResource("input3.txt").toURI());

        int sum = 0;
        for (String input : inputList) {
            System.out.println(input);

            // split input in half
            char[] left = input.substring(0, (input.length() / 2)).toCharArray();
            char[] right = input.substring(input.length() / 2).toCharArray();

            outer: for (char l : left) {
                for (char r : right) {
                    if (l == r) {
                        // common item found
                        System.out.println(getNumericValue(l));
                        sum += getNumericValue(l);
                        break outer;
                    }
                }
            }
        }

        int sum2 = 0;
        for (int i = 0; i < inputList.size(); i = i+3) {
            System.out.println("Group Start");
            char[] rucksack1 = inputList.get(i).toCharArray();
            char[] rucksack2 = inputList.get(i + 1).toCharArray();
            char[] rucksack3 = inputList.get(i + 2).toCharArray();

            outer: for (char a : rucksack1) {
                for (char b : rucksack2) {
                    for (char c : rucksack3) {
                        if (a == b && b == c) {
                            // common item found
                            System.out.println(getNumericValue(a));
                            sum2 += getNumericValue(a);
                            break outer;
                        }
                    }
                }
            }
        }

        System.out.println("====");
        System.out.println(sum);
        System.out.println(sum2);
    }

    public static int getNumericValue(char c) {
        int temp = c;
        if (temp <= 122 & temp >= 97) {
            return temp - 96;
        } else if (temp <= 90 & temp >= 65) {
            return temp - 64 + 26;
        } else {
            throw new UnsupportedOperationException("unsupported char: " + c);
        }
    }
}
