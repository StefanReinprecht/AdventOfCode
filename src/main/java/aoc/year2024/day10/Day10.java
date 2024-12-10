package aoc.year2024.day10;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.Utils;

public class Day10 {

  public static void main(String[] args) {
    List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day10_input.txt");

    long startPart1 = System.currentTimeMillis();
    Coord[][] coords = buildGrid(inputList);

    System.out.println(coords.length);

    List<Coord> trailheads = findTrailheads(coords);

    long totalScore = 0;
    for (Coord trailhead : trailheads) {
      List<Coord> endpoints = new ArrayList<>();
      findRoute(trailhead, coords, endpoints);
      long uniqueEndpoints = endpoints.stream().distinct().count();
      //System.out.println("Score for trailhead " + trailhead + ": " + uniqueEndpoints);
      totalScore += uniqueEndpoints;
    }

    System.out.println("Part 1: " + totalScore + " in " + (System.currentTimeMillis() - startPart1) + " ms");
  }

  private static void findRoute(Coord trailhead, Coord[][] coords, List<Coord> endpoints) {
    if (trailhead.level == 9) {
      endpoints.add(trailhead);
      return;
    }

    // check top
    try {
      Coord topCoord = coords[trailhead.xCoord][trailhead.yCoord - 1];
      if (topCoord.level == (trailhead.level + 1)) {
        findRoute(topCoord, coords, endpoints);
      }
    } catch (ArrayIndexOutOfBoundsException ignored) {}

    // check right
    try {
      Coord topCoord = coords[trailhead.xCoord + 1][trailhead.yCoord];
      if (topCoord.level == (trailhead.level + 1)) {
        findRoute(topCoord, coords, endpoints);
      }
    } catch (ArrayIndexOutOfBoundsException ignored) {}

    // check bottom
    try {
      Coord topCoord = coords[trailhead.xCoord][trailhead.yCoord + 1];
      if (topCoord.level == (trailhead.level + 1)) {
        findRoute(topCoord, coords, endpoints);
      }
    } catch (ArrayIndexOutOfBoundsException ignored) {}

    // check left
    try {
      Coord topCoord = coords[trailhead.xCoord - 1][trailhead.yCoord];
      if (topCoord.level == (trailhead.level + 1)) {
        findRoute(topCoord, coords, endpoints);
      }
    } catch (ArrayIndexOutOfBoundsException ignored) {}
  }

  private static List<Coord> findTrailheads(Coord[][] coords) {
    List<Coord> trailheads = new ArrayList<>();
    for (Coord[] row : coords) {
      for (Coord coord : row) {
        if (coord.level == 0) {
          trailheads.add(coord);
        }
      }
    }
    return trailheads;
  }

  private static Coord[][] buildGrid(List<String> inputList) {
    Coord[][] grid = new Coord[inputList.getFirst().length()][inputList.size()];

    for (int y = 0; y < inputList.size(); y++) {
      for (int x = 0; x < inputList.get(y).length(); x++) {
        char c = inputList.get(y).charAt(x);
        grid[x][y] = new Coord(x, y, c - '0');
      }
    }

    return grid;
  }

  public static class Coord {

    private final int xCoord;
    private final int yCoord;
    private final int level;

    public Coord(int xCoord, int yCoord, int level) {
      this.xCoord = xCoord;
      this.yCoord = yCoord;
      this.level = level;
    }

    @Override
    public String toString() {
      return "[" + xCoord + ":" + yCoord + "]";
    }
  }
}