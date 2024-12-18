package aoc.year2024.day17;

import java.util.List;

public class Day17Part2 {

    public static void main(String[] args) {
        List<Integer> input = List.of(2, 4, 1, 1, 7, 5, 1, 5, 0, 3, 4, 4, 5, 5, 3, 0);

        long result = find(input, 0);
        System.out.println("Part 2: " + result);
    }

    /**
     * 2,4 => b = a % 8
     * 1,1 => b = b ^ 1
     * 7,5 => c = a / 2 ^ b
     * 1,5 => b = b ^ 5
     * 0,3 => a = a / 8
     * 4,4 => b ^ c
     * 5,5 => prints b % 8
     * 3,0 => jump to 0 until a == 0
     */
    public static long find(List<Integer> program, long ans) {
        if (program.isEmpty()) {
            return ans;
        }

        for (int i = 0; i < 8; i++) {
            long a = ans * 8 + i; // At this point multiply instead of divide since we start from the back and calc forward
            long b = a % 8;
            b = b ^ 1;
            long c = (long) (a / Math.pow(2, b));
            b = b ^ 5;
            b = b ^ c;
            if (b % 8 == Long.valueOf(program.getLast())) {
                long sub = find(program.subList(0, program.size() - 1), a);
                if (sub == -1) {
                    continue;
                }
                return sub;
            }
        }
        return -1;
    }
}
