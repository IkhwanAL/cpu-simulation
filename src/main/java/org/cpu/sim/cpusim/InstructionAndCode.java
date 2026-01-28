package org.cpu.sim.cpusim;

enum OpCode {
  LOAD, STORE, ADD, JMP, HALT, JZ;

  static OpCode fromString(String cmd) {
    try {
      return OpCode.valueOf(cmd);
    } catch (IllegalArgumentException | NullPointerException _) {
      return null;
    }
  }
}

enum CpuFault {
  None, INVALID_PC, INVALID_MEM, ILLEGAL_INSTRUCTION
}

class Instruction {
  OpCode opcode;
  int dest;
  int src;
}
