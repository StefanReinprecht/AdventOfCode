package aoc.year2023.day17;

import aoc.utils.Utils;

import java.util.*;
import java.util.stream.Stream;

public class Day17 {

    private Node[][] grid;
    private Node startNode;
    private Node endNode;

    public static void main(String[] args) {
        //List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input17_test.txt");
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input17_sample.txt");
        //List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input17.txt");

        Day17 day17 = new Day17();
        day17.buildGrid(inputList);
        day17.dijkstra();
        System.out.println("Answer 1: " + day17.getDistance());
        day17.print();
    }

    public void buildGrid(List<String> inputList) {
        int width = inputList.getFirst().length();
        int height = inputList.size();

        this.grid = new Node[height][width];

        for (int y = 0; y < inputList.size(); y++) {
            String line = inputList.get(y);
            for (int x = 0; x < line.length(); x++) {
                this.grid[y][x] = new Node(x, y, line.charAt(x));
            }
        }

        this.startNode = grid[0][0];
        this.endNode = grid[grid.length - 1][grid[0].length - 1];
    }

    public void dijkstra() {
        startNode.setDistance(0);

        Set<Node> processedNodes = new HashSet<>();
        Set<Node> unprocessedNodes = new HashSet<>();

        unprocessedNodes.add(startNode);

        while (!unprocessedNodes.isEmpty()) {
            Node evaluationNode = unprocessedNodes.stream().sorted().findFirst().orElseThrow();
            unprocessedNodes.remove(evaluationNode);
            for (Node neighborNode : getNeighborNodes(evaluationNode)) {
                Integer edgeWeight = neighborNode.getValue();
                if (!processedNodes.contains(neighborNode)) {
                    calculateMinimumDistance(neighborNode, edgeWeight, evaluationNode);
                    unprocessedNodes.add(neighborNode);
                }
            }
            processedNodes.add(evaluationNode);
        }
    }

    private List<Node> getNeighborNodes(Node currentNode) {
        List<Node> actualNeighbourNodes = new ArrayList<>();

        if (currentNode.getY() != 0) { // NORTH
            Node northNeighbour = grid[currentNode.getY() - 1][currentNode.getX()];
            if (!pathIsMoreThanThreeBlocksInOneDirection(currentNode, northNeighbour)) {
                actualNeighbourNodes.add(northNeighbour);
            }
        }
        if (currentNode.getY() != grid.length - 1) { // SOUTH
            Node southNeighbour = grid[currentNode.getY() + 1][currentNode.getX()];
            if (!pathIsMoreThanThreeBlocksInOneDirection(currentNode, southNeighbour)) {
                actualNeighbourNodes.add(southNeighbour);
            }
        }
        if (currentNode.getX() != 0) { // WEST
            Node westNeighbour = grid[currentNode.getY()][currentNode.getX() - 1];
            if (!pathIsMoreThanThreeBlocksInOneDirection(currentNode, westNeighbour)) {
                actualNeighbourNodes.add(westNeighbour);
            }
        }
        if (currentNode.getX() != grid[0].length - 1) { // EAST
            Node eastNeighbour = grid[currentNode.getY()][currentNode.getX() + 1];
            if (!pathIsMoreThanThreeBlocksInOneDirection(currentNode, eastNeighbour)) {
                actualNeighbourNodes.add(eastNeighbour);
            }
        }

        // remove already visited noted
        actualNeighbourNodes.removeIf(node -> currentNode.getShortestPath().contains(node));

        return actualNeighbourNodes;
    }

    private static void calculateMinimumDistance(Node neighborNode, Integer edgeWeigh, Node currentNode) {
        Integer sourceDistance = currentNode.getDistance();
        if (sourceDistance + edgeWeigh < neighborNode.getDistance()) {
            neighborNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(currentNode.getShortestPath());
            shortestPath.add(currentNode);
            neighborNode.setShortestPath(shortestPath);
        }
    }

    public int getDistance() {
        return endNode.getDistance();
    }

    public void print() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Node currentNode = grid[i][j];
                if (endNode.equals(currentNode)) {
                    System.out.print("X");
                } else if (endNode.getShortestPath().contains(currentNode)) {
                    System.out.print("#");
                } else {
                    System.out.print(currentNode.getValue());
                }
            }
            System.out.println();
        }

        System.out.println("=====");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Node currentNode = grid[i][j];
                System.out.println(currentNode.getX() + "/" + currentNode.getY() + " " + currentNode.getDistance());
            }
        }
    }

    private static boolean pathIsMoreThanThreeBlocksInOneDirection(Node currentNode, Node neighborNode) {
        List<Node> shortestPath = currentNode.getShortestPath();
        if (shortestPath.size() < 3) {
            return false;
        }

        int x = neighborNode.getX();
        int y = neighborNode.getY();

        List<Node> reversed = shortestPath.reversed();
        if (
            currentNode.getX() == x - 1 &&
            reversed.get(0).getX() == x - 2 &&
            reversed.get(1).getX() == x - 3 &&
            reversed.get(2).getX() == x - 4 &&
            currentNode.getY() == y &&
            reversed.get(0).getY() == y &&
            reversed.get(1).getY() == y &&
            reversed.get(2).getY() == y
        ) {
            return true;
        } else
        if (
            currentNode.getX() == x + 1 &&
            reversed.get(0).getX() == x + 2 &&
            reversed.get(1).getX() == x + 3 &&
            reversed.get(2).getX() == x + 4 &&
            currentNode.getY() == y &&
            reversed.get(0).getY() == y &&
            reversed.get(1).getY() == y &&
            reversed.get(2).getY() == y
        ) {
            return true;
        } else
        if (
            currentNode.getY() == y + 1 &&
            reversed.get(0).getY() == y + 2 &&
            reversed.get(1).getY() == y + 3 &&
            reversed.get(2).getY() == y + 4 &&
            currentNode.getX() == x &&
            reversed.get(0).getX() == x &&
            reversed.get(1).getX() == x &&
            reversed.get(2).getX() == x
        ) {
            return true;
        } else
        if (
            currentNode.getY() == y - 1 &&
            reversed.get(0).getY() == y - 2 &&
            reversed.get(1).getY() == y - 3 &&
            reversed.get(2).getY() == y - 4 &&
            currentNode.getX() == x &&
            reversed.get(0).getX() == x &&
            reversed.get(1).getX() == x &&
            reversed.get(2).getX() == x
        ) {
            return true;
        }

        return false;
    }
}
