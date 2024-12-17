package aoc.year2024.day16;

import aoc.utils.Utils;

import java.util.*;

public class Day16V2 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day16_example4.txt");

        Map<Direction, Pos>[][] grid = parseGrid(inputList);
        Pos startPos = findStartPos(grid);
        List<Pos> endPos = findEndPos(grid);

        Pos endPosWithPath = findPath(grid, startPos, endPos);
//        System.out.println(endPosWithPath.getPath());
        //printGrid(grid, endPosWithPath.getPath());
    }

    private static Pos findPath(Map<Direction, Pos>[][] grid, Pos startPos, List<Pos> endPos) {
        startPos.score = 0;

        Queue<Pos> queue = new PriorityQueue<>();

        queue.add(startPos);

        while(!queue.isEmpty()) {
            Pos current = queue.poll();

            if (current.equals(endPos)) {
                System.out.println("Found destination");
                System.out.println("Score: " + current.score);
                return current;
            }

            Map<String, Pos> neighbours = getNeighbours(grid, current);
            for (Map.Entry<String, Pos> neighbourEntry : neighbours.entrySet()) {
                String direction = neighbourEntry.getKey();
                Pos neighbour = neighbourEntry.getValue();
                if(!isValidPathPos(neighbour) || neighbour.equals(current.previous) || currentPathContainsPos(current, neighbour)) {
                    continue;
                }

                boolean rotation = neighbourAccessNeedsRotation(current, direction);
                int nextScore = 1 + (rotation ? 1000 : 0);

                if (neighbour.score > current.score + nextScore) {
                    neighbour.score = current.score + nextScore;
                    neighbour.previous = current;

                    if (rotation) {
                        current.score += 1000;
                    }

                    queue.add(neighbour);
                }
            }
        }
        throw new IllegalStateException("No path found");
    }

    private static List<Pos> getNeighbours(Map<Direction, Pos>[][] grid, Pos current) {
        List<Pos> neighbours = new ArrayList<>();

        if (current.direction == Direction.NORTH) {
            neighbours.add(grid[current.y][current.x].get(Direction.WEST));
            neighbours.add(grid[current.y][current.x].get(Direction.EAST));
            try {
                neighbours.add(grid[current.y - 1][current.x].get(Direction.NORTH));
            } catch (ArrayIndexOutOfBoundsException ignore) {}
        }

        try {
            neighbours.put("S", grid[current.y + 1][current.x]);
        } catch (Exception ignored) {}

        try {
            neighbours.put("E", grid[current.y][current.x + 1]);
        } catch (Exception ignored) {}

        try {
            neighbours.put("N", grid[current.y - 1][current.x]);
        } catch (Exception ignored) {}

        try {
            neighbours.put("W", grid[current.y][current.x - 1]);
        } catch (Exception ignored) {}

        return neighbours;
    }

    private static boolean neighbourAccessNeedsRotation(Pos current, String neighbourDirection) {
        // start pos, its direction is east
        if (current.previous == null) {
            return !neighbourDirection.equals("E");
        }

        Pos previous = current.previous;

        if (previous.x + 1 == current.x && previous.y == current.y) {
            // current direction is east
            return !neighbourDirection.equals("E");
        } else if (previous.x - 1 == current.x && previous.y == current.y) {
            // current direction is west
            return !neighbourDirection.equals("W");
        } else if (previous.x == current.x && previous.y - 1 == current.y) {
            // current direction is north
            return !neighbourDirection.equals("N");
        } else {
            // current direction is south
            return !neighbourDirection.equals("S");
        }
    }

    private static boolean isValidPathPos(Pos neighbour) {
        return !neighbour.type.equals("#");
    }

    private static boolean currentPathContainsPos(Pos current, Pos neighbour) {
        List<Pos> path = current.getPath();
        return path.contains(neighbour);
    }

    private static Pos findStartPos(Map<Direction, Pos>[][] grid) {
        for (Map<Direction, Pos>[] cols : grid) {
            for (Map<Direction, Pos> col : cols) {
                for (Map.Entry<Direction, Pos> directionPosEntry : col.entrySet()) {
                    if (directionPosEntry.getValue().type.equals("S") && directionPosEntry.getKey() == Direction.EAST) {
                        return directionPosEntry.getValue();
                    }
                }
            }
        }

        throw new IllegalStateException("No start pos found");
    }

    private static List<Pos> findEndPos(Map<Direction, Pos>[][] grid) {
        List<Pos> possibleEndPos = new ArrayList<>();
        for (Map<Direction, Pos>[] cols : grid) {
            for (Map<Direction, Pos> col : cols) {
                for (Map.Entry<Direction, Pos> directionPosEntry : col.entrySet()) {
                    if (directionPosEntry.getValue().type.equals("E")) {
                       possibleEndPos.add(directionPosEntry.getValue());
                    }
                }
            }
        }

        return possibleEndPos;
    }

    private static Map<Direction, Pos>[][] parseGrid(List<String> inputList) {
        Map<Direction, Pos>[][] grid = new Map[inputList.size()][inputList.getFirst().length()];

        for (int row = 0; row < inputList.size(); row++) {
            String[] cols = inputList.get(row).split("");
            for (int col = 0; col < cols.length; col++) {
                Map<Direction, Pos> posMap = new HashMap<>();
                posMap.put(Direction.NORTH, new Pos(col, row, cols[col], Direction.NORTH));
                posMap.put(Direction.EAST, new Pos(col, row, cols[col], Direction.EAST));
                posMap.put(Direction.SOUTH, new Pos(col, row, cols[col], Direction.SOUTH));
                posMap.put(Direction.WEST, new Pos(col, row, cols[col], Direction.WEST));
                grid[row][col] = posMap;
            }
        }

        return grid;
    }

    private static void printGrid(Pos[][] grid, List<Pos> path) {
        for (Pos[] pos : grid) {
            for (Pos po : pos) {
                if (po.type.equals("#")) {
                    System.out.print("#");
                } else if (path.contains(po)) {
                    System.out.print("X");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    public static class Pos implements Comparable<Pos> {
        int x;
        int y;
        String type;
        Direction direction;
        int score = Integer.MAX_VALUE;
        Pos previous;

        public Pos(int x, int y, String type, Direction direction) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.direction = direction;
        }

        List<Pos> getPath() {
            List<Pos> path = new ArrayList<>();
            Pos c = this;
            while (c != null) {
                path.add(c);
                c = c.previous;
            }
            return path;
        }

        @Override
        public int compareTo(Pos o) {
            return Integer.compare(this.score, o.score);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            Pos other = (Pos) obj;
            return Objects.equals(this.x, other.x) && Objects.equals(this.y, other.y) && Objects.equals(this.direction, other.direction);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y, this.direction);
        }
    }

    public enum Direction {
        NORTH, EAST, SOUTH, WEST;
    }
}
