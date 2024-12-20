package aoc.year2024.day20;

import aoc.utils.Utils;

import java.util.*;
import java.util.stream.Stream;

public class Day20 {

    public static void main(String[] args) {
        //day20_input
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day20_input.txt");

        Pos[][] grid = parseGrid(inputList);

        Pos start = Stream.of(grid).flatMap(Stream::of).filter(pos -> pos.isStart()).findFirst().get();

        List<Pos> path = buildPath(grid, start);

        System.out.println(path.size());

        findShortCuts(grid, path);
    }

    private static void findShortCuts(Pos[][] grid, List<Pos> path) {
        Map<Integer, Integer> shortCuts = new HashMap<>();
        for (Pos pathPos : path) {

            try {
                if (grid[pathPos.y - 1][pathPos.x].isWall()) {
                    Pos topShortCut = grid[pathPos.y - 2][pathPos.x];
                    if (!topShortCut.isWall() && topShortCut.distanceFromStart > pathPos.distanceFromStart + 2) {
                        shortCuts.merge(topShortCut.distanceFromStart - pathPos.distanceFromStart - 2, 1, Integer::sum);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {}

            try {
                if (grid[pathPos.y][pathPos.x + 1].isWall()) {
                    Pos rightShortCut = grid[pathPos.y][pathPos.x + 2];
                    if (!rightShortCut.isWall() && rightShortCut.distanceFromStart > pathPos.distanceFromStart + 2) {
                        shortCuts.merge(rightShortCut.distanceFromStart - pathPos.distanceFromStart - 2, 1, Integer::sum);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {}

            try {
                if (grid[pathPos.y + 1][pathPos.x].isWall()) {
                    Pos bottomShortCut = grid[pathPos.y + 2][pathPos.x];
                    if (!bottomShortCut.isWall() && bottomShortCut.distanceFromStart > pathPos.distanceFromStart + 2) {
                        shortCuts.merge(bottomShortCut.distanceFromStart - pathPos.distanceFromStart - 2, 1, Integer::sum);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {}

            try {
                if (grid[pathPos.y][pathPos.x - 1].isWall()) {
                    Pos leftShortCut = grid[pathPos.y][pathPos.x - 2];
                    if (!leftShortCut.isWall() && leftShortCut.distanceFromStart > pathPos.distanceFromStart + 2) {
                        shortCuts.merge(leftShortCut.distanceFromStart - pathPos.distanceFromStart - 2, 1, Integer::sum);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {}

        }

        int totalWorthyShortcuts = 0;
        for (Map.Entry<Integer, Integer> integerIntegerEntry : shortCuts.entrySet()) {
            System.out.println(integerIntegerEntry.getValue() + " that save: " + integerIntegerEntry.getKey());

            if (integerIntegerEntry.getKey() >= 100) {
                totalWorthyShortcuts += integerIntegerEntry.getValue();
            }
        }

        System.out.println("Part 1: " + totalWorthyShortcuts);
    }

    private static List<Pos> buildPath(Pos[][] grid, Pos start) {
        List<Pos> path = new ArrayList<>();
        start.distanceFromStart = 0;
        path.add(start);

        Pos current = start;
        int distanceFromStart = 1;
        while (!current.isEnd()) {

            List<Pos> neighbours = getNeighbours(grid, current);

            Pos finalCurrent = current;
            List<Pos> filteredNeighbours = neighbours.stream().filter(n -> !(n.isWall() || n.equals(finalCurrent.previous))).toList();

            if (filteredNeighbours.size() > 1) {
                throw new IllegalStateException("Oh no, many possible neighbours found on pos: " + current.toString());
            }

            Pos nextPos = filteredNeighbours.getFirst();
            nextPos.previous = current;
            nextPos.distanceFromStart = distanceFromStart;
            current = nextPos;
            path.add(current);
            distanceFromStart++;
        }
        path.add(current);

        return path;
    }

    private static List<Pos> getNeighbours(Pos[][] grid, Pos current) {
        List<Pos> neighbours = new ArrayList<>();

        try {
            neighbours.add(grid[current.y - 1][current.x]);
        } catch (ArrayIndexOutOfBoundsException ignore) {}

        try {
            neighbours.add(grid[current.y][current.x + 1]);
        } catch (ArrayIndexOutOfBoundsException ignore) {}

        try {
            neighbours.add(grid[current.y + 1][current.x]);
        } catch (ArrayIndexOutOfBoundsException ignore) {}

        try {
            neighbours.add(grid[current.y][current.x - 1]);
        } catch (ArrayIndexOutOfBoundsException ignore) {}

        return neighbours;
    }

    private static Pos[][] parseGrid(List<String> inputList) {
        Pos[][] grid = new Pos[inputList.size()][inputList.getFirst().length()];

        for (int y = 0; y < inputList.size(); y++) {
            String[] line = inputList.get(y).split("");
            for (int x = 0; x < line.length; x++) {
                grid[y][x] = new Pos(x, y, line[x]);
            }
        }

        return grid;
    }

    public static class Pos {
        int x;
        int y;
        String type;
        int distanceFromStart;
        Pos previous;

        Pos(int x, int y, String type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        boolean isWall() {
            return type.equals("#");
        }
        
        boolean isStart() {
            return type.equals("S");
        }

        boolean isEnd() {
            return type.equals("E");
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Pos o)) {
                return false;
            }
            return Objects.equals(this.x, o.x) && Objects.equals(this.y, o.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Pos{" +
                    "x=" + x +
                    ", y=" + y +
                    ", type='" + type + '\'' +
                    ", distanceFromStart=" + distanceFromStart +
                    ", previous=" + previous +
                    '}';
        }
    }
}
