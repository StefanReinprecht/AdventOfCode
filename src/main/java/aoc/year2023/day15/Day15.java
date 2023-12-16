package aoc.year2023.day15;

import aoc.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

public class Day15 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input15.txt");

        List<String> steps = new ArrayList<>(Arrays.asList(inputList.getFirst().split(",")));

        // PART 1
        int totalHash = 0;
        for (String step : steps) {
            List<Integer> ascii = new ArrayList<>();
            for (String s : step.split("")) {
                ascii.add((int) s.charAt(0));
            }

            totalHash += hash(ascii);
        }

        System.out.println("Answer 1: " + totalHash);

        //PART 2
        HashMap<Integer, List<BoxContent>> boxes = new HashMap<>();
        // init boxes
        for (int i = 0; i < Math.pow(2, 8); i++) {
            boxes.put(i, new ArrayList<>());
        }
        for (String step : steps) {
            int operatorIndex = Math.max(step.indexOf('='), step.indexOf('-'));

            String boxLabel = step.substring(0, operatorIndex);
            List<Integer> ascii = new ArrayList<>();
            for (String s : boxLabel.split("")) {
                ascii.add((int) s.charAt(0));
            }
            int boxNumber = hash(ascii);

            String operator = step.substring(operatorIndex, operatorIndex + 1);
            if ("=".equals(operator)) {
                int focalLength = Integer.parseInt(step.substring(operatorIndex + 1));
                List<BoxContent> box = boxes.get(boxNumber);
                Optional<BoxContent> boxContent = box
                        .stream()
                        .filter(bc -> bc.getBoxContentLabel().equals(boxLabel)).findFirst();
                if (boxContent.isPresent()) {
                    boxContent.get().setFocalLength(focalLength);
                } else {
                    box.add(new BoxContent(boxLabel, focalLength));
                }
            } else {
                List<BoxContent> box = boxes.get(boxNumber);
                Optional<BoxContent> boxContent = box
                        .stream()
                        .filter(bc -> bc.getBoxContentLabel().equals(boxLabel)).findFirst();
                boxContent.ifPresent(box::remove);
            }
        }

        // calculate focusing power
        int totalFocusingPower = 0;
        for (Map.Entry<Integer, List<BoxContent>> entry : boxes.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                totalFocusingPower += ((1 + entry.getKey()) * (i + 1) * entry.getValue().get(i).getFocalLength());
            }
        }

        System.out.println("Answer 2: " + totalFocusingPower);
    }

    private static int hash(List<Integer> ascii) {
        int currentValue = 0;
        for (Integer c : ascii) {
            currentValue += c;
            currentValue *= 17;
            currentValue = currentValue % 256;
        }
        return currentValue;
    }

    @Data
    @AllArgsConstructor
    private static class BoxContent {
        private String boxContentLabel;
        private int focalLength;
    }
}
