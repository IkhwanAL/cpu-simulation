package org.cpu.sim.cpusim;

enum OpCode {
  LOAD, ADD, JMP, HALT, JZ, CMP, LOADM, STOREM, JL, SUB, JNZ, JG, PUSH, POP;

  static OpCode fromString(String cmd) {
    try {
      return OpCode.valueOf(cmd);
    } catch (IllegalArgumentException | NullPointerException _) {
      return null;
    }
  }
}

enum CpuFault {
  None, INVALID_PC, INVALID_MEM, ILLEGAL_INSTRUCTION, StackOverflow, StackUnderflow;
}

class Instruction {
  OpCode opcode;
  int operandA;
  int operandB;
}

class Flags {
  Boolean zero;
  Boolean negative;
  Boolean carry;
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
