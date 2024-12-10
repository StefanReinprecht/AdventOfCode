package aoc.year2024.day6;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.Utils;
import org.javatuples.Pair;

public class Day6 {

  // I'm not proud but it does its job
  public static void main(String[] args) {
    List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day6_input.txt");

    Pos[][] positions = new Pos[inputList.size()][inputList.getFirst().length()]; {};

    int originalStartPosX = 0;
    int originalStartPosY = 0;
    int currentX = 0;
    int currentY = 0;
    char nextDirection = 'N';

    for (int y = 0; y < inputList.size(); y++) {
      for (int x = 0; x < inputList.get(y).length(); x++) {
        char c = inputList.get(y).charAt(x);

        if (c == '.') {
          positions[x][y] = new Pos(false);
        } else if (c == '#') {
          positions[x][y] = new Pos(true);
        } else if (c == '^') {
          positions[x][y] = new Pos(false);
          positions[x][y].setVisitedVertical();
          originalStartPosX = currentX = x;
          originalStartPosY = currentY = y;
        }
      }
    }

    // Part 1
    long startPart1 = System.currentTimeMillis();
    int totallyVisitedFields = 0;
    while(true) {
      if (nextDirection == 'N') {
        // check if moving to next position is possible
        if (currentY == 0) {
          // next move is not possible change direction
          nextDirection = 'E';
          continue;
        }
        Pos nextPos = positions[currentX][currentY - 1];
        if (nextPos.isVisitedVertical()) {
          break;
        } else if (nextPos.isObstacle()) {
          // next move is not possible change direction
          nextDirection = 'E';
        } else {
          // move to next direction
          currentY = currentY - 1;
          nextPos.setVisitedVertical();
        }
      } else if (nextDirection == 'E') {
        // check if moving to next position is possible
        if (currentX == inputList.size() - 1) {
          nextDirection = 'S';
          continue;
        }
        Pos nextPos = positions[currentX + 1][currentY];
        if (nextPos.isVisitedHorizontal()) {
          break;
        } else if (nextPos.isObstacle()) {
          // next move is not possible change direction
          nextDirection = 'S';
        } else {
          // move to next direction
          currentX = currentX + 1;
          nextPos.setVisitedHorizontal();
        }
      } else if (nextDirection == 'S') {
        // check if moving to next position is possible
        if (currentY == inputList.getFirst().length() - 1) {
          nextDirection = 'W';
          continue;
        }
        Pos nextPos = positions[currentX][currentY + 1];
        if (nextPos.isVisitedVertical()) {
          break;
        } else if (nextPos.isObstacle()) {
          // next move is not possible change direction
          nextDirection = 'W';
        } else {
          // move to next direction
          currentY = currentY + 1;
          nextPos.setVisitedVertical();
        }
      } else {
        // check if moving to next position is possible
        Pos nextPos = positions[currentX - 1][currentY];
        if (currentX == 0) {
          nextDirection = 'N';
          continue;
        }
        if (nextPos.isVisitedHorizontal()) {
          break;
        } else if (nextPos.isObstacle()) {
          // next move is not possible change direction
          nextDirection = 'N';
        } else {
          // move to next direction
          currentX = currentX - 1;
          nextPos.setVisitedHorizontal();
        }
      }
    }

    List<Pair<Integer, Integer>> allVisitedLocations = new ArrayList<>();
    for (int y = 0; y < positions[0].length; y++) {
      // Iterate over each element in the row
      for (int x = 0; x < positions.length; x++) {
        Pos pos = positions[x][y];
        if (pos.isObstacle()) {
          System.out.print("#");
        } else if (pos.isVisitedVertical() || pos.isVisitedHorizontal()) {
          totallyVisitedFields++;
          if (!(currentX == originalStartPosX && currentY == originalStartPosY)) {
            allVisitedLocations.add(new Pair<>(x, y));
          }
          System.out.print("X");
        } else {
          System.out.print(".");
        }
      }
      System.out.println(); // New line after each row
    }

    System.out.println("Part 1: " + totallyVisitedFields + " in " + (System.currentTimeMillis() - startPart1) + " ms");

    part2(allVisitedLocations, positions, originalStartPosX, originalStartPosY, inputList);
  }

