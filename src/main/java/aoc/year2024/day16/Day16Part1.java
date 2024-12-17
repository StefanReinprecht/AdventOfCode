package aoc.year2024.day16;

import aoc.utils.Utils;

import java.util.*;

public class Day16Part1 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day16_input.txt");

        Map<Direction, Pos>[][] grid = parseGrid(inputList);
        Pos startPos = findStartPos(grid);
        List<Pos> endPos = findEndPos(grid);

        findPath(grid, startPos, endPos);
    }

    private static void findPath(Map<Direction, Pos>[][] grid, Pos startPos, List<Pos> endPos) {
        startPos.score = 0;

        Queue<Pos> queue = new PriorityQueue<>();

        queue.add(startPos);

        while(!queue.isEmpty()) {
            Pos current = queue.poll();

            if (endPos.contains(current)) {
                System.out.println("Part 1: " + current.score);
                return;
            }

            List<Pos> neighbours = getNeighbours(grid, current);
            for (Pos neighbour : neighbours) {
                if(!isValidPathPos(neighbour) || neighbour.equals(current.previous) || currentPathContainsPos(current, neighbour)) {
                    continue;
                }

                boolean rotation = neighbourAccessNeedsRotation(current, neighbour);
                int nextScore = (rotation ? 0 : 1) + (rotation ? 1000 : 0);

                if (neighbour.score > current.score + nextScore) {
                    neighbour.score = current.score + nextScore;
                    neighbour.previous = current;

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

        if (current.direction == Direction.EAST) {
            neighbours.add(grid[current.y][current.x].get(Direction.NORTH));
            neighbours.add(grid[current.y][current.x].get(Direction.SOUTH));
            try {
                neighbours.add(grid[current.y][current.x + 1].get(Direction.EAST));
            } catch (ArrayIndexOutOfBoundsException ignore) {}
        }

        if (current.direction == Direction.SOUTH) {
            neighbours.add(grid[current.y][current.x].get(Direction.EAST));
            neighbours.add(grid[current.y][current.x].get(Direction.WEST));
            try {
                neighbours.add(grid[current.y + 1][current.x].get(Direction.SOUTH));
            } catch (ArrayIndexOutOfBoundsException ignore) {}
        }

        if (current.direction == Direction.WEST) {
            neighbours.add(grid[current.y][current.x].get(Direction.SOUTH));
            neighbours.add(grid[current.y][current.x].get(Direction.NORTH));
            try {
                neighbours.add(grid[current.y][current.x - 1].get(Direction.WEST));
            } catch (ArrayIndexOutOfBoundsException ignore) {}
        }

        return neighbours;
    }

    private static boolean neighbourAccessNeedsRotation(Pos current, Pos neighbour) {
        return current.x == neighbour.x && current.y == neighbour.y && current.direction != neighbour.direction;
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
        NORTH, EAST, SOUTH, WEST
    }
}
