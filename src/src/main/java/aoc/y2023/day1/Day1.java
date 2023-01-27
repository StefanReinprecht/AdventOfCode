package src.main.java.aoc.y2023.day1;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static src.main.java.aoc.y2023.utils.Utils.readAllLinesFromStream;

public class Day1 {

    public static void main(String[] args) throws URISyntaxException {
        List<String> inputList = readAllLinesFromStream(Day1.class.getResource("input1.txt").toURI());

        List<Integer> calories = new ArrayList<>();

        int currentCal = 0;
        for (String input : inputList) {
            System.out.println(input);

            if (!input.equals("")) {
                currentCal += Integer.valueOf(input);
            } else {
                System.out.println("Old Elf done, adding calories: " + currentCal);
                calories.add(currentCal);
                System.out.println("New Elf");
                currentCal = 0;
            }
        }

        calories.forEach(cal -> System.out.println(cal));
        calories.sort(Integer::compareTo);
        int totalSize = calories.size();

        System.out.println("====");
        System.out.println("Biggest calorie count: " + calories.get(totalSize - 1));
        System.out.println("Top 3 biggest calorie counts added : " +
                (calories.get(totalSize - 1) + calories.get(totalSize - 2) + calories.get(totalSize - 3)));
    }
}
