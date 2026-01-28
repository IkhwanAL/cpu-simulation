package org.cpu.sim.cpusim;

import org.junit.jupiter.api.Test;

public class ControlUnitTests {
  @Test
  void fetchProgramTest() {
    var program = "LOAD A, 4\r\n";

    var cu = new ControlUnit();
    cu.fetchProgram(program);

    if (cu.programInstruction == null) {
      throw new RuntimeException("Control Unit Does Not Fetch Program");
    }
  }

  @Test
  void decodeProgramTest() {
    var program = "LOAD A, 4\r\n";

    var cu = new ControlUnit();
    cu.fetchProgram(program);
    var list = cu.decode();

    if (list.isEmpty()) {
      throw new RuntimeException("Control Unit Failed To Decode");
    }

    program = "LOAD A, 4\r\nLOAD B, 5\r\n, ADD A, B\r\n";
    cu.fetchProgram(program);
    list = cu.decode();

    if (list.size() != 3) {
      throw new RuntimeException("Control Unit Give an Incorrect Instruction");
    }
  }
}
