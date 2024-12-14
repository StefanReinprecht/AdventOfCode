package aoc.year2024.day14;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Day14 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day14_input.txt");

        List<Robot> robots = parseInput(inputList);

        for (int i = 0; i < 10000; i++) {
            process(robots);
            if(printIfChristmasTree(robots)) {
                System.out.println("Part 2: " + (i + 1));
            }

            if (i == 99) {
                long safetyFactor = checkSafetyFactor(robots);
                System.out.println("Part 1: " + safetyFactor);
            }
        }
    }

    private static long checkSafetyFactor(List<Robot> robots) {
        // count elements in the quadrants
        long tl = robots.stream().filter(r -> r.x < 50 && r.y < 51).count();
        long tr = robots.stream().filter(r -> r.x > 50 && r.y < 51).count();
        long bl = robots.stream().filter(r -> r.x < 50 && r.y > 51).count();
        long br = robots.stream().filter(r -> r.x > 50 && r.y > 51).count();

        return tl * tr * bl * br;
    }

    private static void process(List<Robot> robots) {
        // move robot according to its velocity
        for (Robot r : robots) {
            r.x += r.xVelo;
            r.y += r.yVelo;

            // check if map is left and teleport to other site (for example input the map is 11 x 7 tiles)
            if (r.x > 101 - 1) {
                r.x -= 101;
            } else if (r.x < 0) {
                r.x += 101;
            }

            if (r.y > 103 - 1) {
                r.y -= 103;
            } else if (r.y < 0) {
                r.y += 103;
            }
        }
    }

    private static boolean printIfChristmasTree(List<Robot> robots) {
        List<String> lines = new ArrayList<>();

        String[][] grid = new String[103][101];
        for (Robot r : robots) {
            grid[r.y][r.x] = "#";
        }

        for (String[] strings : grid) {
            StringBuilder sb = new StringBuilder();
            for (String string : strings) {
                if (string != null) {
                    sb.append("#");
                } else {
                    sb.append(" ");
                }
            }
            lines.add(sb.toString());
        }

        if (lines.stream().anyMatch(s -> s.contains("###############################"))) {
            lines.forEach(System.out::println);
            return true;
        }
        return false;
    }

    private static List<Robot> parseInput(List<String> inputList) {
        List<Robot> robots = new ArrayList<>();
        for (String line : inputList) {
            String[] parts = line.split(" ");
            String[] startPos = parts[0].substring(2).split(",");
            String[] velos = parts[1].substring(2).split(",");

            Robot r = new Robot(
                    Integer.parseInt(startPos[0]),
                    Integer.parseInt(startPos[1]),
                    Integer.parseInt(velos[0]),
                    Integer.parseInt(velos[1])
            );
            robots.add(r);
        }
        return robots;
    }

    public static class Robot {
        int x;
        final int xVelo;
        int y;
        final int yVelo;

        public Robot(int startX, int startY, int xVelo, int yVelo) {
            this.x = startX;
            this.y = startY;
            this.xVelo = xVelo;
            this.yVelo = yVelo;
        }

        @Override
        public String toString() {
            return "Robot{" +
                    "x=" + x +
                    ", y=" + y +
                    ", xVelo=" + xVelo +
                    ", yVelo=" + yVelo +
                    '}';
        }
    }
}
