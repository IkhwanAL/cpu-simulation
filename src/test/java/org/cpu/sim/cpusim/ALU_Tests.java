package org.cpu.sim.cpusim;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ALU_Tests {
  Register reg;
  Memory mem;
  ArrayList<Instruction> program;

  ALU alu;

  @BeforeEach
  void setup() {
    alu = new ALU();
    reg = new Register();
    mem = new Memory();
    program = new ArrayList<Instruction>();
  }

  private void haltInstrunction(ArrayList<Instruction> instructions) {
    var instruction = new Instruction();
    instruction.opcode = OpCode.HALT;
    instruction.dest = 0;
    instruction.src = 1;

    instructions.add(instruction);
  }

  private void registerAndValueInstrunction(ArrayList<Instruction> instructions, int opA, int value, OpCode code) {
    var instruction = new Instruction();
    instruction.opcode = code;
    instruction.dest = opA;
    instruction.src = value;

    instructions.add(instruction);
  }

  private void bothRegisterInstruction(ArrayList<Instruction> instructions, int opA, int opB, OpCode code) {
    var instruction = new Instruction();
    instruction.opcode = code;
    instruction.dest = opA;
    instruction.src = opB;

    instructions.add(instruction);
  }

  private void singleInstrunction(ArrayList<Instruction> instructions, int target, OpCode code) {
    var instruction = new Instruction();
    instruction.opcode = code;
    instruction.dest = target;
    instruction.src = 0;

    instructions.add(instruction);
  }

  @Test
  void executeTest() {
    alu.execute(reg, mem, program);

    if (!alu.halted) {
      throw new RuntimeException("ALU Unit Suppose to be halted, the program instruction is empty");
    }
  }

  @Test
  public void loadAndAddTest() {
    registerAndValueInstrunction(program, 0, 1, OpCode.LOAD);
    registerAndValueInstrunction(program, 1, 1, OpCode.LOAD);
    bothRegisterInstruction(program, 0, 1, OpCode.ADD);
    haltInstrunction(program);

    alu.execute(reg, mem, program);

    if (alu.halted && alu.fault != CpuFault.None) {
      // Possible That Alu Unit Error or Test Code Error
      throw new RuntimeException("ALU Unit is halted, something wrong with execute or incorrect instruction");
    }
  }

  @Test
  public void storeAndLoadTest() {
    registerAndValueInstrunction(program, 0, 1, OpCode.LOAD);
    registerAndValueInstrunction(program, 1, 4, OpCode.LOAD);
    registerAndValueInstrunction(program, 1, 0, OpCode.STOREM);
    registerAndValueInstrunction(program, 0, 0, OpCode.LOADM);
    haltInstrunction(program);

    alu.execute(reg, mem, program);

    if (alu.halted && alu.fault == CpuFault.INVALID_MEM) {
      throw new RuntimeException(
          "ALU Unit is halted, something wrong with memory address decode function or memory is too small");
    }

    if (alu.halted && reg.r[0] != 4) {
      throw new RuntimeException(
          "ALU Unit is halted, incorrect value register A should be the same as register B");
    }
  }

  @Test
  public void jumpTest() {
    registerAndValueInstrunction(program, 0, 1, OpCode.LOAD);
    singleInstrunction(program, 3, OpCode.JMP);
    haltInstrunction(program);

    if (alu.halted && alu.fault != CpuFault.None) {
      throw new RuntimeException(
          "ALU Unit is halted, something wrong with jump instruction in test code");
    }
  }

  @Test
  public void jumpIfZeroTest() {
    registerAndValueInstrunction(program, 0, -3, OpCode.LOAD);
    registerAndValueInstrunction(program, 1, 1, OpCode.LOAD);
    bothRegisterInstruction(program, 0, 1, OpCode.ADD);
    singleInstrunction(program, 6, OpCode.JZ);
    singleInstrunction(program, 3, OpCode.JMP);
    haltInstrunction(program);

    if (alu.halted && alu.fault != CpuFault.None) {
      throw new RuntimeException(
          "ALU Unit is halted, something wrong with jump instruction in test code");
    }
  }

  @Test
  public void jumpIfNotZeroTest() {
    registerAndValueInstrunction(program, 0, 1, OpCode.LOAD);
    registerAndValueInstrunction(program, 1, 1, OpCode.LOAD);
    bothRegisterInstruction(program, 0, 1, OpCode.CMP);
    singleInstrunction(program, 6, OpCode.JNZ);
    registerAndValueInstrunction(program, 2, 1, OpCode.LOAD);
    haltInstrunction(program);

    if (alu.halted && alu.fault != CpuFault.None) {
      throw new RuntimeException(
          "ALU Unit is halted, something wrong with jump not zero instruction in test code");
    }
  }

  @Test
  public void compareTest() {
    registerAndValueInstrunction(program, 0, 1, OpCode.LOAD);
    registerAndValueInstrunction(program, 1, 1, OpCode.LOAD);
    bothRegisterInstruction(program, 0, 1, OpCode.CMP);
    haltInstrunction(program);

    if (alu.halted && alu.fault != CpuFault.None) {
      throw new RuntimeException(
          "ALU Unit is halted, something wrong with cmp instruction in test code");
    }

    if (alu.halted && alu.flag.zero != true) {
      throw new RuntimeException(
          "ALU Unit is halted, something wrong with comparison failed");
    }
  }

  @Test
  public void subTest() {
    registerAndValueInstrunction(program, 0, 1, OpCode.LOAD);
    registerAndValueInstrunction(program, 1, 1, OpCode.LOAD);
    bothRegisterInstruction(program, 0, 1, OpCode.SUB);
    haltInstrunction(program);

    if (alu.halted && alu.fault != CpuFault.None) {
      throw new RuntimeException(
          "ALU Unit is halted, something wrong with cmp instruction in test code");
    }
  }
}
