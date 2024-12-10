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
        List<Block> blocks = parseBlocks(inputList);
        replaceBlocks(blocks);
        long part1HashCode = getHashCodeForBlocks(blocks);
        System.out.println("Part 1: " + part1HashCode + " in " + (System.currentTimeMillis() - startPart1) + "ms");

        long startPart2 = System.currentTimeMillis();
        List<File> files = parseFiles(inputList);
        replaceFiles(files);
        long part2HashCode = getHashCodeForFiles(files);
        System.out.println("Part 2: " + part2HashCode + " in " + (System.currentTimeMillis() - startPart2) + "ms");
    }

    private static long getHashCodeForBlocks(List<Block> blocks) {
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

    public static long getHashCodeForFiles(List<File> files) {
        long hash = 0;
        int index = 0;
        for (File f : files) {
            hash += f.calcHash(index);
            index += f.length;
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

    public static void replaceFiles(List<File> files) {
        List<File> reversedFiles = files.reversed();
        for (int i = 0; i < reversedFiles.size(); i++) {
            File currentFile = reversedFiles.get(i);
            if (!currentFile.isFreeSpace) {
                int indexOfFirstFreeFile = getIndexOfFirstFittingFreeFile(files, files.size() - i, currentFile.length);
                if (indexOfFirstFreeFile == -1) {
                    continue;
                }
                File freeFile = files.get(indexOfFirstFreeFile);
                freeFile.squash(currentFile);
                currentFile.free();
            }
        }
    }

    private static int getIndexOfFirstFittingFreeFile(List<File> files, int maxIndex, int fileSize) {
        for (int i = 0; i < maxIndex; i++) {
            File file = files.get(i);
            if (file.isFreeSpace() && file.getLength() >= fileSize) {
                return i;
            }
        }
        return -1;
    }

    public static List<Block> parseBlocks(List<String> input) {
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

    public static List<File> parseFiles(List<String> input) {
        List<File> files = new ArrayList<>();
        String[] blocksAsCompactedString = input.getFirst().split("");
        for (int i = 0; i < blocksAsCompactedString.length; i++) {
            File currentFile = new File();
            if (i % 2 == 0) {
                // file block
                currentFile.id = i / 2;
            } else {
                // free space block
                currentFile.isFreeSpace = true;
            }
            currentFile.length = Integer.parseInt(blocksAsCompactedString[i]);
            files.add(currentFile);
        }
        return files;
    }

    public static class Block {
        boolean isFreeSpace;
        int id;
    }

    public static class File {
        private boolean isFreeSpace;
        private int id;
        private int length;
        List<File> squashedFiles = new ArrayList<>();

        public boolean isFreeSpace() {
            return isFreeSpace && length > squashedFiles.stream().mapToInt(File::getLength).sum();
        }

        public int getLength() {
            if (squashedFiles.isEmpty()) {
                return length;
            } else {
                return length - squashedFiles.stream().mapToInt(File::getLength).sum();
            }
        }

        void free() {
            isFreeSpace = true;
            id = 0;
        }

        void squash(File f) {
            File s = new File();
            s.id = f.id;
            s.length = f.length;
            squashedFiles.add(s);
        }

        long calcHash(int index) {
            long hash = 0;
            if (!isFreeSpace) {
                for (int i = 0; i < length; i++) {
                    hash += (long) id * index;
                    index++;
                }
            } else {
                for (File s : squashedFiles) {
                    hash += s.calcHash(index);
                    index += s.length;
                }
            }
            return hash;
        }
    }
}
