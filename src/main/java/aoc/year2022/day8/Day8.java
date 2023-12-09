package aoc.year2022.day8;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {

    private final List<Point> allPoints;
    private final int gridSize;

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2022, "input8.txt");

        List<Point> allPoints = new ArrayList<>();

        for (int y = 0; y < inputList.size(); y++) {
            String[] line = inputList.get(y).split("");
            for (int x = 0; x < line.length; x++) {
                allPoints.add(new Point(x, y, Integer.parseInt(line[x])));
            }
        }

        Day8 day8 = new Day8(allPoints);

        long startTime1 = System.currentTimeMillis();
        int numberOfVisiblePoints = day8.getNumberOfVisiblePoints();
        System.out.println("Answer 1: " + numberOfVisiblePoints);
        System.out.println("Time 1: " + (System.currentTimeMillis() - startTime1));
    }

    public Day8(List<Point> allPoints) {
        this.allPoints = allPoints;
        gridSize = (int) Math.sqrt(allPoints.size());
    }

    public int getNumberOfVisiblePoints() {
        int counter = 0;
        for (Point p : allPoints) {
            if (checkIfPointIsVisible(p)) {
                counter++;
            }
        }
        return counter;
    }

    private boolean checkIfPointIsVisible(Point p) {
        int val = p.val;
        return checkIfPointIsVisibleToNorth(p, val)
                || checkIfPointIsVisibleToEast(p, val)
                || checkIfPointIsVisibleToSouth(p, val)
                || checkIfPointIsVisibleToWest(p, val);
    }

    private boolean checkIfPointIsVisibleToNorth(Point p, int val) {
        if (p.y == 0) {
            return true;
        }

        Point nextPointToNorth = allPoints.get(p.x + ((p.y - 1) * gridSize));

        if (val > nextPointToNorth.val) {
            return checkIfPointIsVisibleToNorth(nextPointToNorth, val);
        }
        return false;
    }

    private boolean checkIfPointIsVisibleToEast(Point p, int val) {
        if ((p.x + 1) % gridSize == 0) {
            return true;
        }

        Point nextPointToEast = allPoints.get(p.x + 1 + (p.y * gridSize));

        if (val > nextPointToEast.val) {
            return checkIfPointIsVisibleToEast(nextPointToEast, val);
        }
        return false;
    }

    private boolean checkIfPointIsVisibleToSouth(Point p, int val) {
        if (p.y + 1 == gridSize) {
            return true;
        }

        Point nextPointToSouth = allPoints.get(p.x + ((p.y + 1) * gridSize));

        if (val > nextPointToSouth.val) {
            return checkIfPointIsVisibleToSouth(nextPointToSouth, val);
        }
        return false;
    }

    private boolean checkIfPointIsVisibleToWest(Point p, int val) {
        if (p.x == 0) {
            return true;
        }

        Point nextPointToWest = allPoints.get(p.x - 1 + (p.y * gridSize));

        if (val > nextPointToWest.val) {
            return checkIfPointIsVisibleToWest(nextPointToWest, val);
        }
        return false;
    }

    public record Point(int x, int y, int val) {}
}
