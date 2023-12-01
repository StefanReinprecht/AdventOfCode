package aoc.year2022.day7;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day7 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2022, "input7.txt");

        List<Dir> allDirs = new ArrayList<>();
        Dir currentDir = null;

        for (String input : inputList) {
            // lines starting with $ are commands
            String[] split = input.split(" ");
            if (split[0].startsWith("$")) {
                // cd command
                if (split[1].equals("cd")) {

                    if (split[2].equals("..")) {
                        // go one level up
                        assert currentDir != null;
                        currentDir = currentDir.getParentDir();
                    } else {
                        Dir dir = new Dir(split[2]);
                        dir.setParentDir(currentDir);
                        if (currentDir != null) { // to handle root
                            currentDir.addChildDir(dir);
                        }
                        allDirs.add(dir);
                        currentDir = dir;
                    }

                    System.out.println(input);
                    System.out.println("Current dir: " + currentDir.getPath());
                }
            } else {
                if (!split[0].equals("dir")) {
                    File file = new File(Integer.parseInt(split[0]));
                    assert currentDir != null;
                    currentDir.addFile(file);
                }
            }
        }

        int sizeOfSmallDirs = 0;
        for (Dir dir : allDirs) {
            int diskSpace = dir.getDiskSpace();
            System.out.println(dir.getName() + " : " + diskSpace);

            if (diskSpace < 100_000) {
                sizeOfSmallDirs += diskSpace;
            }
        }

        System.out.println("================");
        System.out.println("Answer 1: " + sizeOfSmallDirs);
        System.out.println("================");

        int totalUsedSize = allDirs.get(0).getDiskSpace();
        System.out.println("Total used size: " + totalUsedSize);
        int totalUnusedSize = 70_000_000 - totalUsedSize;
        System.out.println("Total unused size: " + totalUnusedSize);
        int freeUpSize = 30_000_000 - totalUnusedSize;
        System.out.println("Total freeUp size: " + freeUpSize);

        Collections.sort(allDirs);
        for (Dir dir : allDirs) {
            if (dir.getDiskSpace() > freeUpSize) {
                System.out.println(dir.getDiskSpace());
                break;
            }
        }
    }
}
