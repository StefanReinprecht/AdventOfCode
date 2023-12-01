package main.java.aoc.y2023.day6;

import main.java.aoc.y2023.day5.Day5;
import main.java.aoc.y2023.utils.Utils;

import java.io.Serializable;
import java.util.*;

public class Day6 {
    public static void main(String[] args) throws Exception {
        List<String> inputList = Utils.readAllLinesFromStream(Day6.class.getResource("input6.txt").toURI());

        String[] singleLetters = inputList.get(0).split("(?!^)");

        boolean markerFound = false;
        for (int i = 4; i < singleLetters.length; i++) {

            if (!markerFound) {
                List<String> markersToCheck = new ArrayList<>();
                for (int marker = 0; marker < 4; marker++) {
                    markersToCheck.add(singleLetters[i - marker]);
                }
                if (markersToCheck.stream().distinct().count() == 4) {
                    System.out.println("Marker = " + (i + 1));
                    markerFound = true;
                } else {
                    markersToCheck.clear();
                }
            }

            if (i < 14) {
                continue;
            }

            List<String> msgToCheck = new ArrayList<>();
            for (int msg = 0; msg < 14; msg++) {
                msgToCheck.add(singleLetters[i - msg]);
            }
            if (msgToCheck.stream().distinct().count() == 14) {
                System.out.println("Msg = " + (i + 1));
                break;
            }
        }
    }
}
