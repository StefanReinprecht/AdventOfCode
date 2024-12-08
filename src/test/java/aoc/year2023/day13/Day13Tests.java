package aoc.year2023.day13;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day13Tests {

    @Test
    public void test1() {
        List<String> input = Arrays.asList(
                "#.##..##.",
                "..#.##.#.",
                "##......#",
                "##......#",
                "..#.##.#.",
                "..##..##.",
                "#.#.##.#."
        );

        Puzzle p = new Puzzle(input);

        assertEquals(5, p.getPart1Value());
    }

    @Test
    public void test2() {
        List<String> input = Arrays.asList(
                "#...##..#",
                "#....#..#",
                "..##..###",
                "#####.##.",
                "#####.##.",
                "..##..###",
                "#....#..#"
        );

        Puzzle p = new Puzzle(input);

        assertEquals(400, p.getPart1Value());
    }

    @Test
    public void test3() {
        List<String> input = Arrays.asList(
                "####....####.#.##",
                ".....#..#.#...#.#",
                "#......##..#.###.",
                "...####.#.##.#...",
                "###...##.#..#.###",
                "###..###.#..#.###",
                "...####.#.##.#...",
                ".#..#......#####.",
                ".#..#......#####.",
                "...####.#.##.#...",
                "###..###.#..#.###",
                "###...##.#..#.###",
                "...####.#.##.#..."
        );

        Puzzle p = new Puzzle(input);

        assertEquals(800, p.getPart1Value());
    }

    @Test
    public void test4() {
        List<String> input = Arrays.asList(
            "...#..#",
            "...#..#",
            "##..##.",
            ".#.##.#",
            "..#..##",
            "#.#.##.",
            "#.#.###",
            "##.##..",
            "##.##..",
            "#.#.###",
            "..#.##.",
            "..#..##",
            ".#.##.#",
            "##..##.",
            "...#..#"
        );

        Puzzle p = new Puzzle(input);

        assertEquals(800, p.getPart1Value());
    }

    @Test
    public void test5() {
        List<String> input = Arrays.asList(
                ".##...#.##.",
                "....#..#..#",
                "##..##..##.",
                "##.#..#.#..",
                "###..#.####",
                "....#..####",
                "....#..####",
                "###..#.####",
                "##.#..#.#.."

        );

        Puzzle p = new Puzzle(input);

        assertEquals(600, p.getPart1Value());
    }

    @Test
    public void test6() {
        List<String> input = Arrays.asList(
            ".#.####.#.#......",
            "###....#####.####",
            "..#.##.#..##..##.",
            ".#..##..#...#....",
            "....##....##.####",
            ".#.####.#.######.",
            "##########....##.",
            "..#....#...##....",
            "#..#..#..##.#####",
            "#........###..##.",
            ".##....##....####",
            "##.####.###.#.##.",
            "###.##.####.#....",
            ".##.##.##.....##.",
            "..#.##.#...##....",
            "##......###..####",
            ".#......#.###.##."
        );

        Puzzle p = new Puzzle(input);

        assertEquals(5, p.getPart1Value());
    }

    @Test
    public void test7() {
        List<String> input = Arrays.asList(
                "###.####.######.#",
                "#.#......#.##.#..",
                ".#..####..#..#..#",
                "#....##....##....",
                "...#....#........",
                "#..#.##.#..##..#.",
                ".#..####..#..#..#",
                "#.###..###.##.###",
                "###.####.######.#",
                "#.#..##..#.##.#..",
                "#..##..##..##..##"
        );

        Puzzle p = new Puzzle(input);

        assertEquals(6, p.getPart1Value());
    }

    @Test
    public void test8() {
        List<String> input = Arrays.asList(
                "####.###..##.",
                ".#.#.#..####.",
                ".#.#.#..####.",
                "####.###..##.",
                "###..###.###.",
                ".#.####..#.##",
                "#.#.#.##.#.#.",
                "#..#.###..#..",
                "#.....######.",
                "#....##.#..#.",
                ".####..#.....",
                ".........#...",
                ".###.#..##.##",
                "..####.#..#..",
                "..#..#.#..#.#",
                ".####..#.##.#",
                ".###...#.##.#"
        );

        Puzzle p = new Puzzle(input);

        assertEquals(2, p.getPart1Value());
    }
}