  private static void part2(List<Pair<Integer, Integer>> allVisitedLocations, Pos[][] positions, int originalStartPosX, int originalStartPosY, List<String> inputList) {
    int currentX;
    char nextDirection;
    int currentY;
    // Part 2
    long startPart2 = System.currentTimeMillis();
    int possibleLoopCausingCounter = 0;

    for (int i = 0; i < allVisitedLocations.size(); i++) {
      // clear
      for (int y = 0; y < positions[0].length; y++) {
        for (int x = 0; x < positions.length; x++) {
          Pos pos = positions[x][y];
          pos.clear();
        }
      }
      positions[originalStartPosX][originalStartPosY].setVisitedVertical();
      currentX = originalStartPosX;
      currentY = originalStartPosY;
      nextDirection = 'N';

      // unset previous new obstacle
      if (i != 0) {
        Pair<Integer, Integer> prevLocation = allVisitedLocations.get(i - 1);
        positions[prevLocation.getValue0()][prevLocation.getValue1()].setObstacle(false);
      }

      // set current new obstacle
      Pair<Integer, Integer> prevLocation = allVisitedLocations.get(i);
      positions[prevLocation.getValue0()][prevLocation.getValue1()].setObstacle(true);

      // check if loop
      while(true) {
        if (nextDirection == 'N') {
          // check if moving to next position is possible
          if (currentY == 0) {
            // next move is leaving the map
            break;
          }
          Pos nextPos = positions[currentX][currentY - 1];
          if (nextPos.visitedFromBottom) {
              possibleLoopCausingCounter++;
            break;
          } else if (nextPos.isObstacle()) {
            // next move is not possible change direction
            nextDirection = 'E';
          } else {
            // move to next direction
            currentY = currentY - 1;
            nextPos.visitedFromBottom = true;
          }
        } else if (nextDirection == 'E') {
          // check if moving to next position is possible
          if (currentX == inputList.size() - 1) {
            // next move is leaving the map
            break;
          }
          Pos nextPos = positions[currentX + 1][currentY];
          if (nextPos.visitedFromLeft) {
              possibleLoopCausingCounter++;
            break;
          } else if (nextPos.isObstacle()) {
            // next move is not possible change direction
            nextDirection = 'S';
          } else {
            // move to next direction
            currentX = currentX + 1;
            nextPos.visitedFromLeft = true;
          }
        } else if (nextDirection == 'S') {
          // check if moving to next position is possible
          if (currentY == inputList.getFirst().length() - 1) {
            // next move is leaving the map
            break;
          }
          Pos nextPos = positions[currentX][currentY + 1];
          if (nextPos.visitedFromTop) {
              possibleLoopCausingCounter++;
            break;
          } else if (nextPos.isObstacle()) {
            // next move is not possible change direction
            nextDirection = 'W';
          } else {
            // move to next direction
            currentY = currentY + 1;
            nextPos.visitedFromTop = true;
          }
        } else {
          // check if moving to next position is possible
          if (currentX == 0) {
            // next move is leaving the map
            break;
          }
          Pos nextPos = positions[currentX - 1][currentY];
          if (nextPos.visitedFromRight) {
              possibleLoopCausingCounter++;
            break;
          } else if (nextPos.isObstacle()) {
            // next move is not possible change direction
            nextDirection = 'N';
          } else {
            // move to next direction
            currentX = currentX - 1;
            nextPos.visitedFromRight = true;
          }
        }
      }
    }
    System.out.println("Part 2: " + possibleLoopCausingCounter + " in " + (System.currentTimeMillis() - startPart2) + " ms");
  }

  private static void printGrid(Pos[][] positions, Pair<Integer, Integer> newObstacleLocation) {
    for (int y = 0; y < positions[0].length; y++) {
      // Iterate over each element in the row
      for (int x = 0; x < positions.length; x++) {
        Pos pos = positions[x][y];
        if (x == newObstacleLocation.getValue0() && y == newObstacleLocation.getValue1()) {
          System.out.print("O");
        } else
        if (pos.isObstacle()) {
          System.out.print("#");
        } else if (pos.isVisitedVertical() || pos.isVisitedHorizontal()) {
          System.out.print("X");
        } else {
          System.out.print(".");
        }
      }
      System.out.println(); // New line after each row
    }
  }

  public static class Pos {
    private boolean obstacle;
    private boolean visitedHorizontal;
    private boolean visitedVertical;
    private boolean visitedFromBottom;
    private boolean visitedFromLeft;
    private boolean visitedFromTop;
    private boolean visitedFromRight;

    public Pos(boolean obstacle) {
      this.obstacle = obstacle;
    }

    public void setVisitedHorizontal() {
      this.visitedHorizontal = true;
    }

    public void setVisitedVertical() {
      this.visitedVertical = true;
    }

    public boolean isVisitedHorizontal() {
      return visitedHorizontal;
    }

    public boolean isVisitedVertical() {
      return visitedVertical;
    }

    public void setObstacle(boolean obstacle) {
      this.obstacle = obstacle;
    }

    public boolean isObstacle() {
      return obstacle;
    }

    public void clear() {
      visitedHorizontal = false;
      visitedVertical = false;
      visitedFromTop = false;
      visitedFromRight = false;
      visitedFromBottom = false;
      visitedFromLeft = false;
    }
  }
}
