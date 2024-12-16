package aoc.year2024.day16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

import aoc.utils.Utils;

public class Day16 {

  public static void main(String[] args) {
    List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day16_example3.txt");

    long startPart1 = System.currentTimeMillis();
    Map<Pos, ReindeerPos> grid = parseGrid(inputList);
    ReindeerPos initialReindeerPos = getInitialReindeerPos(grid);
    ReindeerPos finalReindeerPos = getFinalReindeerPos(grid);
    List<List<ReindeerPos>> route = findRoute(initialReindeerPos, finalReindeerPos, grid);
    long part1Result = route.getFirst().getFirst().totalScore;
    printResult(inputList, route.getFirst());
    System.out.println("Part 1: " + part1Result + " in " + (System.currentTimeMillis() - startPart1) + "ms");
    // 143572 too high
  }

  private static void printResult(List<String> inputList, List<ReindeerPos> route) {
    String[][] grid = new String[inputList.size()][inputList.getFirst().length()];

    for (int i = 0; i < inputList.size(); i++) {
      String[] split = inputList.get(i).split("");
      for (int j = 0; j < split.length; j++) {
        grid[i][j] = split[j];
      }
    }

    for (int y = 0; y < grid[0].length; y++) {
      for (int x = 0; x < grid.length; x++) {
        int finalX = x;
        int finalY = y;
        if (grid[x][y].equals("S") || grid[x][y].equals("E")) {
          System.out.println(grid[x][y]);
        } else if (route.stream().anyMatch(reindeerPos -> reindeerPos.pos.x == finalX && reindeerPos.pos.y == finalY)) {
          System.out.print("X");
        } else {
          System.out.print(grid[x][y]);
        }
      }
      System.out.println();
    }
  }

  public static List<List<ReindeerPos>> findRoute(ReindeerPos from, ReindeerPos to, Map<Pos, ReindeerPos> grid) {
    List<List<ReindeerPos>> routes = new ArrayList<>();

    Map<ReindeerPos, ReindeerPos> allPos = new HashMap<>();
    Queue<ReindeerPos> queue = new PriorityQueue<>();

    queue.add(from);

    while (!queue.isEmpty()) {
      ReindeerPos next = queue.poll();
      if (next.equals(to)) {
        System.out.println("Found destination!");
        List<ReindeerPos> route = new ArrayList<>();
        ReindeerPos current = next;
        do {
          route.add(current);
          current = current.previous;
        } while (current != null);

        routes.add(route);
      }

      List<ReindeerPos> neighbors = getNeighbors(next, grid);
      neighbors.forEach(
          neighbor -> {
            allPos.put(neighbor, next);
            int nextNodeScore = 1 + (neighbor.direction.equals(next.direction) ? 0 : 1000);
            if (neighbor.totalScore > nextNodeScore + next.totalScore) {
              neighbor.totalScore = nextNodeScore + next.totalScore;
              neighbor.previous = next;
              queue.add(neighbor);
            }
          }
      );

    }

    return routes;
    //throw new IllegalArgumentException("Queue is empty, no finish reached");
  }

  public static List<ReindeerPos> getNeighbors(ReindeerPos reindeerPos, Map<Pos, ReindeerPos> grid) {
    List<ReindeerPos> neighbors = new ArrayList<>();

    int x = reindeerPos.pos.x;
    int y = reindeerPos.pos.y;

    // north
    ReindeerPos reindeerPosNorth = grid.get(new Pos(x, y + 1));
    if (reindeerPosNorth != null && !Objects.equals(reindeerPos.direction, "S") && !reindeerPosNorth.posType.equals("#")) {
      reindeerPosNorth.direction = "N";
      neighbors.add(reindeerPosNorth);
    }

    // south
    ReindeerPos reindeerPosSouth = grid.get(new Pos(x, y - 1));
    if (reindeerPosSouth != null && !Objects.equals(reindeerPos.direction, "N") && !reindeerPosSouth.posType.equals("#")) {
      reindeerPosSouth.direction = "S";
      neighbors.add(reindeerPosSouth);
    }

    // west
    ReindeerPos reindeerPosWest = grid.get(new Pos(x - 1, y));
    if (reindeerPosWest != null && !Objects.equals(reindeerPos.direction, "E") && !reindeerPosWest.posType.equals("#")) {
      reindeerPosWest.direction = "W";
      neighbors.add(reindeerPosWest);
    }

    // east
    ReindeerPos reindeerPosEast = grid.get(new Pos(x + 1, y));
    if (reindeerPosEast != null && !Objects.equals(reindeerPos.direction, "W") && !reindeerPosEast.posType.equals("#")) {
      reindeerPosEast.direction = "E";
      neighbors.add(reindeerPosEast);
    }

    return neighbors;
  }

  private static ReindeerPos getInitialReindeerPos(Map<Pos, ReindeerPos> grid) {
    Optional<ReindeerPos> startPos = grid.values().stream().filter(p -> p.posType.equals("S")).findFirst();
    if (startPos.isPresent()) {
      ReindeerPos sP = startPos.get();
      sP.direction = "E";
      sP.totalScore = 0;
      return sP;
    }
    throw new IllegalArgumentException("No initial reindeer position found");
  }

  private static ReindeerPos getFinalReindeerPos(Map<Pos, ReindeerPos> grid) {
    Optional<ReindeerPos> endPos = grid.values().stream().filter(p -> p.posType.equals("E")).findFirst();
    if (endPos.isPresent()) {
      return endPos.get();
    }
    throw new IllegalArgumentException("No initial reindeer position found");
  }

  private static Map<Pos, ReindeerPos> parseGrid(List<String> inputList) {
    Map<Pos, ReindeerPos> grid = new HashMap<>();

    for (int y = 0; y < inputList.size(); y++) {
      String line = inputList.get(y);
      String[] split = line.split("");
      for (int x = 0; x < split.length; x++) {
        Pos pos = new Pos(x, y);
        ReindeerPos reindeerPos = new ReindeerPos(pos, split[x]);
        grid.put(pos, reindeerPos);
      }
    }
    return grid;
  }

  public record Pos(int x, int y) {}

  public static class ReindeerPos implements Comparable<ReindeerPos> {

    public Pos pos;
    public String direction;
    public int totalScore = Integer.MAX_VALUE;
    public ReindeerPos previous;
    public String posType;

    public ReindeerPos(Pos pos, String posType) {
      this.pos = pos;
      this.posType = posType;
    }

    @Override
    public int compareTo(ReindeerPos o) {
      return Integer.compare(this.totalScore, o.totalScore);
    }

    @Override
    public boolean equals(Object obj) {
      ReindeerPos o = (ReindeerPos) obj;
      return Objects.equals(this.pos, o.pos);
    }
  }
}
