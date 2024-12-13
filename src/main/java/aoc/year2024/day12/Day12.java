package aoc.year2024.day12;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Day12 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day12_input.txt");

        Pos[][] grid = buildGrid(inputList);
        
        processPart1(grid);
    }

    private static void processPart1(Pos[][] grid) {
        long startPart1 = System.currentTimeMillis();
        List<Area> areas = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].visited) {
                    continue;
                }

                grid[i][j].visited = true;
                Area currentArea = new Area(grid[i][j].type);
                findArea(grid, i + 1, j, currentArea); // =>
                findArea(grid, i, j + 1, currentArea); // v
                findArea(grid, i - 1, j, currentArea); // <=
                findArea(grid, i, j - 1, currentArea); // ^
                areas.add(currentArea);
            }
        }

        int totalPrice = areas.stream().mapToInt(area -> area.areaSize * area.fenceLength).sum();
        System.out.println("Part1: " + totalPrice + " in " + (System.currentTimeMillis() - startPart1) + "ms");
    }

    private static void findArea(Pos[][] grid, int i, int j, Area currentArea) {
        Pos newPos;
        try {
            newPos = grid[i][j];
        } catch (Exception e) {
            currentArea.fenceLength += 1;
            return;
        }

        if (!newPos.type.equals(currentArea.type)) {
            currentArea.fenceLength += 1;
            return;
        }

        if (newPos.visited) {
            return;
        }

        currentArea.areaSize += 1;

        newPos.visited = true;

        findArea(grid, i + 1, j, currentArea); // =>
        findArea(grid, i, j + 1, currentArea); // v
        findArea(grid, i - 1, j, currentArea); // <=
        findArea(grid, i, j - 1, currentArea); // ^
    }

    private static Pos[][] buildGrid(List<String> inputList) {
        Pos[][] grid = new Pos[inputList.size()][inputList.getFirst().length()];

        for (int i = 0; i < inputList.size(); i++) {
            String[] split = inputList.get(i).split("");
            for (int j = 0; j < split.length; j++) {
                grid[i][j] = new Pos(split[j]);
            }
        }

        return grid;
    }

    public static class Pos {
        String type;
        boolean visited;

        public Pos(String type) {
            this.type = type;
        }
    }

    public static class Area {
        String type;
        int fenceLength;
        int areaSize;

        public Area(String type) {
            this.type = type;
            fenceLength = 0;
            areaSize = 1;
        }

        @Override
        public String toString() {
            return "Type: " + type + ", fenceLength: " + fenceLength + ", areaSize: " + areaSize;
        }
    }
}
