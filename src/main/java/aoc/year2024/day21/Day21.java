package aoc.year2024.day21;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 {
    static Map<String, List<String>> numericInputSequences = new HashMap<>();
    static Map<String, List<String>> directionalInputSequences = new HashMap<>();
    static Map<String, Long> cache = new HashMap<>();

    public static void main(String[] args) {
        //List<String> codes = List.of("029A", "980A", "179A", "456A", "379A");
        List<String> codes = List.of("279A", "341A", "459A", "540A", "085A");

        numericInputSequences.put("A0", List.of("<", "A"));
        numericInputSequences.put("A1", List.of("<", "^", "<", "A"));
        numericInputSequences.put("A2", List.of("<", "^", "A"));
        numericInputSequences.put("A3", List.of("^", "A"));
        numericInputSequences.put("A4", List.of("^", "^", "<", "<", "A"));
        numericInputSequences.put("A5", List.of("<", "^", "^", "A"));
        //numericInputSequences.put("A9", List.of("^", "^", "^", "A"));
        numericInputSequences.put("1A", List.of(">", ">", "v", "A"));
        //numericInputSequences.put("17", List.of("^", "^", "A"));
        numericInputSequences.put("27", List.of("<", "^", "^", "A")); // check
        //numericInputSequences.put("29", List.of(">", "^", "^", "A"));
        numericInputSequences.put("34", List.of("<", "<", "^", "A")); // Check
        //numericInputSequences.put("37", List.of("<", "<", "^", "^", "A"));
        numericInputSequences.put("40", List.of(">", "v", "v", "A")); // check
        numericInputSequences.put("41", List.of("v", "A")); // check
        numericInputSequences.put("45", List.of(">", "A")); // check
        numericInputSequences.put("5A", List.of("v", "v", ">", "A")); // check
        numericInputSequences.put("54", List.of("<", "A")); // check
        numericInputSequences.put("56", List.of(">", "A")); // check
        numericInputSequences.put("59", List.of(">", "^", "A")); // check
        numericInputSequences.put("6A", List.of("v", "v", "A")); // check
        numericInputSequences.put("79", List.of(">", ">", "A")); // check
        numericInputSequences.put("80", List.of("v", "v", "v", "A")); // check
        numericInputSequences.put("85", List.of("v", "A")); // check
        numericInputSequences.put("9A", List.of("v", "v", "v", "A")); // check
        numericInputSequences.put("98", List.of("<", "A")); // check
        numericInputSequences.put("0A", List.of(">", "A")); // check
        numericInputSequences.put("02", List.of("^", "A")); // check
        numericInputSequences.put("08", List.of("^", "^", "^", "A")); // check

        directionalInputSequences.put("A<", List.of("v", "<", "<", "A"));
        directionalInputSequences.put("<A", List.of(">", ">", "^", "A"));
        directionalInputSequences.put("A^", List.of("<", "A"));
        directionalInputSequences.put("^A", List.of(">", "A"));
        directionalInputSequences.put("Av", List.of("<", "v", "A"));
        directionalInputSequences.put("vA", List.of(">", "^", "A"));
        directionalInputSequences.put("A>", List.of("v", "A"));
        directionalInputSequences.put(">A", List.of("^", "A"));
        directionalInputSequences.put("AA", List.of("A"));

        directionalInputSequences.put("v<", List.of("<", "A"));
        directionalInputSequences.put("<<", List.of("A"));
        directionalInputSequences.put(">>", List.of("A"));
        directionalInputSequences.put(">^", List.of("<", "^", "A"));
        directionalInputSequences.put("^^", List.of("A"));
        directionalInputSequences.put("^>", List.of("v", ">", "A"));
        directionalInputSequences.put("v>", List.of(">", "A"));
        directionalInputSequences.put("vv", List.of("A"));
        directionalInputSequences.put("<^", List.of(">", "^", "A"));
        directionalInputSequences.put("<v", List.of(">", "A"));
        directionalInputSequences.put("^<", List.of("v", "<", "A"));
        directionalInputSequences.put(">v", List.of("<", "A"));

        long start = System.currentTimeMillis();
        long totalComplexity = 0;
        for (String code : codes) {
            long complexity = enterCode(code);
            totalComplexity += complexity;
        }

        System.out.println("Part 1: " + totalComplexity + " in " + (System.currentTimeMillis() - start));

        // 127252 too high
        // 125092 too high
        // 123976 too high
        // 123096 ?
        // 121476 not correct
        // 119640 not correct
        // 176472888295110 too high
        // 176472888295110
        // 70499194631928 too low
    }

    private static long enterCode(String code) {
        //StringBuilder sb = new StringBuilder();
        cache.clear();
        Counter counter = new Counter();
        String currentPos = "A";
        for (String c : code.split("")) {
            numericInput(currentPos, c, counter);
            currentPos = c;
        }

        System.out.println(counter.getCount());
        return counter.getCount() * Integer.parseInt(code.substring(0, 3));
    }

    private static void numericInput(String current, String c, Counter counter) {
        List<String> strings = numericInputSequences.get(current + c);
        //System.out.print(current);
        if (strings == null) {
            throw new IllegalStateException(current + c);
        }
        String currentPos = "A";
        for (String s : strings) {
            robot(currentPos, s, counter, 1);
            currentPos = s;
        }
    }

    private static long robot(String current, String c, Counter counter, int depth) {
        String cacheKey = current + c + depth;
        if(cache.containsKey(cacheKey)) {
            Long countAdd = cache.get(cacheKey);
            counter.add(countAdd);
            return countAdd;
        } else {
            List<String> strings = directionalInputSequences.get(current + c);
            if (strings == null) {
                throw new IllegalStateException(current + c);
            }

            if (depth == 24) {
                cache.put(cacheKey, (long) strings.size());
                counter.add((long) strings.size());
                return strings.size();
            } else {
                String currentPos = "A";
                long total = 0;
                for (String s : strings) {
                    total += robot(currentPos, s, counter, depth + 1);
                    currentPos = s;
                }
                cache.put(cacheKey, total);
                return total;
            }
        }
    }

    private static void human(String c, Counter counter) {
        //System.out.print(c);
       // sb.append(c);
        counter.add();
    }

    public static class Counter {
        long count = 0L;

        void add() {
            count++;
        }

        void add(Long add) {
            count += add;
        }

        long getCount() {
            return count;
        }
    }
}
