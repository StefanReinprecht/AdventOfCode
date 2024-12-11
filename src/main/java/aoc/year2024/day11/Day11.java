package aoc.year2024.day11;

import java.util.HashMap;
import java.util.Map;

public class Day11 {

  public static void main(String[] args) {
    Map<Long, Long> stones = new HashMap<>();
    stones.put(965842L, 1L);
    stones.put(9159L, 1L);
    stones.put(3372473L, 1L);
    stones.put(311L, 1L);
    stones.put(0L, 1L);
    stones.put(6L, 1L);
    stones.put(86213L, 1L);
    stones.put(48L, 1L);

    long start = System.currentTimeMillis();
    for (int i = 0; i < 75; i++) {
      Map<Long, Long> nextStones = new HashMap<>();
      for (Long stone : stones.keySet()) {
        if (stone == 0) {
          nextStones.merge(1L, stones.get(stone), Long::sum);
        } else {
          String s = String.valueOf(stone);
          if (s.length() % 2 == 0) {
            Long left = Long.parseLong(s.substring(0, s.length() / 2));
            Long right = Long.parseLong(s.substring(s.length() / 2));
            nextStones.merge(left, stones.get(stone), Long::sum);
            nextStones.merge(right, stones.get(stone), Long::sum);
          } else {
            nextStones.merge(stone * 2024, stones.get(stone), Long::sum);
          }
        }
      }
      stones = nextStones;
    }

    long sum = stones.values().stream().mapToLong(Long::valueOf).sum();
    System.out.println("Part: " + sum + " in " + (System.currentTimeMillis() - start) + "ms");
  }
}
