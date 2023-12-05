package aoc.year2023.day5;

import aoc.utils.Utils;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Stream;

public class Day5 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input5.txt");

        List<Long> seeds = new ArrayList<>();
        List<Range> seedToSoil = new ArrayList<>();
        List<Range> soilToFertilizer = new ArrayList<>();
        List<Range> fertilizerToWater = new ArrayList<>();
        List<Range> waterToLight = new ArrayList<>();
        List<Range> lightToTemperature = new ArrayList<>();
        List<Range> temperatureToHumidity = new ArrayList<>();
        List<Range> humidityToLocation = new ArrayList<>();

        List<Range> currentRangeList = seedToSoil;

        // fill maps
        for (String line : inputList) {
            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("seeds")) {
                seeds.addAll(parseSeeds(line));
            } else if ("seed-to-soil map:".equals(line)) {
                currentRangeList = seedToSoil;
            } else if ("soil-to-fertilizer map:".equals(line)) {
                currentRangeList = soilToFertilizer;
            } else if ("fertilizer-to-water map:".equals(line)) {
                currentRangeList = fertilizerToWater;
            } else if ("water-to-light map:".equals(line)) {
                currentRangeList = waterToLight;
            } else if ("light-to-temperature map:".equals(line)) {
                currentRangeList = lightToTemperature;
            } else if ("temperature-to-humidity map:".equals(line)) {
                currentRangeList = temperatureToHumidity;
            } else if ("humidity-to-location map:".equals(line)) {
                currentRangeList = humidityToLocation;
            } else {
                parseCurrentLine(currentRangeList, line);
            }

        }

        long startPart1 = System.currentTimeMillis();
        long lowestLocation = -1;
        // get location for seed
        for (long seed : seeds) {
            long location = findLocationForSeed(seed, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation);
            if (lowestLocation == -1 || location < lowestLocation) {
                lowestLocation = location;
            }
        }
        System.out.println("Answer1: " + lowestLocation);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart1));
        System.out.println("====");

        long startPart2 = System.currentTimeMillis();
        List<Pair<Long, Long>> seedsRanges = parseSeedsPart2(inputList.get(0));
        int locationCounter = 0;
        // There must be a better way to do this
        while (true) {
            long seed = findSeedForLocation(locationCounter, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation);
            Optional<Pair<Long, Long>> any = seedsRanges.stream().filter(range -> range.getValue0() <= seed && seed <= range.getValue0() + range.getValue1()).findAny();
            if (any.isPresent()) {
                lowestLocation = locationCounter;
                break;
            }
            locationCounter++;
        }

        System.out.println("Answer2: " + lowestLocation);
        System.out.println("Time: " + (System.currentTimeMillis() - startPart2));
    }

    private static long findLocationForSeed(Long seed, List<Range> seedToSoil, List<Range> soilToFertilizer, List<Range> fertilizerToWater, List<Range> waterToLight, List<Range> lightToTemperature, List<Range> temperatureToHumidity, List<Range> humidityToLocation) {
        Optional<Range> soilRangeFound = seedToSoil.stream().filter(r -> r.sourceInRange(seed)).findFirst();
        long soil = soilRangeFound.map(range -> range.getTarget(seed)).orElse(seed);

        Optional<Range> fertilizerRangeFound = soilToFertilizer.stream().filter(r -> r.sourceInRange(soil)).findFirst();
        long fertilizer = fertilizerRangeFound.map(range -> range.getTarget(soil)).orElse(soil);

        Optional<Range> waterRangeFound = fertilizerToWater.stream().filter(r -> r.sourceInRange(fertilizer)).findFirst();
        long water = waterRangeFound.map(range -> range.getTarget(fertilizer)).orElse(fertilizer);

        Optional<Range> lightRangeFound = waterToLight.stream().filter(r -> r.sourceInRange(water)).findFirst();
        long light = lightRangeFound.map(range -> range.getTarget(water)).orElse(water);

        Optional<Range> temperatureRangeFound = lightToTemperature.stream().filter(r -> r.sourceInRange(light)).findFirst();
        long temperature = temperatureRangeFound.map(range -> range.getTarget(light)).orElse(light);

        Optional<Range> humidityRangeFound = temperatureToHumidity.stream().filter(r -> r.sourceInRange(temperature)).findFirst();
        long humidity = humidityRangeFound.map(range -> range.getTarget(temperature)).orElse(temperature);

        Optional<Range> locationRangeFound = humidityToLocation.stream().filter(r -> r.sourceInRange(humidity)).findFirst();
        return locationRangeFound.map(range -> range.getTarget(humidity)).orElse(humidity);
    }

    private static long findSeedForLocation(long location, List<Range> seedToSoil, List<Range> soilToFertilizer, List<Range> fertilizerToWater, List<Range> waterToLight, List<Range> lightToTemperature, List<Range> temperatureToHumidity, List<Range> humidityToLocation) {
        Optional<Range> humidityRangeFound = humidityToLocation.stream().filter(r -> r.targetInRange(location)).findFirst();
        long humidity = humidityRangeFound.map(range -> range.getSource(location)).orElse(location);

        Optional<Range> temperatureRangeFound = temperatureToHumidity.stream().filter(r -> r.targetInRange(humidity)).findFirst();
        long temperature = temperatureRangeFound.map(range -> range.getSource(humidity)).orElse(humidity);

        Optional<Range> lightRangeFound = lightToTemperature.stream().filter(r -> r.targetInRange(temperature)).findFirst();
        long light = lightRangeFound.map(range -> range.getSource(temperature)).orElse(temperature);

        Optional<Range> waterRangeFound = waterToLight.stream().filter(r -> r.targetInRange(light)).findFirst();
        long water = waterRangeFound.map(range -> range.getSource(light)).orElse(light);

        Optional<Range> fertilizerRangeFound = fertilizerToWater.stream().filter(r -> r.targetInRange(water)).findFirst();
        long fertilizer = fertilizerRangeFound.map(range -> range.getSource(water)).orElse(water);

        Optional<Range> soilRangeFound = soilToFertilizer.stream().filter(r -> r.targetInRange(fertilizer)).findFirst();
        long soil = soilRangeFound.map(range -> range.getSource(fertilizer)).orElse(fertilizer);

        Optional<Range> seedRangeFound = seedToSoil.stream().filter(r -> r.targetInRange(soil)).findFirst();
        return seedRangeFound.map(range -> range.getSource(soil)).orElse(soil);
    }

    private static void parseCurrentLine(List<Range> currentRangeList, String line) {
        String[] split = line.split(" ");
        long target = Long.parseLong(split[0]);
        long source = Long.parseLong(split[1]);
        long length = Long.parseLong(split[2]);

        currentRangeList.add(new Range(source, target, length));
    }

    private static Collection<Long> parseSeeds(String line) {
        return Stream.of(line.substring(7).split(" ")).map(Long::parseLong).toList();
    }

    private static List<Pair<Long, Long>> parseSeedsPart2(String line) {
        List<Long> parts = Stream.of(line.substring(7).split(" ")).map(Long::parseLong).toList();
        List<Pair<Long, Long>> seedRanges = new ArrayList<>();
        long startSeed = -1;
        for (int i = 0; i < parts.size(); i++) {
            if (i % 2 == 0) {
                startSeed = parts.get(i);
            } else {
                seedRanges.add(new Pair<>(startSeed, parts.get(i)));
            }
        }
        return seedRanges;
    }
}
