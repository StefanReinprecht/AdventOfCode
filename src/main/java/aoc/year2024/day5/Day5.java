package aoc.year2024.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import aoc.utils.Utils;

public class Day5 {

  public static void main(String[] args) {
    List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "input5_actual.txt");

    Map<Integer, List<Integer>> rulesList = new HashMap<>();
    List<List<Integer>> updates = new ArrayList<>();

    boolean processingRules = true;
    for (int i = 0; i < inputList.size(); i++) {
      if (inputList.get(i).equals("")) {
        processingRules = false;
        continue;
      }

      if (processingRules) {
        String[] rulesSplit = inputList.get(i).split("\\|");
        rulesList.computeIfAbsent(Integer.parseInt(rulesSplit[0]), k -> new ArrayList<>()).add(Integer.parseInt(rulesSplit[1]));
      } else {
        String[] updateSplit = inputList.get(i).split(",");
        updates.add(Arrays.stream(updateSplit).map(Integer::valueOf).toList());
      }
    }

    List<List<Integer>> faultyUpdates = new ArrayList<>();

    // Part 1
    long startPart1 = System.currentTimeMillis();
    int totalMiddleElements = 0;
    for (List<Integer> update : updates) {
      List<Integer> alreadyPrinted = new ArrayList<>();
      boolean success = true;
      for (int i = 0; i < update.size(); i++) {
        List<Integer> rules = rulesList.get(update.get(i));
        if (rules != null && alreadyPrinted.stream().anyMatch(rules::contains)) {
          success = false;
          faultyUpdates.add(new ArrayList<>(update));
          break;
        }
        alreadyPrinted.add(update.get(i));
      }

      if (success) {
        totalMiddleElements += update.get(update.size()/2);
      }
    }

    System.out.println("Part 1: " + totalMiddleElements + " in " + (System.currentTimeMillis() - startPart1) + "ms");

    // Part 2
    long startPart2 = System.currentTimeMillis();
    int totalMiddleElements2 = 0;
    for (List<Integer> faultyUpdate : faultyUpdates) {
      boolean isInRightOrder = false;
      main:
      while (!isInRightOrder) {
        List<Integer> alreadyPrinted = new ArrayList<>();
        for (int i = 0; i < faultyUpdate.size(); i++) {
          List<Integer> rules = rulesList.get(faultyUpdate.get(i));
          if (rules != null && alreadyPrinted.stream().anyMatch(rules::contains)) {
            Collections.swap(faultyUpdate, i, i - 1);
            continue main;
          }
          alreadyPrinted.add(faultyUpdate.get(i));
        }
        isInRightOrder = true;
        totalMiddleElements2 += faultyUpdate.get(faultyUpdate.size()/2);
      }
    }

    System.out.println("Part 2: " + totalMiddleElements2 + " in " + (System.currentTimeMillis() - startPart2) + "ms");
  }
}
