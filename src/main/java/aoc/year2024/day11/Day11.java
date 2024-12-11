package aoc.year2024.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class Day11 {

    public static void main(String[] args) {
        //int[] example = new int[] {125, 17};
        long[] input = new long[] {965842, 9159, 3372473, 311, 0, 6, 86213, 48};

        List<Long> stones = Arrays.stream(input).boxed().collect(Collectors.toCollection(ArrayList::new));

        long startPart1 = System.currentTimeMillis();
        int moves = 25;
        for (int i = 0; i < moves; i++) {
            move(stones);
        }

        int numOfStones = stones.size();
        System.out.println("Part 1: " + numOfStones + " in " + (System.currentTimeMillis() - startPart1) + "ms");

        long startPart2 = System.currentTimeMillis();
        moves = 50;
        for (int i = 0; i < moves; i++) {
            System.out.println("Move " + i);
            move(stones);
        }

        numOfStones = stones.size();
        System.out.println("Part 2: " + numOfStones + " in " + (System.currentTimeMillis() - startPart2) + "ms");
    }

    private static void move(List<Long> stones) {
        ListIterator<Long> iterator = stones.listIterator();
        while (iterator.hasNext()) {
            Long stone = iterator.next();
            if (stone == 0) {
                iterator.set(1L);
            } else {
                String s = String.valueOf(stone);
                if (s.length() % 2 == 0) { // even
                    // split
                    String left = s.substring(0, s.length() / 2);
                    String right = s.substring(s.length() / 2);
                    iterator.set(Long.parseLong(left));
                    iterator.add(Long.parseLong(right));
                } else {
                    iterator.set(stone * 2024);
                }
            }
        }
    }
}
