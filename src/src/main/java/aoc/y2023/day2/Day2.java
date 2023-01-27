package src.main.java.aoc.y2023.day2;

import java.net.URISyntaxException;
import java.util.List;

import static src.main.java.aoc.y2023.utils.Utils.readAllLinesFromStream;

public class Day2 {

    public static void main(String[] args) throws URISyntaxException {
        List<String> inputList = readAllLinesFromStream(Day2.class.getResource("input2.txt").toURI());

        int scoreByInput = 0;
        int scoreByResult = 0;

        for (String input : inputList) {
            System.out.println(input);

            String[] parts = input.split(" ");
            scoreByInput += calculateRoundScoreByInput(parts[0], parts[1]);
            scoreByResult += calculateRoundScoreByResult(parts[0], parts[1]);
        }

        System.out.println("====");
        System.out.println(scoreByInput);
        System.out.println(scoreByResult);
    }

    public static int calculateRoundScoreByInput(String oponentInput, String myInput) {
        // oponentInput A = ROCK, B = PAPER, C = SCISSORS
        // myInput X = ROCK, Y = PAPER, Z = SCISSORS

        if (oponentInput.equals("A")) {
            if (myInput.equals("X")) {
                return 4; // 1 + 3
            } else if (myInput.equals("Y")) {
                return 8; // 2 + 6
            } else {
                return 3; // 3 + 0
            }
        } else if (oponentInput.equals("B")) {
            if (myInput.equals("X")) {
                return 1; // 1 + 0
            } else if (myInput.equals("Y")) {
                return 5; // 2 + 3
            } else {
                return 9; // 3 + 6
            }
        } else {
            if (myInput.equals("X")) {
                return 7; // 1 + 6
            } else if (myInput.equals("Y")) {
                return 2; // 2 + 0
            } else {
                return 6; // 3 + 3
            }
        }
    }

    public static int calculateRoundScoreByResult(String oponentInput, String result) {
        // oponentInput A = ROCK, B = PAPER, C = SCISSORS
        // result X = LOSE, Y = DRAW, Z = WIN

        if (oponentInput.equals("A")) {
            if (result.equals("X")) {
                return 3; // 3 + 0
            } else if (result.equals("Y")) {
                return 4; // 1 + 3
            } else {
                return 8; // 2 + 6
            }
        } else if (oponentInput.equals("B")) {
            if (result.equals("X")) {
                return 1; // 1 + 0
            } else if (result.equals("Y")) {
                return 5; // 2 + 3
            } else {
                return 9; // 3 + 6
            }
        } else {
            if (result.equals("X")) {
                return 2; // 2 + 0
            } else if (result.equals("Y")) {
                return 6; // 3 + 3
            } else {
                return 7; // 1 + 6
            }
        }
    }
}
