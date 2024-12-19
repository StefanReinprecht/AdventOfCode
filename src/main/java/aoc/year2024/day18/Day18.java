package aoc.year2024.day18;

import aoc.utils.Utils;

import java.util.*;

public class Day18 {
    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day18_input.txt");

        Pos[][] grid = parseInput(inputList);
        int pathLength = findShortestPathLength(grid);
        System.out.println("Part 1: " + pathLength);

        for (int i = 1024; i < inputList.size(); i++) {
            clearGrid(grid);
            String line = inputList.get(i);
            String[] split = line.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);

            grid[y][x].corrupted = true;
            try {
                findShortestPathLength(grid);
            } catch (IllegalStateException e) {
                System.out.println("Part 2: " + x + "," + y);
                break;
            }
        }
    }

    private static int findShortestPathLength(Pos[][] grid) {
        Pos startPos = grid[0][0];
        startPos.score = 0;

        Queue<Pos> queue = new PriorityQueue<>();
        queue.add(startPos);

        while(!queue.isEmpty()) {
            Pos current = queue.poll();

            if (current.x == 70 && current.y == 70) {
                // end found
                return current.score;
            }

            List<Pos> neighbours = getNeighbours(grid, current);
            for (Pos neighbour : neighbours) {
                if (neighbour.corrupted || neighbour.equals(current.previous)) {
                    continue;
                }

                if (neighbour.score > current.score + 1) {
                    neighbour.score = current.score + 1;
                    neighbour.previous = current;

                    queue.add(neighbour);
                }
            }
        }

        throw new IllegalStateException("No path found!");
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

    private static Pos[][] parseInput(List<String> inputList) {
        Pos[][] grid = new Pos[71][71];

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = new Pos(x, y);
            }
        }

        for (int i = 0; i < 1024; i++) {
            String line = inputList.get(i);
            String[] split = line.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);

            grid[y][x].corrupted = true;
        }

        return grid;
    }

    public static void clearGrid(Pos[][] grid) {
        for (Pos[] pos : grid) {
            for (Pos po : pos) {
                po.score = Integer.MAX_VALUE;
                po.previous = null;
            }
        }
    }

    public static class Pos implements Comparable<Pos> {
        int x;
        int y;
        int score = Integer.MAX_VALUE;
        boolean corrupted;
        Pos previous;

        Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Pos o) {
            return Integer.compare(this.score, o.score);
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
            return corrupted ? "#" : ".";
        }
    }
}
