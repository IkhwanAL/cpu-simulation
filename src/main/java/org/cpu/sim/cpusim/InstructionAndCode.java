package org.cpu.sim.cpusim;

enum OpCode {
  LOAD, STORE, ADD, JMP, HALT, JZ, CMP;

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

class Flags {
  Boolean zero;
  Boolean negative;
}

sealed interface Operand permits Immediate, LabelRef, HexCode, RegisterCode {
}

record Immediate(int value) implements Operand {
}

record HexCode(String value) implements Operand {
}

record RegisterCode(String value) implements Operand {
}

record LabelRef(String name) implements Operand {
}
