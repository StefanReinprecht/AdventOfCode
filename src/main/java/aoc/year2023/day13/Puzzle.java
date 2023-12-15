package aoc.year2023.day13;

import java.util.ArrayList;
import java.util.List;

public class Puzzle {

    private final List<String> rows;
    private final List<String> cols;

    public Puzzle(List<String> puzzleLines) {
        rows = puzzleLines;
        cols = calcCols(puzzleLines);
    }

    public int getPart1Value() {
        int colIndex = getColPatternIndex();
        if (colIndex != -1) {
            return colIndex + 1;
        } else {
            return (getRowPatternIndex() + 1) * 100;
        }
    }

    private int getColPatternIndex() {
        List<Integer> mirrorLines = new ArrayList<>();
        for (int i = 0; i < cols.size() - 1; i++) {
            if (cols.get(i).equals(cols.get(i + 1))) {
                mirrorLines.add(i);
            }
        }

        int patternIndex = -1;
        for (int index : mirrorLines) {
            int patternSize = 0;
            try {
                while (cols.get(index - patternSize).equals(cols.get(index + (1 + patternSize)))) {
                    patternSize++;
                }
            } catch (Exception e) {
                // we reached array border, it is a valid mirror
                patternIndex = index;
                break;
            }
        }
        return patternIndex;
    }

    private int getRowPatternIndex() {
        List<Integer> mirrorLines = new ArrayList<>();
        for (int i = 0; i < rows.size() - 1; i++) {
            if (rows.get(i).equals(rows.get(i + 1))) {
                mirrorLines.add(i);
            }
        }

        int patternIndex = -1;
        for (int index : mirrorLines) {
            int patternSize = 0;
            try {
                while (rows.get(index - patternSize).equals(rows.get(index + (1 + patternSize)))) {
                    patternSize++;
                }
            } catch (Exception e) {
                // we reached array border, it is a valid mirror
                patternIndex = index;
                break;
            }
        }
        return patternIndex;
    }

    private List<String> calcCols(List<String> puzzleLines) {
        List<String> cols = new ArrayList<>();
        for (int i = 0; i < puzzleLines.getFirst().length(); i++) {
            StringBuilder sb = new StringBuilder();
            for (String row : puzzleLines) {
                sb.append(row.charAt(i));
            }
            cols.add(sb.toString());
        }

        return cols;
    }
}
