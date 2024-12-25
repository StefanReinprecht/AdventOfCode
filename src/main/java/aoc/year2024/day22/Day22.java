package aoc.year2024.day22;

import aoc.utils.Utils;

import java.util.List;

public class Day22 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day22_input.txt");

        long totalNumbers = 0;
        for (String input : inputList) {
            totalNumbers += generate2000thSecretNumber(Long.parseLong(input));
        }
        System.out.println("Part 1: " + totalNumbers);
    }

    private static long generate2000thSecretNumber(long baseSecretNumber) {
        long lastSecretNumber = baseSecretNumber;
        for (int i = 0; i < 2000; i++) {
            lastSecretNumber = generateSecretNumber(lastSecretNumber);
        }
        return lastSecretNumber;
    }

    private static long generateSecretNumber(long secretNumber) {
        long seq1 = secretNumber * 64;
        long seq1Mix = seq1 ^ secretNumber;
        long seq1Trunc = seq1Mix % 16777216;

        long seq2 = seq1Trunc / 32;
        long seq2Mix = seq2 ^ seq1Trunc;
        long seq2Trunc = seq2Mix % 16777216;

        long seq3 = seq2Trunc * 2048;
        long seq3Mix = seq3 ^ seq2Trunc;
        return seq3Mix % 16777216;
    }
}
