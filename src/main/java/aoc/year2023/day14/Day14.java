package aoc.year2023.day14;

import aoc.utils.Utils;

import java.util.*;

public class Day14 {
    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input14.txt");

        List<List<String>> input = inputList.stream().map(s -> (List<String>) new ArrayList<>(Arrays.asList(s.split("")))).toList();

        long start = System.currentTimeMillis();
        for (int i = 1; i < input.size(); i++) {
            boolean anyMove = true;
            int currentLine = i;
            while (anyMove) {
                anyMove = false;
                List<String> line = input.get(currentLine);
                List<String> aboveLine = input.get(currentLine - 1);
                for (int j = 0; j < line.size(); j++) {
                    if (line.get(j).equals("O") && aboveLine.get(j).equals(".")) {
                        aboveLine.set(j, "O");
                        line.set(j, ".");
                        anyMove = true;
                    }
                }
                if (currentLine > 1) {
                    currentLine--;
                }
            }
        }

        // calc weight
        List<List<String>> reversed = input.reversed();
        int totalWeight = 0;
        for (int i = 0; i < reversed.size(); i++) {
            totalWeight += (i + 1) * reversed.get(i).stream().filter(s -> s.equals("O")).toList().size();
        }

        System.out.println("Answer 1: " + totalWeight);
        System.out.println("Time 1: " + (System.currentTimeMillis() - start));

        List<List<String>> input2 = inputList.stream().map(s -> (List<String>) new ArrayList<>(Arrays.asList(s.split("")))).toList();

        long start2 = System.currentTimeMillis();
        Map<List<List<String>>, List<List<String>>> memo = new HashMap<>();
        int roundTrip = 0;
        List<List<String>> roundTripStart = null;
        int simCycles = 1_000_000_000;
        for (int count = 0; count < simCycles; count++) {
            if (roundTripStart != null && roundTripStart.equals(input2) && roundTrip > 1 && count + roundTrip < simCycles - 1) {
                int modulo = (simCycles - count) % roundTrip;
                for (int m = 0; m < modulo; m++) {
                    input2 = memo.get(input2);
                }
                break;
            } else
            if (memo.containsKey(input2)) {
                if (roundTripStart == null) {
                    roundTripStart = deepCopy(input2);
                }
                input2 = memo.get(input2);
                roundTrip++;
            } else {
                List<List<String>> output = cycle(input2);
                memo.put(input2, output);
                input2 = output;
            }
        }

        int totalWeight2 = calcWeight(input2);

        System.out.println("Answer 2: " + totalWeight2);
        System.out.println("Time 2: " + (System.currentTimeMillis() - start2));
    }

    private static int calcWeight(List<List<String>> input2) {
        // calc weight
        List<List<String>> reversed = input2.reversed();
        int totalWeight2 = 0;
        for (int i = 0; i < reversed.size(); i++) {
            totalWeight2 += (i + 1) * reversed.get(i).stream().filter(s -> s.equals("O")).toList().size();
        }
        return totalWeight2;
    }

    private static List<List<String>> deepCopy(List<List<String>> originalList) {
        List<List<String>> deepCopyList = new ArrayList<>();

        for (List<String> innerList : originalList) {
            // Create a new inner list and copy elements
            List<String> innerCopy = new ArrayList<>(innerList.size());
            innerCopy.addAll(innerList);

            // Add the inner copy to the deep copy list
            deepCopyList.add(innerCopy);
        }

        return deepCopyList;
    }

    private static List<List<String>> cycle(List<List<String>> input) {
        List<List<String>> copy = deepCopy(input);
        // move everything one step to north
        for (int i = 1; i < copy.size(); i++) {
            boolean anyMove = true;
            int currentLine = i;
            while (anyMove) {
                anyMove = false;
                List<String> line = copy.get(currentLine);
                List<String> aboveLine = copy.get(currentLine - 1);
                for (int j = 0; j < line.size(); j++) {
                    if (line.get(j).equals("O") && aboveLine.get(j).equals(".")) {
                        aboveLine.set(j, "O");
                        line.set(j, ".");
                        anyMove = true;
                    }
                }
                if (currentLine > 1) {
                    currentLine--;
                }
            }
        }

        // move everything one step to west
        for (List<String> line : copy) {
            for (int j = 1; j < line.size(); j++) {
                boolean anyMove = true;
                int currentCol = j;
                while (anyMove) {
                    if (currentCol < 1) {
                        break;
                    }

                    anyMove = false;
                    if (line.get(currentCol).equals("O") && line.get(currentCol - 1).equals(".")) {
                        line.set(currentCol - 1, "O");
                        line.set(currentCol, ".");
                        anyMove = true;
                    }

                    currentCol--;
                }
            }
        }

        // move everything one step to south
        for (int i = copy.size() - 2; 0 <= i; i--) {
            boolean anyMove = true;
            int currentLine = i;
            while (anyMove) {
                anyMove = false;
                List<String> line = copy.get(currentLine);
                List<String> belowLine = copy.get(currentLine + 1);
                for (int j = 0; j < line.size(); j++) {
                    if (line.get(j).equals("O") && belowLine.get(j).equals(".")) {
                        belowLine.set(j, "O");
                        line.set(j, ".");
                        anyMove = true;
                    }
                }
                if (currentLine < copy.size() - 2) {
                    currentLine++;
                }
            }
        }

        // move everything all the way to east
        for (List<String> line : copy) {
            for (int j = line.size() - 1; 0 <= j; j--) {
                boolean anyMove = true;
                int currentCol = j;
                while (anyMove) {
                    anyMove = false;

                    if (currentCol >= line.size() - 1) {
                        break;
                    }

                    if (line.get(currentCol).equals("O") && line.get(currentCol + 1).equals(".")) {
                        line.set(currentCol + 1, "O");
                        line.set(currentCol, ".");
                        anyMove = true;
                    }

                    currentCol++;
                }
            }
        }

        return copy;
    }
}
