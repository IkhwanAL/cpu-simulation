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
  void testLoop() {
    var program = """
        LOAD A, 1
        LOAD B, 1
        LOAD C, 7

        loop:
        ADD A, B
        CMP A, C
        JZ exit
        JMP loop

        exit:
        HALT
        """;

    cpu.cu.fetchProgram(program);
    var list = cpu.cu.decode();

    cpu.program = list;

    try {
      while (!cpu.alu.halted) {
        cpu.tick();
      }

      if (cpu.alu.fault != CpuFault.None) {
        throw new RuntimeException("The Cpu Halted With Status of :" + cpu.alu.fault);
      }
    } catch (Exception e) {
      throw new RuntimeException("Something Wrong When Try To Create Simple Loop Program" + e.getMessage());
    }
  }
}
