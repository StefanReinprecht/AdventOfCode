package aoc.year2023.day16;

import lombok.Data;

@Data
public class Beam {

    private Direction direction;
    private Tile startPosition;

    private Tile currentPosition;

    boolean beamEnded;

    public Beam(Tile currentPosition, Direction direction) {
        this.startPosition = currentPosition.copy();
        this.currentPosition = currentPosition;
        this.direction = direction;
    }

    public Beam copy() {
        return new Beam(currentPosition, direction);
    }
}
