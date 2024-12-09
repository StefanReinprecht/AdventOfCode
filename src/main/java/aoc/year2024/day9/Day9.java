package aoc.year2024.day9;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

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

        List<File> reversedFilesList = files.reversed();
        ListIterator<File> fileListIterator = files.listIterator();
        while(fileListIterator.hasNext()) {
            File f = fileListIterator.next();

            if (f.isFreeSpace) {
                Optional<File> nextToMove = reversedFilesList.stream().filter(fi -> !fi.isFreeSpace && !fi.isDefragged).findFirst();
                if (nextToMove.isPresent()) {
                    if (f.length > nextToMove.get().length) {
                        // split block
                        f.id = nextToMove.get().id;
                        fileListIterator.add(new File(true, f.length - nextToMove.get().length));
                    } if (f.length < nextToMove.get().length) {
                        f.id = nextToMove.get().id;
                        nextToMove.get().length = nextToMove.get().length - f.length;
                    } else {
                      // move last block and set defragged
                        f.id = nextToMove.get().id;
                        nextToMove.get().isDefragged = true;
                    }
                } else {
                    break;
                }
            }
        }

        int hashedSum = files.stream().mapToInt(File::getFileHash).sum();
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

        public int getFileHash() {
            if (isDefragged) {
                return 0;
            }

            return length * id;
        }
    }
}
