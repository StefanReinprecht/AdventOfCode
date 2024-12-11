package aoc.year2024.day11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11_RecusiveWithLookupTable {
  public static void main(String[] args) {
    int[] example = new int[] {125, 17};
    //long[] input = new long[] {965842, 9159, 3372473, 311, 0, 6, 86213, 48};

    Map<Long, List<Long>> evenOddLookupTable = new HashMap<>();

    long start = System.currentTimeMillis();
    List<Long> totalStones = new ArrayList<>();
    for (int i = 0; i < example.length; i++) {
      ArrayList<Long> stones = new ArrayList<>();
      sim(example[i], 40, stones, evenOddLookupTable);
      //System.out.println(stones);
      totalStones.addAll(stones);
    }

    System.out.println("Part 1: " + totalStones.size() + " in " + (System.currentTimeMillis() - start) + "ms");
  }

  private static void sim(long stone, int movesLeft, Collection<Long> stones, Map<Long, List<Long>> evenOddLookupTable) {
    if (movesLeft == 0) {
      stones.add(stone);
      return;
    }
    if (stone == 0) {
      //stones.add(stone);
      sim(1, movesLeft - 1, stones, evenOddLookupTable);
    } else {
      if (evenOddLookupTable.containsKey(stone)) {
        List<Long> longs = evenOddLookupTable.get(stone);
        if (longs.size() == 1) {
          sim(longs.getFirst(), movesLeft - 1, stones, evenOddLookupTable);
        } else {
          sim(longs.get(0), movesLeft - 1, stones, evenOddLookupTable);
          sim(longs.get(1), movesLeft - 1, stones, evenOddLookupTable);
        }
      } else {
        String s = String.valueOf(stone);
        if (s.length() % 2 == 0) { // even
          // split
          Long left = Long.parseLong(s.substring(0, s.length() / 2));
          Long right = Long.parseLong(s.substring(s.length() / 2));
          evenOddLookupTable.put(stone, List.of(left, right));
          sim(left, movesLeft - 1, stones, evenOddLookupTable);
          sim(right, movesLeft - 1, stones, evenOddLookupTable);
        } else {
          Long stoneVal = stone * 2024;
          evenOddLookupTable.put(stone, List.of(stoneVal));
          sim(stoneVal, movesLeft - 1, stones, evenOddLookupTable);
        }
      }
    }
  }
}
