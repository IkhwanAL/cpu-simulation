package org.cpu.sim.cpusim;

import java.util.ArrayList;

public class ALU {
  int stackPointer = 0;
  Boolean halted = false;
  CpuFault fault = CpuFault.None;
  Integer programCounter = 0;
  Flags flag = new Flags();

  ALU(int memLength) {
    stackPointer = memLength;
  }

  void execute(Register reg, Memory mem, ArrayList<Instruction> program) {
    if (programCounter < 0 || programCounter >= program.size()) {
      halted = true;
      fault = CpuFault.INVALID_PC;
      return;
    }

    Instruction inst = program.get(programCounter);

    switch (inst.opcode) {
      case LOAD:
      case SUB:
      case STOREM:
      case LOADM:
      case ADD: // register to register
        this.store(inst, reg, mem);
        break;
      case PUSH:
        if (this.stackPointer < 0) {
          halted = true;
          fault = CpuFault.StackOverflow;
          break;
        }
        this.stackPointer--;
        mem.ram[this.stackPointer] = reg.r[inst.operandA];
        programCounter++;
        break;
      case POP:
        if (this.stackPointer > mem.ram.length) {
          halted = true;
          fault = CpuFault.StackUnderflow;
          break;
        }
        reg.r[inst.operandA] = mem.ram[this.stackPointer];
        this.stackPointer++;
        programCounter++;
        break;
      case CMP:
        int temp = reg.r[inst.operandA] - reg.r[inst.operandB];
        flag.zero = (temp == 0);
        flag.negative = (temp < 0);
        programCounter++;
        break;
      case JMP:
        programCounter = inst.operandA - 1;
        break;
      case JL:
        if (flag.negative) {
          programCounter = inst.operandA - 1;
        } else {
          programCounter++;
        }
        break;
      case JG:
        if (flag.zero == false && flag.negative == false) {
          programCounter = inst.operandA - 1;
        } else {
          programCounter++;
        }
        break;
      case JNZ:
        if (flag.zero != false) {
          programCounter = inst.operandA - 1;
        } else {
          programCounter++;
        }
        break;
      case JZ:
        if (flag.zero) {
          programCounter = inst.operandA - 1;
        } else {
          programCounter++;
        }
        break;
      case HALT:
        halted = true;
        break;
      case null:
      default:
        fault = CpuFault.ILLEGAL_INSTRUCTION;
        halted = true;
    }
  }

  private void store(Instruction inst, Register reg, Memory mem) {
    switch (inst.opcode) {
      case LOADM:
        if (inst.operandB > mem.ram.length) {
          halted = true;
          fault = CpuFault.INVALID_MEM;
          break;
        }
        reg.r[inst.operandB] = mem.ram[inst.operandA];
        programCounter++;
        break;
      case STOREM:
        if (inst.operandB > mem.ram.length) {
          halted = true;
          fault = CpuFault.INVALID_MEM;
          break;
        }
        mem.ram[inst.operandB] = reg.r[inst.operandA];
        programCounter++;
        break;
      case SUB:
        int sub = reg.r[inst.operandA] - reg.r[inst.operandB];
        reg.r[inst.operandA] = sub;
        flag.zero = (sub == 0);
        flag.negative = (sub < 0);
        programCounter++;
        break;
      case ADD:
        int add = reg.r[inst.operandA] + reg.r[inst.operandB];
        reg.r[inst.operandA] = add;
        flag.zero = (add == 0);
        flag.negative = (add < 0);
        programCounter++;
        break;
      case LOAD:
        reg.r[inst.operandA] = inst.operandB;
        programCounter++;
        break;
      default:
        break;
    }
  }
}
