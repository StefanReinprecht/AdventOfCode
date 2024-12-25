package aoc.year2024.day24;

import aoc.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Day24 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day24_input.txt");

        Map<String, Integer> values = parseValues(inputList);

        runInstructions(values, inputList);

        String zKeysBinary = values.keySet()
                .stream()
                .filter(k -> k.startsWith("z"))
                .sorted(Comparator.reverseOrder())
                .map(values::get)
                .map(Objects::toString)
                .collect(Collectors.joining());
        System.out.println(zKeysBinary);
        long decimalZKeysValue = Long.valueOf(zKeysBinary, 2);
        System.out.println("Part 1: " + decimalZKeysValue);
    }

    private static void runInstructions(Map<String, Integer> values, List<String> inputList) {
        LinkedList<String> queue = new LinkedList<>(inputList.subList(values.size() + 1, inputList.size()));
        while (queue.peek() != null) {
            String instruction = queue.pop();
            String[] split = instruction.split(" ");
            String val1 = split[0];
            String val2 = split[2];
            String resultVal = split[4];
            String operation = split[1];

            if (!(values.containsKey(val1) && values.containsKey(val2))) {
                queue.add(instruction);
                continue;
            }

            int result = switch (operation) {
                case "XOR" -> values.get(val1) ^ values.get(val2);
                case "OR" -> values.get(val1) | values.get(val2);
                case "AND" -> values.get(val1) & values.get(val2);
                default -> throw new IllegalStateException("Unknown operation: " + operation);
            };
            values.put(resultVal, result);
        }
    }

    private static Map<String, Integer> parseValues(List<String> inputList) {
        Map<String, Integer> values = new HashMap<>();

        for (String line : inputList) {
            if (line.isEmpty()) {
                break;
            }

            String[] split = line.split(": ");
            values.put(split[0], Integer.parseInt(split[1]));
        }
        
        return values;
    }
}
