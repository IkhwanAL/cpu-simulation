package org.cpu.sim.cpusim;

import java.util.ArrayList;

public class ALU {
  Boolean halted = false;
  CpuFault fault = CpuFault.None;
  Integer programCounter = 0;
  Flags flag = new Flags();

  void execute(Register reg, Memory mem, ArrayList<Instruction> program) {
    if (programCounter < 0 || programCounter >= program.size()) {
      halted = true;
      fault = CpuFault.INVALID_PC;
      return;
    }

    Instruction inst = program.get(programCounter);

    switch (inst.opcode) {
      case LOAD:
      case STORE:
      case ADD: // register to register
        this.store(inst, reg, mem);
        break;
      case CMP:
        int temp = reg.r[inst.dest] - reg.r[inst.src];
        flag.zero = (temp == 0);
        flag.negative = (temp < 0);
        programCounter++;
        break;
      case JMP:
        programCounter = inst.dest - 1;
        break;
      case JZ:
        if (flag.zero) {
          programCounter = inst.dest - 1;
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
      case STORE:
        if (inst.src > mem.ram.length) {
          halted = true;
          fault = CpuFault.INVALID_MEM;
          break;
        }
        mem.ram[inst.src] = reg.r[inst.dest];
        programCounter++;
        break;
      case ADD:
        int res = reg.r[inst.dest] + reg.r[inst.src];
        reg.r[inst.dest] = res;
        flag.zero = (res == 0);
        flag.negative = (res < 0);
        programCounter++;
        break;
      case LOAD:
        reg.r[inst.dest] = inst.src;
        programCounter++;
        break;
      default:
        break;
    }
  }
}
