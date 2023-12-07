package aoc.year2023.day6;

import org.javatuples.Pair;

import java.util.List;

public class Day6 {
    public static void main(String[] args) {
        List<Pair<Long, Long>> input = List.of(
                new Pair<>(54L, 302L),
                new Pair<>(94L, 1476L),
                new Pair<>(65L, 1029L),
                new Pair<>(92L, 1404L)
        );

        Pair<Long, Long> input2 =
                new Pair<>(54946592L, 302147610291404L);

        long startPart1 = System.currentTimeMillis();
        long totalOptions = 0;
        for (Pair<Long, Long> race : input) {
            int options = 0;
            for (long i = 0; i <= race.getValue0(); i++) {
                long distance = i * (race.getValue0() - i);
                if (distance > race.getValue1()) {
                    options++;
                }
            }

            if (totalOptions == 0) {
                totalOptions = options;
            } else {
                totalOptions *= options;
            }
        }
        System.out.println("====");
        System.out.println("Answer 1: " + totalOptions);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart1));


        long startPart2 = System.currentTimeMillis();
        var options = 0;
        for (long i = 0; i <= input2.getValue0(); i++) {
            long d = i * (input2.getValue0() - i);
            if (d > input2.getValue1()) {
                options++;
            }
        }
        System.out.println("====");
        System.out.println("Answer 2 (brute-force, non optimized): " + options);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart2));

        /**
         * Improve performance by not checking every combination but going in steps until we do no longer match
         * Fill the remaining with one-by-one step combination checking
         *
         * BTW System.out.println is evil and costs a hell of performance
         */
        long startPart3 = System.currentTimeMillis();
        int stepSize = 40;
        options = 0;
        for (long i = (stepSize - 1); i < input2.getValue0(); i += stepSize) {
            long distance = i * (input2.getValue0() - i);
            if (distance > input2.getValue1()) {
                if (options > 0) {
                    options += stepSize;
                } else {
                    for (long j = i - (stepSize - 1); j <= i; j++) {
                        long distance2 = j * (input2.getValue0() - j);
                        if (distance2 > input2.getValue1()) {
                            options++;
                        }
                    }
                }
            } else {
                for (long j = i - (stepSize - 1); j <= i; j++) {
                    long distance2 = j * (input2.getValue0() - j);
                    if (distance2 > input2.getValue1()) {
                        options++;
                    } else {
                        break;
                    }
                }

                if (options > 0) {
                    break;
                }
            }
        }
        System.out.println("====");
        System.out.println("Answer 2 (brute-force, optimized): " + options);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart3));

        // Mathematical way (quadratic equation)
        long time = 54946592L;
        long distance = 302147610291404L;

        long startPart4 = System.currentTimeMillis();
        double a = (time - Math.sqrt(Math.pow(time, 2) - 4 * distance)) / 2;
        double b = (time + Math.sqrt(Math.pow(time, 2) - 4 * distance)) / 2;

        double answer = Math.floor(b) - Math.ceil(a) + 1;
        System.out.println("====");
        System.out.println("Answer 2 (math formula): " + (int) answer);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart4));

    }
}
