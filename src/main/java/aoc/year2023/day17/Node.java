package aoc.year2023.day17;

import java.util.*;

public class Node implements Comparable<Node>{
    private final int x;
    private final int y;
    private final int value;
    private List<Node> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;
    Map<Node, Integer> neighborNodes = new HashMap<>();

    public Node(int x, int y, char value) {
        this.x = x;
        this.y = y;
        this.value = Integer.parseInt(String.valueOf(value));
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

    public void addNeighbor(Node destination, int distance) {
        neighborNodes.put(destination, distance);
    }

    public Map<Node, Integer> getNeighborNodes() {
        return neighborNodes;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public int getValue() {
        return value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", value=" + value +
                ", distance=" + distance +
                '}';
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.getDistance(), o.getDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y && value == node.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, value);
    }
}
