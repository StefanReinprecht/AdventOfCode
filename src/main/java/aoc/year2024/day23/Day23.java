package aoc.year2024.day23;

import aoc.utils.Utils;

import java.util.*;

public class Day23 {

    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day23_input.txt");

        Map<String, Set<String>> connections = parseInput(inputList);

        Map<String, Set<String>> potentialConnection = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : connections.entrySet()) {
            if (entry.getKey().startsWith("t")) {
                potentialConnection.put(entry.getKey(), entry.getValue());
            }
        }

        Set<String> loops = new HashSet<>();
        // find connection loops of 3 nodes
        for (Map.Entry<String, Set<String>> potentialEntry : potentialConnection.entrySet()) {
            System.out.println(potentialEntry.getKey() + ": " + potentialEntry.getValue());
            for (String n1 : potentialEntry.getValue()) {
                Set<String> n1s = connections.get(n1);
                for (String n2 : n1s) {
                    Set<String> n2s = connections.get(n2);
                    for (String n3 : n2s) {
                        if (n3.equals(potentialEntry.getKey())) {
                            List<String> loopsConcater = new ArrayList<>(List.of(potentialEntry.getKey(), n1, n2));
                            Collections.sort(loopsConcater);
                            loops.add(String.join("", loopsConcater));
                        }
                    }
                }
            }
        }

        System.out.println("Part 1: " + loops.size());

        List<String> biggestSet = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : connections.entrySet()) {
            List<String> subList = new ArrayList<>(entry.getValue());
            while (subList.size() > 0) {
                boolean allMatch = true;
                for (String con : subList) {
                    Set<String> subConnections = connections.get(con);
                    ArrayList<String> mustHaveConnections = new ArrayList<>(subList);
                    mustHaveConnections.remove(con);
                    mustHaveConnections.add(entry.getKey());
                    if (!subConnections.containsAll(mustHaveConnections)) {
                        allMatch = false;
                    }
                }
                if (allMatch && biggestSet.size() < subList.size() + 1) {
                    biggestSet = new ArrayList<>(subList);
                    biggestSet.add(entry.getKey());
                    break;
                } else if (!allMatch) {
                    /*
                     * Very dirty solution to the problem of removing always the first element and therefore missing possible largest sets.
                     * Actual clean solution should retry every possible combination by removing any element on each index and only then removing the next element.
                     */
                    Random r = new Random();
                    int low = 0;
                    int high = subList.size();
                    int result = r.nextInt(high-low) + low;
                    subList.remove(result);
                    //subList.removeFirst();
                } else {
                    break;
                }
            }
        }

        Collections.sort(biggestSet);
        System.out.println(String.join(",", biggestSet));
    }

    private static List<String> findLoop(Map<String, Set<String>> connections, String end, String current, List<String> visitedNodes) {
        if (current.equals(end)) {
            return visitedNodes;
        }

        for (String nextConnection : connections.get(current)) {
            if (visitedNodes.contains(nextConnection)) {
                continue;
            }
            visitedNodes.add(nextConnection);
            return findLoop(connections, end, nextConnection, new ArrayList<>(visitedNodes));
        }

        return Collections.emptyList();
    }

    private static Map<String, Set<String>> parseInput(List<String> inputList) {
        Map<String, Set<String>> connections = new HashMap<>();
        for (String input : inputList) {
            String[] nodes = input.split("-");

            connections.computeIfAbsent(nodes[0], k -> new HashSet<>()).add(nodes[1]);
            connections.computeIfAbsent(nodes[1], k -> new HashSet<>()).add(nodes[0]);
        }
        return connections;
    }
}
