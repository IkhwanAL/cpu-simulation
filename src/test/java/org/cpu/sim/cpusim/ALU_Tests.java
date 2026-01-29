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

  private void loadInstrunction(ArrayList<Instruction> instructions, int opA, int value) {
    var instruction = new Instruction();
    instruction.opcode = OpCode.LOAD;
    instruction.dest = opA;
    instruction.src = value;

    instructions.add(instruction);
  }

  private void cmpInstrunction(ArrayList<Instruction> instructions, int opA, int opB) {
    var instruction = new Instruction();
    instruction.opcode = OpCode.CMP;
    instruction.dest = opA;
    instruction.src = opB;

    instructions.add(instruction);
  }

  private void addInstrunction(ArrayList<Instruction> instructions, int opA, int opB) {
    var instruction = new Instruction();
    instruction.opcode = OpCode.ADD;
    instruction.dest = opA;
    instruction.src = opB;

    instructions.add(instruction);
  }

  private void storeMemInstrunction(ArrayList<Instruction> instructions, int opA, int memIndex) {
    var instruction = new Instruction();
    instruction.opcode = OpCode.STOREM;
    instruction.dest = opA;
    instruction.src = memIndex;

    instructions.add(instruction);
  }

  private void loadMemInstrunction(ArrayList<Instruction> instructions, int opA, int memIndex) {
    var instruction = new Instruction();
    instruction.opcode = OpCode.LOADM;
    instruction.dest = opA;
    instruction.src = memIndex;

    instructions.add(instruction);
  }

  private void jumpInstrunction(ArrayList<Instruction> instructions, int target) {
    var instruction = new Instruction();
    instruction.opcode = OpCode.JMP;
    instruction.dest = target;
    instruction.src = 0;

    instructions.add(instruction);
  }

  private void jumpIfZeroInstrunction(ArrayList<Instruction> instructions, int target) {
    var instruction = new Instruction();
    instruction.opcode = OpCode.JZ;
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
    loadInstrunction(program, 0, 1);
    loadInstrunction(program, 1, 1);
    addInstrunction(program, 0, 1);
    haltInstrunction(program);

    alu.execute(reg, mem, program);

    if (alu.halted && alu.fault != CpuFault.None) {
      // Possible That Alu Unit Error or Test Code Error
      throw new RuntimeException("ALU Unit is halted, something wrong with execute or incorrect instruction");
    }
  }

  @Test
  public void storeAndLoadTest() {
    loadInstrunction(program, 0, 1);
    loadInstrunction(program, 1, 4);
    storeMemInstrunction(program, 1, 0);
    loadMemInstrunction(program, 0, 0);
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
    loadInstrunction(program, 0, 1);
    jumpInstrunction(program, 3);
    haltInstrunction(program);

    if (alu.halted && alu.fault != CpuFault.None) {
      throw new RuntimeException(
          "ALU Unit is halted, something wrong with jump instruction in test code");
    }
  }

  @Test
  public void jumpIfZeroTest() {
    loadInstrunction(program, 0, -3);
    loadInstrunction(program, 1, 1);
    addInstrunction(program, 0, 1);
    jumpIfZeroInstrunction(program, 6);
    jumpInstrunction(program, 3);
    haltInstrunction(program);

    if (alu.halted && alu.fault != CpuFault.None) {
      throw new RuntimeException(
          "ALU Unit is halted, something wrong with jump instruction in test code");
    }
  }

  @Test
  public void compareTest() {
    loadInstrunction(program, 0, 1);
    loadInstrunction(program, 1, 1);
    cmpInstrunction(program, 0, 1);
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
}
