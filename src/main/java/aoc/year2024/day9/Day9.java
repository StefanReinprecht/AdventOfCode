package aoc.year2024.day9;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Day9 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day9_input.txt");

        long startPart1 = System.currentTimeMillis();
        List<Block> blocks = parseInput(inputList);

        replaceBlocks(blocks); // <- slow

        long hashCode = getHashCode(blocks);
        System.out.println("Part 1: " + hashCode + " in " + (System.currentTimeMillis() - startPart1));
    }

    private static long getHashCode(List<Block> blocks) {
        long hash = 0;
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block.isFreeSpace) {
                break;
            }
            hash += (long) block.id * i;
        }
        return hash;
    }

    private static void replaceBlocks(List<Block> blocks) {
        List<Block> reversedBlocks = blocks.reversed();
        int indexOfFirstFreeBlock = 0;
        for (int i = 0; i < reversedBlocks.size(); i++) {
            Block currentBlock = reversedBlocks.get(i);
            if (!currentBlock.isFreeSpace) {
                indexOfFirstFreeBlock = getIndexOfFirstFreeBlock(blocks, indexOfFirstFreeBlock, blocks.size() - i);
                if (indexOfFirstFreeBlock == -1) {
                    break;
                }
                Collections.swap(blocks, indexOfFirstFreeBlock, blocks.size() - (i + 1));
            }
        }
    }

    private static int getIndexOfFirstFreeBlock(List<Block> blocks, int lastFirstIndex, int maxIndex) {
        for (; lastFirstIndex < maxIndex; lastFirstIndex++) {
            if (blocks.get(lastFirstIndex).isFreeSpace) {
                return lastFirstIndex;
            }
        }
        return -1;
    }

    public static List<Block> parseInput(List<String> input) {
        List<Block> blocks = new ArrayList<>();
        String[] blocksAsCompactedString = input.getFirst().split("");
        for (int i = 0; i < blocksAsCompactedString.length; i++) {
            Block currentBlock = new Block();
            if (i % 2 == 0) {
                // file block
                currentBlock.id = i / 2;
            } else {
                // free space block
                currentBlock.isFreeSpace = true;
            }
            IntStream.range(0, Integer.parseInt(blocksAsCompactedString[i])).forEach(a -> blocks.add(currentBlock));
        }
        return blocks;
    }

    public static class Block {
        boolean isFreeSpace;
        int id;
    }
}
