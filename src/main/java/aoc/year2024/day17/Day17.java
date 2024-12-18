package aoc.year2024.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import aoc.utils.Utils;

public class Day17 {

  public static void main(String[] args) {
    List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day17_input.txt");

    List<Integer> instructions = parseInstructions(inputList);

    String output = processInstructions(instructions);
    System.out.println("Part 1: " + output);
  }

  private static String processInstructions(List<Integer> instructions) {
    long registerA = 23999685;
    long registerB = 0;
    long registerC = 0;
    List<String> programOutput = new ArrayList<>();
    for (int i = 0; i < instructions.size() - 1; i = i + 2) {
      Integer opcode = instructions.get(i);
      Integer operand = instructions.get(i + 1);
      switch(opcode) {
        case 0:
          registerA = registerA / (long) Math.pow(2, getComboOperand(operand, registerA, registerB, registerC));
          break;
        case 1:
          registerB = registerB ^ operand;
          break;
        case 2:
          registerB = getComboOperand(operand, registerA, registerB, registerC) % 8;
          break;
        case 3:
          if (registerA == 0) {
            break;
          }
          i = operand - 2; // the for loop adds 2 again
          break;
        case 4:
          registerB = registerB ^ registerC;
          break;
        case 5:
          programOutput.add("" + getComboOperand(operand, registerA, registerB, registerC) % 8);
          break;
        case 6:
          registerB = registerA / (long) Math.pow(2, getComboOperand(operand, registerA, registerB, registerC));
          break;
        case 7:
          registerC = registerA / (long) Math.pow(2, getComboOperand(operand, registerA, registerB, registerC));
          break;
        default:
          throw new IllegalStateException("Unhandled opcode: " + opcode);
      }
    }

    return String.join(",", programOutput);
  }

  private static long getComboOperand(int operand, long registerA, long registerB, long registerC) {
    if (operand == 0 || operand == 1 || operand == 2 || operand == 3) {
      return operand;
    } else if (operand == 4) {
      return registerA;
    } else if (operand == 5) {
      return registerB;
    } else if (operand == 6) {
      return registerC;
    } else {
      throw new IllegalStateException("Unhandled combo operand: " + operand);
    }
  }

  private static List<Integer> parseInstructions(List<String> inputList) {
    String instructionLine = inputList.get(4);
    return Arrays.stream(instructionLine.substring(9).split(",")).map(Integer::parseInt).toList();
  }

  public static Long parseRegister(int lineNr, List<String> inputList) {
    String line = inputList.get(lineNr);
    String number = line.substring(12);
    return Long.valueOf(number);
  }
}