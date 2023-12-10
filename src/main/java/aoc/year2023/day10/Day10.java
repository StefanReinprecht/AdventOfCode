package aoc.year2023.day10;

import aoc.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

public class Day10 {

    @Data
    @AllArgsConstructor
    public static class Point {
        private int x;
        private int y;
        private String val;
    }

    /**
     *
     * - | is a vertical pipe connecting north and south.
     * - - is a horizontal pipe connecting east and west.
     * - L is a 90-degree bend connecting north and east.
     * - J is a 90-degree bend connecting north and west.
     * - 7 is a 90-degree bend connecting south and west.
     * - F is a 90-degree bend connecting south and east.
     * - . is ground; there is no pipe in this tile.
     * - S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
     *
     */
    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input10.txt");

        List<Point> allPoints = new ArrayList<>();

        for (int y = 0; y < inputList.size(); y++) {
            String[] line = inputList.get(y).split("");
            for (int x = 0; x < line.length; x++) {
                allPoints.add(new Point(x, y, line[x]));
            }
        }

        Day10 day10 = new Day10(allPoints, inputList.size(), inputList.get(0).length());

        long startTime1 = System.currentTimeMillis();
        int distanceFromStart = day10.calculateFarthestDistanceFromStart();
        System.out.println("Answer 1: " + distanceFromStart);
        System.out.println("Time 1: " + (System.currentTimeMillis() - startTime1));

        long startTime2 = System.currentTimeMillis();
        int area = day10.calculateArea();
        System.out.println("Answer 2: " + area);
        System.out.println("Time 2: " + (System.currentTimeMillis() - startTime2));
    }

    private final List<Point> allPoints;
    private final int rows;
    private final int cols;
    private final List<Point> loopElements = new ArrayList<>();

    public Day10(List<Point> allPoints, int rows, int cols) {
        this.allPoints = allPoints;
        this.rows = rows;
        this.cols = cols;
    }

    public int calculateFarthestDistanceFromStart() {
        buildLoop();

        int size = loopElements.size();
        if (size % 2 != 0) {
            size--;
        }
        return size / 2;
    }

    public int calculateArea() {
        int numInsideFields = 0;
        boolean inside = false;
        String lastCorner = "";
        for (int i = 0; i < allPoints.size(); i++) {
            if (i % cols == 0) {
                inside = false;
            }

            Point currentPoint = allPoints.get(i);

            if (loopElements.contains(currentPoint)) {
                if ("|".equals(currentPoint.getVal()) || "S".equals(currentPoint.getVal())) {
                    inside = !inside; // flip
                } else if ("LF7J".contains(currentPoint.getVal())) {
                    if ((!lastCorner.equals("F") && currentPoint.getVal().equals("7")) ||
                            (!lastCorner.equals("L") && currentPoint.getVal().equals("J"))) {
                        inside = !inside; // flip
                    }

                    lastCorner = currentPoint.getVal();
                }
            } else {
                if (inside) {
                    numInsideFields++;
                }
            }
        }

        return numInsideFields;
    }

    public void buildLoop() {
        loopElements.add(findStartPoint());
        while (!loopElements.getFirst().equals(loopElements.getLast()) || loopElements.size() == 1) {
            Point nextPoint = findNextPoint(loopElements);
            loopElements.add(nextPoint);
        }
        loopElements.removeLast();
    }

    private Point findStartPoint() {
        List<Point> collect = allPoints.stream().filter(p -> "S".equals(p.val)).toList();

        if (collect.size() != 1) {
            throw new IllegalStateException("Impossible number of start points found.");
        }

        return collect.get(0);
    }

    private Point findNextPoint(List<Point> loopElements) {
        Point currentPoint = loopElements.getLast();

        if ("S|JL".contains(currentPoint.val)) {
            Optional<Point> nextNorth = getElementToNorth(currentPoint);
            if (nextNorth.isPresent()) {
                Point north = nextNorth.get();
                if (!loopElements.contains(north) && "|7F".contains(north.val)) {
                    return north;
                }
            }
        }

        if ("S-LF".contains(currentPoint.val)) {
            Optional<Point> nextEast = getElementToEast(currentPoint);
            if (nextEast.isPresent()) {
                Point east = nextEast.get();
                if (!loopElements.contains(east) && "-J7".contains(east.val)) {
                    return east;
                }
            }
        }

        if ("S|7F".contains(currentPoint.val)) {
            Optional<Point> nextSouth = getElementToSouth(currentPoint);
            if (nextSouth.isPresent()) {
                Point south = nextSouth.get();
                if (!loopElements.contains(south) && "|JL".contains(south.val)) {
                    return south;
                }
            }
        }

        if ("S-J7".contains(currentPoint.val)) {
            Optional<Point> nextWest = getElementToWest(currentPoint);
            if (nextWest.isPresent()) {
                Point west = nextWest.get();
                if (!loopElements.contains(west) && "-LF".contains(west.val)) {
                    return west;
                }
            }
        }

        return loopElements.getFirst(); // Must be the start point
    }

    private Optional<Point> getElementToNorth(Point p) {
        if (p.y == 0) {
            return Optional.empty();
        }

        return Optional.of(allPoints.get(p.x + ((p.y - 1) * cols)));
    }

    private Optional<Point> getElementToEast(Point p) {
        if ((p.x + 1) % cols == 0) {
            return Optional.empty();
        }

        return Optional.of(allPoints.get(p.x + 1 + (p.y * cols)));
    }

    private Optional<Point> getElementToSouth(Point p) {
        if (p.y + 1 == rows) {
            return Optional.empty();
        }

        return Optional.of(allPoints.get(p.x + ((p.y + 1) * cols)));
    }

    private Optional<Point> getElementToWest(Point p) {
        if (p.x == 0) {
            return Optional.empty();
        }

        return Optional.of(allPoints.get(p.x - 1 + (p.y * cols)));
    }
}
