package aoc.year2023.day16;

import aoc.year2022.day7.Dir;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Tile {

    private int x;
    private int y;
    private String tileContent;
    private List<Direction> visitedFromDirection = new ArrayList<>();

    public Tile(int x, int y, String tileContent) {
        this.x = x;
        this.y = y;
        this.tileContent = tileContent;
    }

    public void addVisitedFromDirection(Direction d) {
        visitedFromDirection.add(d);
    }

    public boolean isVisitedFromDirection(Direction fromDirection) {
        return visitedFromDirection.stream().anyMatch(d -> d == fromDirection);
    }

    public Tile copy() {
        return new Tile(x, y, tileContent);
    }
}
