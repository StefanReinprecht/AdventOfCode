package aoc.year2023.day12;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Day12 {
    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input12_sample.txt");
        //List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input12.txt");

        List<Line> lines = inputList.stream().map(Line::create).toList();

        Optional<Integer> totalPossibleArrangements = lines
                .stream()
                .mapToInt(Day12::calcPossibleArrangements)
                .boxed()
                .reduce(Integer::sum);
        totalPossibleArrangements.ifPresent(integer -> System.out.println("Answer 1: " + integer));
    }

    private static int calcPossibleArrangements(Line line) {
        int arrangements = 0;
        for (int i = 0; i < line.conditions.size(); i++) {
            int conditionLength = Integer.parseInt(line.conditions.get(i));
            Spring spring = line.springs.get(i);

            if ((spring.type == SpringType.DAMAGED || spring.type == SpringType.OPERATIONAL) && spring.length == conditionLength) {
                continue;
            }

            if (spring.type == SpringType.UNKNOWN) {
                if (spring.length == conditionLength) {
                    arrangements++;
                }
            }
        }
        return arrangements;
    }

    public record Line(List<Spring> springs, List<String> conditions) {

        public static Line create(String input) {
            String[] parts = input.split(" ");

            List<Spring> springs = new ArrayList<>();
            SpringType currentType = null;
            int counter = 0;
            String[] springSplit = parts[0].split("");
            for (int i = 0; i < springSplit.length; i++) {
                if ("?".equals(springSplit[i])) {
                    if (currentType == null) {
                        currentType = SpringType.UNKNOWN;
                        counter++;
                    } else if (currentType == SpringType.UNKNOWN) {
                        counter++;
                    } else {
                        springs.add(new Spring(currentType, counter));
                        currentType = null;
                        counter = 0;
                    }
                }

                if (".".equals(springSplit[i])) {
                    if (currentType == null) {
                        currentType = SpringType.OPERATIONAL;
                        counter++;
                    } else if (currentType == SpringType.OPERATIONAL) {
                        counter++;
                    } else {
                        springs.add(new Spring(currentType, counter));
                        currentType = null;
                        counter = 0;
                    }
                }

                if ("?".equals(springSplit[i])) {
                    if (currentType == null) {
                        currentType = SpringType.DAMAGED;
                        counter++;
                    } else if (currentType == SpringType.DAMAGED) {
                        counter++;
                    } else {
                        springs.add(new Spring(currentType, counter));
                        currentType = null;
                        counter = 0;
                    }
                }
            }

            return new Line(
                    springs,
                    Arrays.asList(parts[1].split(","))
            );
        }
    }

    public record Spring(SpringType type, int length) {}

    public enum SpringType {
        OPERATIONAL,
        DAMAGED,
        UNKNOWN;
    }
}
