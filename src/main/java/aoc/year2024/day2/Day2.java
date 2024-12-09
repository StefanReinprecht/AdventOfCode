package aoc.year2024.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import aoc.utils.Utils;


public class Day2 {

  public static void main(String[] args) {
    List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "input2_actual.txt");

    int countSafePlans = 0;
    int countSafePlansWithDampener = 0;
    for (String line : inputList) {
      List<Integer> levels = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
      if (isSafePlan(levels)) {
        countSafePlans++;
      } else {
        for (int i = 0; i < levels.size(); i++) {
          List<Integer> newLevels = new ArrayList<>(levels);
          newLevels.remove(i);
          if (isSafePlan(newLevels)) {
            countSafePlansWithDampener++;
            break;
          }
        }
      }
    }

    System.out.println("Part 1: " + countSafePlans); // 411
    System.out.println("+++++");
    System.out.println("Part 2: " + (countSafePlans + countSafePlansWithDampener)); // 443 // 449 // 446 // 465
  }

  public static boolean isSafePlan(List<Integer> levels) {
    boolean mustDecrease = levels.get(0) > levels.get(1);

    for (int i = 0; i < levels.size() - 1; i++) {
      if (Objects.equals(levels.get(i), levels.get(i + 1))) {
        return false;
      }

      if (mustDecrease) {
        if (levels.get(i) <= levels.get(i + 1)) {
          return false;
        }
      } else {
        if (levels.get(i) >= levels.get(i + 1)) {
          return false;
        }
      }

      int diff = Math.abs(levels.get(i) - levels.get(i + 1));
      if (!(0 < diff && diff < 4)) {
        return false;
      }
    }

    return true;
  }

}
