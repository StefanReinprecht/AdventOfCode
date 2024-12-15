package aoc.year2024.day15;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day15 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day15_input.txt");

        long startPart1 = System.currentTimeMillis();
        String[][] grid = parseGrid(inputList);
        List<String> instructions = parseInstructions(inputList);
        Pos initialRobotPos = getInitialRobotPos(grid);
        process(initialRobotPos, grid, instructions);
        long part1Result = sumGPSCoords(grid);

        System.out.println("Part 1: " + part1Result + " in " + (System.currentTimeMillis() - startPart1) + "ms");
    }

    private static long sumGPSCoords(String[][] grid) {
        long totalSum = 0;
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y].equals("O")) {
                    totalSum += y * 100L + x;
                }
            }
        }
        return totalSum;
    }

    private static void process(Pos currentRobotPos, String[][] grid, List<String> instructions) {
        for (String instruction : instructions) {
            Pos nextPos = switch (instruction) {
                case "^" -> new Pos(currentRobotPos.x, currentRobotPos.y - 1);
                case ">" -> new Pos(currentRobotPos.x + 1, currentRobotPos.y);
                case "v" -> new Pos(currentRobotPos.x, currentRobotPos.y + 1);
                default -> new Pos(currentRobotPos.x - 1, currentRobotPos.y);
            };

            currentRobotPos = tryMove(currentRobotPos, nextPos, instruction, grid);
        }
    }

    private static Pos tryMove(Pos currentRobotPos, Pos nextPos, String direction, String[][] grid) {
        // check if nextPos is a wall
        if (grid[nextPos.x][nextPos.y].equals("#")) {
            return currentRobotPos;
        }

        // check if nextPos is a box
        if (grid[nextPos.x][nextPos.y].equals("O")) {
            if (tryPushBox(nextPos, direction, grid)) {
                grid[currentRobotPos.x][currentRobotPos.y] = ".";
                grid[nextPos.x][nextPos.y] = "@";
            } else {
                return currentRobotPos;
            }
        }

        grid[currentRobotPos.x][currentRobotPos.y] = ".";
        grid[nextPos.x][nextPos.y] = "@";
        return nextPos;
    }

    private static boolean tryPushBox(Pos currentBoxPos, String direction, String[][] grid) {
        Pos nextBoxPos = switch (direction) {
            case "^" -> new Pos(currentBoxPos.x, currentBoxPos.y - 1);
            case ">" -> new Pos(currentBoxPos.x + 1, currentBoxPos.y);
            case "v" -> new Pos(currentBoxPos.x, currentBoxPos.y + 1);
            default -> new Pos(currentBoxPos.x - 1, currentBoxPos.y);
        };

        // check if nextBoxPos is a wall
        if (grid[nextBoxPos.x][nextBoxPos.y].equals("#")) {
            return false;
        }

        // check if nextPos is a box
        if (grid[nextBoxPos.x][nextBoxPos.y].equals("O")) {
            if (tryPushBox(nextBoxPos, direction, grid)) {
                grid[currentBoxPos.x][currentBoxPos.y] = ".";
                grid[nextBoxPos.x][nextBoxPos.y] = "O";
                return true;
            } else {
                return false;
            }
        }

        grid[currentBoxPos.x][currentBoxPos.y] = ".";
        grid[nextBoxPos.x][nextBoxPos.y] = "O";
        return true;
    }

    private static String[][] parseGrid(List<String> inputList) {
        String[][] grid = new String[inputList.getFirst().length()][(int) inputList.stream().filter(l -> l.startsWith("#")).count()];

        for (int i = 0; i < inputList.size(); i++) {
            String line = inputList.get(i);
            if (line.isEmpty()) {
                break;
            }
            String[] split = line.split("");
            for(int j = 0; j < split.length; j++) {
                grid[j][i] = split[j];
            }
        }
        return grid;
    }

    private static List<String> parseInstructions(List<String> inputList) {
        List<String> instructions = new ArrayList<>();
        for (String line : inputList) {
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }
            String[] split = line.split("");
            instructions.addAll(Arrays.stream(split).toList());
        }
        return instructions;
    }

    private static Pos getInitialRobotPos(String[][] grid) {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y].equals("@")) {
                    return new Pos(x, y);
                }
            }
        }
        throw new RuntimeException("This is not possible");
    }

    private static void printGrid(String[][] grid) {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                System.out.print(grid[x][y]);
            }
            System.out.println();
        }
    }

    private static class Pos {
        int x;
        int y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
