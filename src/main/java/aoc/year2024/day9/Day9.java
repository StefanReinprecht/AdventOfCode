package aoc.year2024.day9;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day9 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day9_example.txt");

        // parse input
        List<File> files = new ArrayList<>();
        String[] compactFiles = inputList.getFirst().split("");
        boolean isFile = true;
        int idCounter = 0;
        for(String file : compactFiles) {
            int fileLength = Integer.parseInt(file);
            if (isFile) {
                files.add(new File(idCounter, fileLength));
                idCounter++;
            } else {
                if (fileLength != 0) {
                    files.add(new File(true, fileLength));
                }
            }
            isFile = !isFile;
        }

        System.out.println(files.size());

        String wholeLine = files.stream().map(File::toString).collect(Collectors.joining());

        List<File> reversedFilesList = files.reversed();
        ListIterator<File> fileListIterator = files.listIterator();
        boolean usePrevious = false;
        while(fileListIterator.hasNext()) {
            File currentFile;
            if (usePrevious) {
                currentFile = fileListIterator.previous();
                usePrevious = false;
            } else {
                currentFile = fileListIterator.next();
            }

            if (currentFile.isDefragged) {
                break;
            } else if (currentFile.isFreeSpace) {
                Optional<File> nextToMove = reversedFilesList.stream().filter(file -> !file.isFreeSpace && !file.isDefragged).findFirst();
                if (nextToMove.isPresent()) {
                    File nextToMoveFile = nextToMove.get();
                    if (currentFile.length > nextToMoveFile.length) {
                        // split block
                        currentFile.id = nextToMoveFile.id;
                        nextToMoveFile.isDefragged = true;
                        nextToMoveFile.isFreeSpace = true;
                        fileListIterator.add(new File(true, currentFile.length - nextToMoveFile.length));
                        usePrevious = true;
                        currentFile.length = nextToMoveFile.length;
                    } else if (currentFile.length < nextToMoveFile.length) {
                        currentFile.id = nextToMoveFile.id;
                        nextToMoveFile.length = nextToMoveFile.length - currentFile.length;
                    } else {
                        // move last block and set defragged
                        currentFile.id = nextToMoveFile.id;
                        nextToMoveFile.isDefragged = true;
                        nextToMoveFile.isFreeSpace = true;
                    }
                    currentFile.isFreeSpace = false;
                } else {
                    break;
                }

            } else {
                currentFile.isDefragged = true;
            }
            wholeLine = files.stream().map(File::toString).collect(Collectors.joining());
            System.out.println(wholeLine);
        }

        int hashedSum = 0;
        String[] split = wholeLine.split("");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals(".")) {
                continue;
            }
            hashedSum += Integer.parseInt(split[i]) * i;
        }

        System.out.println(wholeLine);
        // 009981118872766333545554 my result
        // 0099811188827773336446555566 expected result
        // 009981118882777333644655556
        // 00998111888277733364465555666777.88899
        // 00998111888277733364465555.66.......99

        System.out.println("Result 1: " + hashedSum);
    }

    public static class File {
        boolean isFreeSpace;
        int id;
        int length;
        boolean isDefragged;

        public File(boolean isFreeSpace, int length) {
            this.isFreeSpace = isFreeSpace;
            this.length = length;
        }

        public File(int id, int length) {
            this.id = id;
            this.length = length;
        }

        @Override
        public String toString() {
            if (isFreeSpace ) {
                return ".".repeat(length);
            } else if (isDefragged) {
                return new String(id + "").repeat(length);
                //return "D".repeat(length);
            }
            return new String(id + "").repeat(length);
        }
    }
}
