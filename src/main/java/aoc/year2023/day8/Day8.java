package aoc.year2023.day8;

import aoc.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Day8 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input8.txt");

        String[] moveInstructions = inputList.get(0).split("");

        Map<String, Node> nodeMap = inputList.stream().skip(2).map(Node::create).collect(Collectors.toMap(Node::name, node -> node));

        long startPart1 = System.currentTimeMillis();
        Node currentNode = nodeMap.get("AAA");
        long moveCounter = 0;
        while (!currentNode.name.equals("ZZZ")) {
            for (String move : moveInstructions) {
                if ("L".equals(move)) {
                    currentNode = nodeMap.get(currentNode.left);
                } else if ("R".equals(move)) {
                    currentNode = nodeMap.get(currentNode.right);
                }
                moveCounter++;
            }
        }

        System.out.println("Answer 1: " + moveCounter);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart1));
        System.out.println("=======");

        long startPart2 = System.currentTimeMillis();
        List<Node> currentNodes = nodeMap.keySet().stream().filter(k -> k.endsWith("A")).map(nodeMap::get).toList();

        List<Long> moveCounters = new ArrayList<>();
        /*
         * Instead of playing the left/right game until all nodes are at the same time on a node ending with 'Z',
         * we run the game for each node and afterward calculate the lcm for all numbers.
         */
        for (Node node : currentNodes) {
            moveCounter = 0;
            while (!node.name.endsWith("Z")) {
                for (String move : moveInstructions) {
                    if ("L".equals(move)) {
                        node = nodeMap.get(node.left);
                    } else if ("R".equals(move)) {
                        node = nodeMap.get(node.right);
                    }
                    moveCounter++;
                }
            }
            moveCounters.add(moveCounter);
        }

        long lcm = 1;
        for (long moves : moveCounters) {
            lcm = lcm(moves, lcm);
        }

        System.out.println("Answer 2: " + lcm);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart2));
    }

    public record Node(String name, String left, String right) {

        public static Node create(String input) {
            String[] split = input.split(" = ");

            return new Node(split[0], split[1].substring(1, 4), split[1].substring(6, 9));
        }
    }

    private static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }
}
