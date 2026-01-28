package org.cpu.sim.cpusim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CpuTests {
  Cpu cpu;

  @BeforeEach
  void setup() {
    cpu = new Cpu();
  }

  @Test
  void testParseAndExecute() {
    var program = "LOAD A, 4\r\n" +
        "LOAD B, 1\r\n" +
        "JMP skip\r\n" +
        "LOAD C, 1\r\n" +
        "LOAD D, 2\r\n" +
        "skip:\r\n" +
        "ADD A, B\r\n" +
        "HALT\r\n";

    cpu.cu.fetchProgram(program);
    cpu.program = cpu.cu.decode();

    while (!cpu.alu.halted) {
      cpu.tick();
    }

    if (cpu.alu.fault != CpuFault.None) {
      throw new RuntimeException("Something Wrong When Execute After Parse");
    }
  }
}
