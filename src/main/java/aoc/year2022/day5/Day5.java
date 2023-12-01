package aoc.year2022.day5;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Day5 {
    public static void main(String[] args) throws Exception {
        List<String> inputList = Utils.readAllLinesFromStream(Day5.class.getResource("input5.txt").toURI());

        List<String> moveInputs = new ArrayList<>();

        boolean stackPositionsProcessed = false;
        for (String input : inputList) {
            if (input.equals("")) {
                stackPositionsProcessed = true;
                continue;
            }

            if (stackPositionsProcessed) {
                moveInputs.add(input);
            }
        }

        List<String> stackInputs = Arrays.asList("SCVN", "ZMJHNS", "MCTGJND", "TDFJWRM", "PFH", "CTZHJ", "DPRQFSLZ", "CSLHDFPW", "DSMPFNGZ");

        List<Stack<String>> stacks = processStackInputs(stackInputs);
        processMoves(stacks, moveInputs, false);
        String resultOld = getResult(stacks);

        stacks = processStackInputs(stackInputs);
        processMoves(stacks, moveInputs, true);
        String resultNew = getResult(stacks);

        System.out.println("===");
        System.out.println(resultOld);
        System.out.println(resultNew);
        // CNSZFDVLJ
    }

    public static List<Stack<String>> processStackInputs(List<String> stackInputs) {
        List<Stack<String>> stacks = new ArrayList<>();
        for (String stackInput : stackInputs) {
            String[] singleLetters = stackInput.split("(?!^)");
            Stack<String> s = new Stack<>();
            for (String letter : singleLetters) {
                s.push(letter);
            }
            stacks.add(s);
        }
        return stacks;
    }

    public static void processMoves(List<Stack<String>> stacks, List<String> moves, boolean method) {

        for (String move : moves) {
            processMove(stacks, move, method);
        }
    }

    public static void processMove(List<Stack<String>> stack, String move, boolean method) {
        int sourceStack = getSourceStack(move);
        int targetStack = getTargetStack(move);

        System.out.println(sourceStack + " to " + targetStack);

        if (!method) {
            for (int i = 0; i < numberOfItemsToMove(move); i++) {
                String item = stack.get(sourceStack - 1).pop();
                System.out.println("Move " + item);
                stack.get(targetStack - 1).push(item);
            }
        } else {
            Stack<String> tempStore = new Stack<>();
            for (int i = 0; i < numberOfItemsToMove(move); i++) {
                String item = stack.get(sourceStack - 1).pop();
                tempStore.push(item);
            }

            for (int i = 0; i < numberOfItemsToMove(move); i++) {
                stack.get(targetStack - 1).push(tempStore.pop());
            }
        }
    }

    public static int numberOfItemsToMove(String move) {
        // e.g. move 1 from 1 to 2
        return Integer.parseInt(move.split(" ")[1]);
    }

    public static int getSourceStack(String move) {
        // e.g. move 1 from 1 to 2
        return Integer.parseInt(move.split(" ")[3]);
    }

    public static int getTargetStack(String move) {
        // e.g. move 1 from 1 to 2
        return Integer.parseInt(move.split(" ")[5]);
    }

    public static String getResult(List<Stack<String>> stack) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < stack.size(); i++) {
            String topItem = stack.get(i).pop();
            builder.append(topItem);
        }
        return builder.toString();
    }
}
