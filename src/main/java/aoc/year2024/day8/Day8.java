package aoc.year2024.day8;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day8_input.txt");

        Coord[][] grid = buildGrid(inputList);

        Map<String, List<Coord>> antennaLocations = findAntennaLocations(grid);

        // Part 1
        setAntiNodeLocations(antennaLocations, grid, false);

        int resultPart1 = countAntiNodeLocations(grid, false);

        System.out.println("Part 1: " + resultPart1);

        // Part 2
        clearAntiNodeLocations(grid);

        setAntiNodeLocations(antennaLocations, grid, true);

        int resultPart2 = countAntiNodeLocations(grid, true);

        System.out.println("Part 2: " + resultPart2);
    }

    private static Coord[][] buildGrid(List<String> inputList) {
        Coord[][] grid = new Coord[inputList.getFirst().length()][inputList.size()];

        for (int y = 0; y < inputList.size(); y++) {
            for (int x = 0; x < inputList.get(y).length(); x++) {
                char c = inputList.get(y).charAt(x);
                grid[x][y] = new Coord(x, y, c);
            }
        }

        return grid;
    }

    private static Map<String, List<Coord>> findAntennaLocations(Coord[][] grid) {
        Map<String, List<Coord>> locations = new HashMap<>();

        for (Coord[] coords : grid) {
            for (Coord coord : coords) {
                if (coord.isAntenna) {
                    locations.computeIfAbsent(coord.antennaType, key -> new ArrayList<>()).add(coord);
                }
            }
        }

        return locations;
    }

    private static void setAntiNodeLocations(Map<String, List<Coord>> antennaLocations, Coord[][] grid, boolean part2) {
        for (String key: antennaLocations.keySet()) {
            List<Coord> antennas = antennaLocations.get(key);
            setAntiNodeLocationsForAntennaType(antennas, grid, part2);
        }
    }

    private static void setAntiNodeLocationsForAntennaType(List<Coord> antennas, Coord[][] grid, boolean part2) {
        for (int i = 0; i < antennas.size(); i++) {
            for (int j = i + 1; j < antennas.size(); j++) {
                int xDiff = antennas.get(i).xCoord - antennas.get(j).xCoord;
                int yDiff =antennas.get(i).yCoord - antennas.get(j).yCoord;

                try {
                    Coord coord1 = grid[antennas.get(i).xCoord + xDiff][antennas.get(i).yCoord + yDiff];
                    do {
                        if (!coord1.equals(antennas.get(j))) {
                            coord1.setAntiNode(true);
                        }
                        coord1 = grid[coord1.xCoord + xDiff][coord1.yCoord + yDiff];
                    } while (part2);
                } catch (ArrayIndexOutOfBoundsException ignored) {}

                try {
                    Coord coord2 = grid[antennas.get(i).xCoord - xDiff][antennas.get(i).yCoord - yDiff];
                    do {
                        if (!coord2.equals(antennas.get(j))) {
                            coord2.setAntiNode(true);
                        }
                        coord2 = grid[coord2.xCoord - xDiff][coord2.yCoord - yDiff];
                    } while (part2);
                } catch (ArrayIndexOutOfBoundsException ignored) {}

                try {
                    Coord coord3 = grid[antennas.get(j).xCoord + xDiff][antennas.get(j).yCoord + yDiff];
                    do {
                        if (!coord3.equals(antennas.get(i))) {
                            coord3.setAntiNode(true);
                        }
                        coord3 = grid[coord3.xCoord + xDiff][coord3.yCoord + yDiff];
                    } while (part2);
                } catch (ArrayIndexOutOfBoundsException ignored) {}

                try {
                    Coord coord4 = grid[antennas.get(j).xCoord - xDiff][antennas.get(j).yCoord - yDiff];
                    do {
                        if (!coord4.equals(antennas.get(i))) {
                            coord4.setAntiNode(true);
                        }
                        coord4 = grid[coord4.xCoord - xDiff][coord4.yCoord - yDiff];
                    } while (part2);
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }
    }

    private static int countAntiNodeLocations(Coord[][] grid, boolean part2) {
        int antiNodeLocationsCounter = 0;
        for (Coord[] coords : grid) {
            for (Coord coord : coords) {
                if (coord.isAntiNode || (part2 && coord.isAntenna)) {
                    antiNodeLocationsCounter++;
                }
            }
        }
        return antiNodeLocationsCounter;
    }

    private static void clearAntiNodeLocations(Coord[][] grid) {
        for (Coord[] coords : grid) {
            for (Coord coord : coords) {
                if (coord.isAntiNode) {
                    coord.setAntiNode(false);
                }
            }
        }
    }

    public static class Coord {

        private final int xCoord;
        private final int yCoord;
        private boolean isAntenna;
        private String antennaType;
        private boolean isAntiNode;

        public Coord(int xCoord, int yCoord, char type) {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            if (type != '.') {
                isAntenna = true;
                antennaType = String.valueOf(type);
            }
        }

        public void setAntiNode(boolean antiNode) {
            isAntiNode = antiNode;
        }

        @Override
        public String toString() {
            return "[" + xCoord + ":" + yCoord + "]";
        }
    }
}
