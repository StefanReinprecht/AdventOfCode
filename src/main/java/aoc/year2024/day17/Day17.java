package aoc.year2024.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import aoc.utils.Utils;

public class Day17 {

  public static int registerA;
  public static int registerB;
  public static int registerC;

  public static void main(String[] args) {
    List<String> inputList = Utils.readAllLinesFromResourceAsStream(2024, "day17_input.txt");

    registerA = parseRegister(0, inputList);
    registerB = parseRegister(1, inputList);
    registerC = parseRegister(2, inputList);

    List<Integer> instructions = parseInstructions(inputList);

    String output = processInstructions(instructions);

    System.out.println("Part 1: " + output);

    int aNew = 0;
    //while (output != "2,4,1,1,7,5,1,5,0,3,4,4,5,5,3,0") {
    while (aNew < 1024) {
      aNew += 8;
      registerA = aNew;
      registerB = 0;
      registerC = 0;

      output = processInstructions(instructions);
      System.out.println(aNew + ": " + output);
    }
  }

  private static String processInstructions(List<Integer> instructions) {
    List<String> programOutput = new ArrayList<>();
    for (int i = 0; i < instructions.size() - 1; i = i + 2) {
      Integer opcode = instructions.get(i);
      Integer operand = instructions.get(i + 1);

      switch(opcode) {
        case 0:
          registerA = registerA / (int) Math.pow(2, getComboOperand(operand));
          break;
        case 1:
          registerB = registerB ^ operand;
          break;
        case 2:
          registerB = getComboOperand(operand) % 8;
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
          programOutput.add("" + getComboOperand(operand) % 8);
          break;
        case 6:
          registerB = registerA / (int) Math.pow(2, getComboOperand(operand));
        case 7:
          registerC = registerA / (int) Math.pow(2, getComboOperand(operand));
          break;
        default:
          throw new IllegalStateException("Unhandled opcode: " + opcode);
      }
//      System.out.println("A: " + registerA);
//      System.out.println("B: " + registerB);
//      System.out.println("C: " + registerC);
    }

    return String.join(",", programOutput);
  }

  private static int getComboOperand(int operand) {
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

  public static Integer parseRegister(int lineNr, List<String> inputList) {
    String line = inputList.get(lineNr);
    String number = line.substring(12);
    return Integer.valueOf(number);
  }
}


//2,4 store result of 4 in registerB
//1,1 store result of bitwise xor of registerB and 1(001) in registerB ==> 100 xor 000 => 1
//    7,5 divide value of registerA by 2^registerB(1) and store in registerC
//1,5 store result of bitwise xor of registerB and 5(101) in registerB ==> 001 xor 101 => 100 => 4
//    0,3 divide value of registerA by 8 and store in registerA
//4,4 store result of bitwise xor of registerB and registerC in registerB
//5,5 print value stored in registerB modulo 8
//    3,0 loop back to instruction 0 while registerA != 0
//
//
//b register values :
//ending 2,
//ending 4,
//ending 1 or 9,
//ending 1 or 9,
//ending 7,
//ending 5,
//ending 1 or 9,
//ending 5,
//ending 0 or 8,
//ending 3,
//ending 4,
//ending 4,
//ending 5,
//ending 5,
//ending 3,
//ending 0 or 8
//
//
//    0
//    1
//    2
//    3
//    4
//    5
//    6
//    7
//    0
//    1
//
//    8 => 0,4
//    16 => 4,6
//    24 => 0,7
//    32 => 4,0
