package aoc.year2024.day1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.utils.Utils;

public class Day1 {

  public static void main(String[] args) {
    List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "input1_actual.txt");

    List<Integer> left = new ArrayList<>();
    List<Integer> right = new ArrayList<>();

    inputList.forEach(i -> {
      String[] s1 = i.split("   ");
      left.add(Integer.parseInt(s1[0]));
      right.add(Integer.parseInt(s1[1]));
    });

    Collections.sort(left);
    Collections.sort(right);

    part1(left, right);

    part2(left, right);
  }

  private static void part2(List<Integer> left, List<Integer> right) {
    int totalSimilarity = 0;

    long start = System.currentTimeMillis();

    Map<Integer, Integer> rightMap = new HashMap<>();

    right.forEach(i -> rightMap.put(i, rightMap.getOrDefault(i, 0) + 1));

    for(int i = 0; i < left.size(); i++) {
      totalSimilarity += left.get(i) * rightMap.getOrDefault(left.get(i), 0);
    }

    long end = System.currentTimeMillis();

    System.out.println("Part 2: " + totalSimilarity);
    System.out.println(end - start);
  }

  private static void part1(List<Integer> left, List<Integer> right) {
    int totalDiff = 0;
    for (int i = 0; i < left.size(); i++) {
      int diff = Math.abs(left.get(i) - right.get(i));
      totalDiff += diff;
    }

    System.out.println("Part 1: " + totalDiff);
  }
}
