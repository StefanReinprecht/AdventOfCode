package aoc.year2023.day16;

import aoc.utils.Utils;

import java.util.*;

public class Day16 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input16.txt");

        long startPart1 = System.currentTimeMillis();
        // parse tiles
        List<List<Tile>> tiles = new ArrayList<>();
        for (int y = 0; y < inputList.size(); y++) {
            String[] line = inputList.get(y).split("");
            List<Tile> lineTiles = new ArrayList<>();
            for (int x = 0; x < line.length; x++) {
                lineTiles.add(new Tile(x, y, line[x]));
            }
            tiles.add(lineTiles);
        }

        List<Beam> beams = new ArrayList<>();

        // add initial beam
        beams.add(new Beam(new Tile(-1, 0, "."), Direction.EAST));

        simulate(beams, tiles);

        long result1 = countVisitedTiles(tiles);
        System.out.println("Answer 1: " + result1);
        System.out.println("Time 1: " + (System.currentTimeMillis() - startPart1));

        long startPart2 = System.currentTimeMillis();
        List<Beam> possibleStartBeams = new ArrayList<>();
        // add all possible start beams
        tiles = refreshTiles(inputList);
        for (Tile t : tiles.getFirst()) {
            possibleStartBeams.add(new Beam(new Tile(t.getX(), t.getY() - 1, "."), Direction.SOUTH));
        }
        for (Tile t : tiles.getLast()) {
            possibleStartBeams.add(new Beam(new Tile(t.getX(), t.getY() + 1, "."), Direction.NORTH));
        }
        for (List<Tile> tile : tiles) {
            possibleStartBeams.add(new Beam(new Tile(-1, tile.getFirst().getY(), "."), Direction.EAST));
            possibleStartBeams.add(new Beam(new Tile(tile.getLast().getX() + 1, tile.getLast().getY(), "."), Direction.WEST));
        }

        long result2 = 0;
        for (Beam beam : possibleStartBeams) {
            tiles = refreshTiles(inputList);
            simulate(List.of(beam), tiles);
            long result = countVisitedTiles(tiles);

            if (result > result2) {
                result2 = result;
            }
        }
        System.out.println("Answer 2: " + result2);
        System.out.println("Time 2: " + (System.currentTimeMillis() - startPart2));
    }

    private static List<List<Tile>> refreshTiles(List<String> inputList) {
        List<List<Tile>> tiles = new ArrayList<>();
        for (int y = 0; y < inputList.size(); y++) {
            String[] line = inputList.get(y).split("");
            List<Tile> lineTiles = new ArrayList<>();
            for (int x = 0; x < line.length; x++) {
                lineTiles.add(new Tile(x, y, line[x]));
            }
            tiles.add(lineTiles);
        }
        return tiles;
    }

    private static long countVisitedTiles(List<List<Tile>> tiles) {
        return tiles.stream().flatMap(Collection::stream).filter(tile -> !tile.getVisitedFromDirection().isEmpty()).count();
    }

    private static void simulate(List<Beam> beams, List<List<Tile>> allTiles) {
        for (Beam beam : beams) {
            List<Beam> beamsForBeam = getBeamsForBeam(beam, allTiles);
            if (!beamsForBeam.isEmpty()) {
                simulate(beamsForBeam, allTiles);
            }
        }
    }

    private static List<Beam> getBeamsForBeam(Beam beam, List<List<Tile>> allTiles) {
        move(beam, allTiles);

        while(beam.getCurrentPosition().getTileContent().equals(".") && !beam.isBeamEnded()) {
            move(beam, allTiles);
        }

        if (beam.isBeamEnded()) {
            return Collections.emptyList();
        }

        return calcNewBeams(beam);
    }

    private static List<Beam> calcNewBeams(Beam beam) {
        if (beam.getDirection() == Direction.NORTH) {
            switch (beam.getCurrentPosition().getTileContent()) {
                case "|" -> {
                    return List.of(beam.copy());
                }
                case "-" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.WEST);
                    Beam copy2 = beam.copy();
                    copy2.setDirection(Direction.EAST);
                    return List.of(copy1, copy2);
                }
                case "/" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.EAST);
                    return List.of(copy1);
                }
                case "\\" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.WEST);
                    return List.of(copy1);
                }
            }
        } else if (beam.getDirection() == Direction.EAST) {
            switch (beam.getCurrentPosition().getTileContent()) {
                case "|" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.NORTH);
                    Beam copy2 = beam.copy();
                    copy2.setDirection(Direction.SOUTH);
                    return List.of(copy1, copy2);
                }
                case "-" -> {
                    return List.of(beam.copy());
                }
                case "/" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.NORTH);
                    return List.of(copy1);
                }
                case "\\" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.SOUTH);
                    return List.of(copy1);
                }
            }
        } else if (beam.getDirection() == Direction.SOUTH) {
            switch (beam.getCurrentPosition().getTileContent()) {
                case "|" -> {
                    return List.of(beam.copy());
                }
                case "-" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.WEST);
                    Beam copy2 = beam.copy();
                    copy2.setDirection(Direction.EAST);
                    return List.of(copy1, copy2);
                }
                case "/" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.WEST);
                    return List.of(copy1);
                }
                case "\\" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.EAST);
                    return List.of(copy1);
                }
            }
        } else { // WEST
            switch (beam.getCurrentPosition().getTileContent()) {
                case "|" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.NORTH);
                    Beam copy2 = beam.copy();
                    copy2.setDirection(Direction.SOUTH);
                    return List.of(copy1, copy2);
                }
                case "-" -> {
                    return List.of(beam.copy());
                }
                case "/" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.SOUTH);
                    return List.of(copy1);
                }
                case "\\" -> {
                    Beam copy1 = beam.copy();
                    copy1.setDirection(Direction.NORTH);
                    return List.of(copy1);
                }
            }
        }
        throw new IllegalArgumentException("That should not happen!");
    }

    private static void move(Beam b, List<List<Tile>> allTiles) {
        Optional<Tile> nextLocation = Optional.empty();
        switch (b.getDirection()) {
            case NORTH -> nextLocation = getNorth(b.getCurrentPosition(), allTiles);
            case EAST -> nextLocation = getEast(b.getCurrentPosition(), allTiles);
            case SOUTH -> nextLocation = getSouth(b.getCurrentPosition(), allTiles);
            case WEST -> nextLocation = getWest(b.getCurrentPosition(), allTiles);
        }
        if (nextLocation.isPresent()) {
            Tile tile = nextLocation.get();
            if (tile.isVisitedFromDirection(b.getDirection())) {
                b.setBeamEnded(true);
            } else {
                tile.addVisitedFromDirection(b.getDirection());
            }
            b.setCurrentPosition(tile);
        } else {
            b.setBeamEnded(true);
        }
    }

    private static Optional<Tile> getWest(Tile currentPosition, List<List<Tile>> allTiles) {
        if (currentPosition.getX() == 0) {
            // we are at the end of the line we cannot go further west
            return Optional.empty();
        }
        return Optional.of(allTiles.get(currentPosition.getY()).get(currentPosition.getX() - 1));
    }

    private static Optional<Tile> getSouth(Tile currentPosition, List<List<Tile>> allTiles) {
        if (currentPosition.getY() == allTiles.size() - 1) {
            // we are at the end of the line we cannot go further south
            return Optional.empty();
        }
        return Optional.of(allTiles.get(currentPosition.getY() + 1).get(currentPosition.getX()));
    }

    private static Optional<Tile> getEast(Tile currentPosition, List<List<Tile>> allTiles) {
        if (currentPosition.getX() == allTiles.getFirst().size() - 1) {
            // we are at the end of the line we cannot go further east
            return Optional.empty();
        }
        return Optional.of(allTiles.get(currentPosition.getY()).get(currentPosition.getX() + 1));
    }

    private static Optional<Tile> getNorth(Tile currentPosition, List<List<Tile>> allTiles) {
        if (currentPosition.getY() == 0) {
            // we are at the end of the line we cannot go further north
            return Optional.empty();
        }
        return Optional.of(allTiles.get(currentPosition.getY() - 1).get(currentPosition.getX()));
    }
}
