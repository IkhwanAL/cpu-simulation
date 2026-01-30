package org.cpu.sim.cpusim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ControlUnitTests {
  ControlUnit cu;

  @BeforeEach
  void setup() {
    cu = new ControlUnit();
  }

  @Test
  void fetchProgramTest() {
    var program = "LOAD A, 4\r\nCMP A, B\r\nSTOREM A, 0x001\r\nLOADM A, 0x001\r\nLOAD A, -1\r\n";

    cu.fetchProgram(program);

    if (cu.programInstruction == null) {
      throw new RuntimeException("Control Unit Does Not Fetch Program");
    }
  }

  @Test
  void decodeProgramwithComment() {
    var program = "LOAD A, 4\r\n" +
        "CMP A, B ; this is comment\r\n" +
        "STOREM A, 0x001\r\n" +
        "; this is also comment\r\n" +
        "LOADM A, 0x001\r\n";

    cu.fetchProgram(program);
    var list = cu.decode();

    if (list.size() != 4) {
      throw new RuntimeException("Control Unit Give an Incorrect Instruction After Adding Comment");
    }
  }

  @Test
  void decodeProgramTest() {
    var program = "LOAD A, 4\r\n";

    cu.fetchProgram(program);
    var list = cu.decode();

    if (list.isEmpty()) {
      throw new RuntimeException("Control Unit Failed To Decode");
    }

    program = "LOAD A, 4\r\nLOAD B, 5\r\nADD A, B\r\nSTOREM A, 0x001\r\nLOADM A, 0x001\r\n";
    cu.fetchProgram(program);
    list = cu.decode();

    if (list.size() != 5) {
      throw new RuntimeException("Control Unit Give an Incorrect Instruction");
    }
  }

  @Test
  void decodeWithLabelTest() {
    var program = "LOAD A, 4\r\n" +
        "LOAD B, 1\r\n" +
        "PUSH A\r\n" +
        "JMP skip\r\n" +
        "LOAD C, 1\r\n" +
        "LOAD D, 2\r\n" +
        "skip:\r\n" +
        "ADD A, B\r\n" +
        "POP A\r\n" +
        "HALT\r\n";

    cu.fetchProgram(program);
    var list = cu.decode();

    if (list.size() != 7 + 2) {
      throw new RuntimeException("Control Unit Give an Incorrect Instruction When Add Label");
    }
  }

  @Test
  void decodeLoop() {
    var program = """
        LOAD A, 1
        LOAD B, 1

        loop:
        ADD A, B
        CMP A, 7
        JZ exit
        JMP loop

        exit:
        HALT
        """;
    cu.fetchProgram(program);
    var list = cu.decode();

    if (list.size() != 7) {
      throw new RuntimeException("Control Unit Give an Incorrect Instruction When Add Label");
    }
  }
}
