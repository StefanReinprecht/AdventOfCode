package aoc.year2024.day15;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day15Part2 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day15_input.txt");

        long startPart2 = System.currentTimeMillis();
        List<List<String>> grid = parseGrid(inputList);
        List<String> instructions = parseInstructions(inputList);
        Pos initialRobotPos = getInitialRobotPos(grid);
        process(initialRobotPos, grid, instructions);
        printGrid(grid);
        long part2Result = sumGPSCoords(grid);

        System.out.println("Part 2: " + part2Result + " in " + (System.currentTimeMillis() - startPart2) + "ms");
    }

    private static long sumGPSCoords(List<List<String>> grid) {
        long totalSum = 0;
        for (int rowindex = 0; rowindex < grid.size(); rowindex++) {
            for (int colIndex = 0; colIndex < grid.get(rowindex).size(); colIndex++) {
                if (grid.get(rowindex).get(colIndex).equals("[")) {
                    totalSum += rowindex * 100L + colIndex;
                }
            }
        }
        return totalSum;
    }

    private static void process(Pos currentRobotPos, List<List<String>> grid, List<String> instructions) {
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

    private static Pos tryMove(Pos currentRobotPos, Pos nextPos, String direction, List<List<String>> grid) {
        String nextPosString = grid.get(nextPos.y).get(nextPos.x);

        // check if nextPos is a wall
        if (nextPosString.equals("#")) {
            return currentRobotPos;
        }

        // check if nextPos is a box
        if (nextPosString.equals("[") || nextPosString.equals("]")) {
            if (direction.equals("<") || direction.equals(">")) {
                // horizontal shifts are easy
                if (tryPushBoxHorizontally(nextPos, direction, grid)) {
                    grid.get(currentRobotPos.y).set(currentRobotPos.x, ".");
                    grid.get(nextPos.y).set(nextPos.x, "@");
                    return nextPos;
                } else {
                    return currentRobotPos;
                }
            } else {
                if (nextPosString.equals("[") ? tryPushVertically(new Pos(nextPos.x, nextPos.y), direction, grid) : tryPushVertically(new Pos(nextPos.x - 1, nextPos.y), direction, grid)) {
                    grid.get(currentRobotPos.y).set(currentRobotPos.x, ".");
                    grid.get(nextPos.y).set(nextPos.x, "@");
                    if (nextPosString.equals("[")) {
                        grid.get(nextPos.y).set(nextPos.x + 1, ".");
                    } else {
                        grid.get(nextPos.y).set(nextPos.x - 1, ".");
                    }
                    return nextPos;
                } else {
                    return currentRobotPos;
                }
            }
        }

        grid.get(currentRobotPos.y).set(currentRobotPos.x, ".");
        grid.get(nextPos.y).set(nextPos.x, "@");
        return nextPos;
    }

    private static boolean tryPushBoxHorizontally(Pos currentBoxPos, String direction, List<List<String>> grid) {
        Pos nextBoxPos = switch (direction) {
            case ">" -> new Pos(currentBoxPos.x + 1, currentBoxPos.y);
            case "<" -> new Pos(currentBoxPos.x - 1, currentBoxPos.y);
            default -> throw new IllegalArgumentException("Must not happen!");
        };

        String nextBoxPosString = grid.get(nextBoxPos.y).get(nextBoxPos.x);

        // check if nextBoxPos is a wall
        if (nextBoxPosString.equals("#")) {
            return false;
        }

        // check if nextPos is a box
        if (nextBoxPosString.equals("[") || nextBoxPosString.equals("]")) {
            if (tryPushBoxHorizontally(nextBoxPos, direction, grid)) {
                grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
                grid.get(currentBoxPos.y).set(currentBoxPos.x, nextBoxPosString);
                return true;
            } else {
                return false;
            }
        }

        grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
        grid.get(currentBoxPos.y).set(currentBoxPos.x, nextBoxPosString);
        return true;
    }

    private static boolean tryPushVertically(Pos currentBoxPos, String direction, List<List<String>> grid) {
        Pos nextBoxPos = switch (direction) {
            case "^" -> new Pos(currentBoxPos.x, currentBoxPos.y - 1);
            case "v" -> new Pos(currentBoxPos.x, currentBoxPos.y + 1);
            default -> throw new IllegalArgumentException("Must not happen!");
        };

        String nextBoxPosString = grid.get(nextBoxPos.y).get(nextBoxPos.x);
        String nextBoxPosRightString = grid.get(nextBoxPos.y).get(nextBoxPos.x + 1);

        // check if nextBoxPos is a wall
        if (nextBoxPosString.equals("#") || nextBoxPosRightString.equals("#")) {
            return false;
        }

        // check if nextPos is a box
        if (nextBoxPosString.equals("[")) { // || nextBoxPosRightString.equals("]")
            if (tryPushVertically(nextBoxPos, direction, grid)) {
                grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
                grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
                grid.get(currentBoxPos.y).set(currentBoxPos.x, nextBoxPosString);
                grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, nextBoxPosRightString);
                return true;
            }
            return false;
        }

        // check if next pos contains two boxes
        if (nextBoxPosString.equals("]") || nextBoxPosRightString.equals("[")) {
            if (nextBoxPosString.equals("]") && nextBoxPosRightString.equals(".")) {
                if (!isVerticalPushPossible(new Pos(nextBoxPos.x - 1, nextBoxPos.y), direction, grid)) {
                    return false;
                } else {
                    forcePushVertically(new Pos(nextBoxPos.x - 1, nextBoxPos.y), direction, grid);
                    grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
                    grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
                    grid.get(currentBoxPos.y).set(currentBoxPos.x, ".");
                    grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, ".");
                    return true;
                }
            } else if (nextBoxPosString.equals(".") && nextBoxPosRightString.equals("[")) {
                if (!isVerticalPushPossible(new Pos(nextBoxPos.x + 1, nextBoxPos.y), direction, grid)) {
                    return false;
                } else {
                    forcePushVertically(new Pos(nextBoxPos.x + 1, nextBoxPos.y), direction, grid);
                    grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
                    grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
                    grid.get(currentBoxPos.y).set(currentBoxPos.x, ".");
                    grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, ".");
                    return true;
                }
            } else {
                if (isVerticalPushPossible(new Pos(nextBoxPos.x - 1, nextBoxPos.y), direction, grid)
                && isVerticalPushPossible(new Pos(nextBoxPos.x + 1, nextBoxPos.y), direction, grid)) {
                    forcePushVertically(new Pos(nextBoxPos.x - 1, nextBoxPos.y), direction, grid);
                    forcePushVertically(new Pos(nextBoxPos.x + 1, nextBoxPos.y), direction, grid);
                    grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
                    grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
                    grid.get(currentBoxPos.y).set(currentBoxPos.x, nextBoxPosString);
                    grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, nextBoxPosRightString);
                    return true;
                } else {
                    return false;
                }
            }
        }

        grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
        grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
        grid.get(currentBoxPos.y).set(currentBoxPos.x, nextBoxPosString);
        grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, nextBoxPosRightString);
        return true;
    }

    private static boolean isVerticalPushPossible(Pos currentBoxPos, String direction, List<List<String>> grid) {
        Pos nextBoxPos = switch (direction) {
            case "^" -> new Pos(currentBoxPos.x, currentBoxPos.y - 1);
            case "v" -> new Pos(currentBoxPos.x, currentBoxPos.y + 1);
            default -> throw new IllegalArgumentException("Must not happen!");
        };

        String nextBoxPosString = grid.get(nextBoxPos.y).get(nextBoxPos.x);
        String nextBoxPosRightString = grid.get(nextBoxPos.y).get(nextBoxPos.x + 1);

        // check if nextBoxPos is a wall
        if (nextBoxPosString.equals("#") || nextBoxPosRightString.equals("#")) {
            return false;
        }

        // check if nextPos is a box
        if (nextBoxPosString.equals("[")) {
            return isVerticalPushPossible(nextBoxPos, direction, grid);
        }

        // check if next pos contains two boxes
        if (nextBoxPosString.equals("]") || nextBoxPosRightString.equals("[")) {
            if (nextBoxPosString.equals("]") && nextBoxPosRightString.equals(".")) {
                return isVerticalPushPossible(new Pos(nextBoxPos.x - 1, nextBoxPos.y), direction, grid);
            } else if (nextBoxPosString.equals(".") && nextBoxPosRightString.equals("[")) {
                return isVerticalPushPossible(new Pos(nextBoxPos.x + 1, nextBoxPos.y), direction, grid);
            } else {
                return isVerticalPushPossible(new Pos(nextBoxPos.x - 1, nextBoxPos.y), direction, grid) &&
                        isVerticalPushPossible(new Pos(nextBoxPos.x + 1, nextBoxPos.y), direction, grid);
            }
        }

        return true;
    }

    private static void forcePushVertically(Pos currentBoxPos, String direction, List<List<String>> grid) {
        Pos nextBoxPos = switch (direction) {
            case "^" -> new Pos(currentBoxPos.x, currentBoxPos.y - 1);
            case "v" -> new Pos(currentBoxPos.x, currentBoxPos.y + 1);
            default -> throw new IllegalArgumentException("Must not happen!");
        };

        String nextBoxPosString = grid.get(nextBoxPos.y).get(nextBoxPos.x);
        String nextBoxPosRightString = grid.get(nextBoxPos.y).get(nextBoxPos.x + 1);

        // check if nextPos is a box
        if (nextBoxPosString.equals("[")) { // || nextBoxPosRightString.equals("]")
            forcePushVertically(nextBoxPos, direction, grid);
            grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
            grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
            grid.get(currentBoxPos.y).set(currentBoxPos.x, ".");
            grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, ".");
            return;
        }

        // check if next pos contains two boxes
        if (nextBoxPosString.equals("]") || nextBoxPosRightString.equals("[")) {
            if (nextBoxPosString.equals("]") && nextBoxPosRightString.equals(".")) {
                forcePushVertically(new Pos(nextBoxPos.x - 1, nextBoxPos.y), direction, grid);
                grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
                grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
                grid.get(currentBoxPos.y).set(currentBoxPos.x, ".");
                grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, ".");
            } else if (nextBoxPosString.equals(".") && nextBoxPosRightString.equals("[")) {
                forcePushVertically(new Pos(nextBoxPos.x + 1, nextBoxPos.y), direction, grid);
                grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
                grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
                grid.get(currentBoxPos.y).set(currentBoxPos.x, ".");
                grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, ".");
            } else {
                forcePushVertically(new Pos(nextBoxPos.x - 1, nextBoxPos.y), direction, grid);
                forcePushVertically(new Pos(nextBoxPos.x + 1, nextBoxPos.y), direction, grid);
                grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
                grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
                grid.get(currentBoxPos.y).set(currentBoxPos.x, ".");
                grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, ".");
            }
            return;
        }

        grid.get(nextBoxPos.y).set(nextBoxPos.x, grid.get(currentBoxPos.y).get(currentBoxPos.x));
        grid.get(nextBoxPos.y).set(nextBoxPos.x + 1, grid.get(currentBoxPos.y).get(currentBoxPos.x + 1));
        grid.get(currentBoxPos.y).set(currentBoxPos.x, ".");
        grid.get(currentBoxPos.y).set(currentBoxPos.x + 1, ".");
    }

    private static List<List<String>> parseGrid(List<String> inputList) {
        List<List<String>> grid = new ArrayList<>();
        for (String line : inputList) {
            if (line.isEmpty()) {
                break;
            }
            List<String> row = new ArrayList<>();
            String[] split = line.split("");
            for (String s : split) {
                switch (s) {
                    case "#" -> {
                        row.add("#");
                        row.add("#");
                    }
                    case "O" -> {
                        row.add("[");
                        row.add("]");
                    }
                    case "." -> {
                        row.add(".");
                        row.add(".");
                    }
                    case "@" -> {
                        row.add("@");
                        row.add(".");
                    }
                    default -> throw new IllegalArgumentException("This must not happen");
                }
            }
            grid.add(row);
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

    private static Pos getInitialRobotPos(List<List<String>> grid) {
        for (int x = 0; x < grid.getFirst().size(); x++) {
            for (int y = 0; y < grid.size(); y++) {
                if (grid.get(y).get(x).equals("@")) {
                    return new Pos(x, y);
                }
            }
        }
        throw new RuntimeException("This is not possible");
    }

    private static void printGrid(List<List<String>> grid) {
        for (List<String> row : grid) {
            for (String s : row) {
                System.out.print(s);
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
