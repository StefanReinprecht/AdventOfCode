package main.java.aoc.y2023.day4;

import java.util.List;

import static main.java.aoc.y2023.utils.Utils.readAllLinesFromStream;

public class Day4 {

    public static void main(String[] args) throws Exception {
        List<String> inputList = readAllLinesFromStream(Day4.class.getResource("input4.txt").toURI());

        int containsCounter = 0;
        int touchCounter = 0;
        for (String input : inputList) {
            System.out.println(input);
            String[] elfpairs = input.split(",");

            int firstElfStart = Integer.valueOf(elfpairs[0].split("-")[0]);
            int firstElfEnd = Integer.valueOf(elfpairs[0].split("-")[1]);

            int secondElfStart = Integer.valueOf(elfpairs[1].split("-")[0]);
            int secondElfEnd = Integer.valueOf(elfpairs[1].split("-")[1]);

            if (firstElfStart >= secondElfStart && firstElfEnd <= secondElfEnd) {
                System.out.println("First is contained in second");
                containsCounter++;
            } else if (secondElfStart >= firstElfStart && secondElfEnd <= firstElfEnd) {
                System.out.println("Second is contained in first");
                containsCounter++;
            } else {
                System.out.println("No contains the other");
            }

            if (firstElfStart <= secondElfEnd && firstElfEnd >= secondElfStart) {
                System.out.println("First is contained in second");
                touchCounter++;
            } else if (secondElfStart <= firstElfEnd && secondElfEnd >= firstElfStart) {
                System.out.println("Second is contained in first");
                touchCounter++;
            } else {
                System.out.println("No contains the other");
            }
        }

        System.out.println("===");
        System.out.println(containsCounter);
        System.out.println(touchCounter);
    }
}
