package aoc.year2023.day2;

import aoc.utils.Utils;

import java.util.List;

public class Day2 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input2.txt");

        int totalNumberOfIds = 0;
        int totalPower = 0;
        for (String game : inputList) {
            int i = game.indexOf(":");
            int gameId = Integer.parseInt(game.substring(0, i).split(" ")[1]);
            String gameInfo = game.substring(i + 2);
            String[] turns = gameInfo.split("; ");

            int maxTurnBlue = 0;
            int maxTurnRed = 0;
            int maxTurnGreen = 0;

            boolean allTurnsGood = true;

            for (String turn : turns) {
                String[] colors = turn.split(", ");

                int turnBlue = 0;
                int turnRed = 0;
                int turnGreen = 0;

                for (String color : colors) {
                    String[] colorInfo = color.split(" ");
                    switch (colorInfo[1]) {
                        case "blue" -> turnBlue = Integer.parseInt(colorInfo[0]);
                        case "red" -> turnRed = Integer.parseInt(colorInfo[0]);
                        case "green" -> turnGreen = Integer.parseInt(colorInfo[0]);
                        case null, default -> System.out.println("Wait what");
                    }
                }

                maxTurnBlue = Math.max(maxTurnBlue, turnBlue);
                maxTurnRed = Math.max(maxTurnRed, turnRed);
                maxTurnGreen = Math.max(maxTurnGreen, turnGreen);

                if (!(turnRed <= 12 && turnGreen <= 13 && turnBlue <= 14)) {
                    allTurnsGood = false;
                }
            }

            if (allTurnsGood) {
                totalNumberOfIds += gameId;
            }

            totalPower += (maxTurnBlue * maxTurnRed * maxTurnGreen);
        }

        System.out.println("Answer 1: " + totalNumberOfIds);
        System.out.println("Answer 2: " + totalPower);
    }
}
