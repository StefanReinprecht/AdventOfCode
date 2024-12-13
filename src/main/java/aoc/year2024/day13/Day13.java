package aoc.year2024.day13;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Day13 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day13_input.txt");

        List<ClawMachine> clawMachines = parseInput(inputList);

        solve(clawMachines, false);
        solve(clawMachines, true);
    }

    // linear algebra way
    // ax * s + bx * t = px  // * by
    // ay * s + by * t = py  // * bx
    //
    // ax * by * s + bx * by * t = px * by
    // ay * bx * s + by * bx * t = py * bx
    //
    // Subtract the equations:
    // ax * by * s - ay * bx * s = px * by - py * bx
    //
    // s = ( px * by - py * bx ) / ( ax * by - ay * bx)
    // t = ( px - ax * s) bx
    private static void solve(List<ClawMachine> clawMachines, boolean part2) {
        long totalTokens = 0;

        for (ClawMachine m : clawMachines) {

            long px = part2 ? m.px + 10000000000000L : m.px;
            long py = part2 ? m.py + 10000000000000L : m.py;

            double ca = (double) (px * m.by - py * m.bx) / (m.ax * m.by - m.ay * m.bx);
            double cb = (px - m.ax * ca) / m.bx;

            if ( ca % 1 == 0 && cb % 1 == 0) {
                if (part2 || (ca <= 100 && cb <= 100)) {
                    totalTokens += (long) (ca * 3 + cb);
                }
            }
        }

        System.out.println((part2 ? "Part 2: " : "Part 1: ") + totalTokens);
    }

    private static List<ClawMachine> parseInput(List<String> inputList) {
        List<ClawMachine> clawMachines = new ArrayList<>();
        for (int i = 0; i <= inputList.size(); i = i + 4) {
            String buttonALine = inputList.get(i);
            String buttonBLine = inputList.get(i+1);
            String prizeLine = inputList.get(i+2);

            String[] aLineSplit = buttonALine.split(" ");
            String[] bLineSplit = buttonBLine.split(" ");
            String[] prizeSplit = prizeLine.split(" ");

            int ax = Integer.parseInt(aLineSplit[2].substring(2, aLineSplit[2].length() - 1));
            int ay = Integer.parseInt(aLineSplit[3].substring(2));
            int bx = Integer.parseInt(bLineSplit[2].substring(2, bLineSplit[2].length() - 1));
            int by = Integer.parseInt(bLineSplit[3].substring(2));
            int px = Integer.parseInt(prizeSplit[1].substring(2, prizeSplit[1].length() - 1));
            int py = Integer.parseInt(prizeSplit[2].substring(2));

            clawMachines.add(new ClawMachine(ax, ay, bx, by, px, py));
        }

        return clawMachines;
    }

    public record ClawMachine (int ax, int ay, int bx, int by, long px, long py) {}
}
