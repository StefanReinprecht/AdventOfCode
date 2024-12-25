package aoc.year2024.day25;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Day25 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day25_input.txt");

        List<Lock> locks = getLocks(inputList);
        List<Key> keys = getKeys(inputList);

        int totalPais = 0;
        for (Lock lock : locks) {
            for (Key key : keys) {
                if (key.opens(lock)) {
                    totalPais++;
                }
            }
        }
        System.out.println("Part 1: " + totalPais);
    }

    private static List<Lock> getLocks(List<String> inputList) {
        List<Lock> locks = new ArrayList<>();

        List<String> object = new ArrayList<>();
        for (String input : inputList) {
            if (input.isEmpty()) {
                if (object.getFirst().equals("#####") && object.getLast().equals(".....")) {
                    locks.add(new Lock(object));
                }
                object.clear();
            } else {
                object.add(input);
            }
        }

        if (object.getFirst().equals("#####") && object.getLast().equals(".....")) {
            locks.add(new Lock(object));
        }

        return locks;
    }

    private static List<Key> getKeys(List<String> inputList) {
        List<Key> keys = new ArrayList<>();

        List<String> object = new ArrayList<>();
        for (String input : inputList) {
            if (input.isEmpty()) {
                if (object.getFirst().equals(".....") && object.getLast().equals("#####")) {
                    keys.add(new Key(object));
                }
                object.clear();
            } else {
                object.add(input);
            }
        }

        if (object.getFirst().equals(".....") && object.getLast().equals("#####")) {
            keys.add(new Key(object));
        }

        return keys;
    }

    private static class Lock {
        int pinLength1;
        int pinLength2;
        int pinLength3;
        int pinLength4;
        int pinLength5;

        public Lock(List<String> object) {
            for (int i = 0; i < object.size(); i++) {
                if (object.get(i).charAt(0) == '#') {
                    pinLength1 = i + 1;
                }
                if (object.get(i).charAt(1) == '#') {
                    pinLength2 = i + 1;
                }
                if (object.get(i).charAt(2) == '#') {
                    pinLength3 = i + 1;
                }
                if (object.get(i).charAt(3) == '#') {
                    pinLength4 = i + 1;
                }
                if (object.get(i).charAt(4) == '#') {
                    pinLength5 = i + 1;
                }
            }
        }
    }

    private static class Key {
        int keyHeight1;
        int keyHeight2;
        int keyHeight3;
        int keyHeight4;
        int keyHeight5;

        public Key(List<String> object) {
            for (int i = object.size() - 1; i >= 0; i--) {
                if (object.get(i).charAt(0) == '#') {
                    keyHeight1 = object.size() - i;
                }
                if (object.get(i).charAt(1) == '#') {
                    keyHeight2 = object.size() - i;
                }
                if (object.get(i).charAt(2) == '#') {
                    keyHeight3 = object.size() - i;
                }
                if (object.get(i).charAt(3) == '#') {
                    keyHeight4 = object.size() - i;
                }
                if (object.get(i).charAt(4) == '#') {
                    keyHeight5 = object.size() - i;
                }
            }
        }

        public boolean opens(Lock lock) {
            return keyHeight1 + lock.pinLength1 <= 7 &&
                    keyHeight2 + lock.pinLength2 <= 7 &&
                    keyHeight3 + lock.pinLength3 <= 7 &&
                    keyHeight4 + lock.pinLength4 <= 7 &&
                    keyHeight5 + lock.pinLength5 <= 7;
        }
    }
}
