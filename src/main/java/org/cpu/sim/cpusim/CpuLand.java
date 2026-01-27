package org.cpu.sim.cpusim;

import java.util.ArrayList;

enum OpCode {
  LOAD, STORE, ADD, JMP, HALT;

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

class Parser {
  int parseRegister(String cmd) {
    switch (cmd) {
      case "A":
        return 0;
      case "B":
        return 1;
      case "C":
        return 2;
      case "D":
        return 3;
      default:
        throw new IllegalArgumentException("unknow register: " + cmd);
    }
  }

  Instruction parse(String line) {
    var cmd = line.replace(",", "").split(" ");
    var ins = new Instruction();
    ins.opcode = OpCode.fromString(cmd[0]);

    switch (ins.opcode) {
      case HALT:
        break;
      case JMP:
        ins.dest = Integer.parseInt(cmd[1]);
      case STORE:
        ins.dest = parseRegister(cmd[1]);
        ins.src = Integer.decode(cmd[2]);
        break;
      case LOAD:
        ins.dest = parseRegister(cmd[1]);
        ins.src = Integer.parseInt(cmd[2]);
        break;
      case ADD:
        ins.dest = parseRegister(cmd[1]);
        ins.src = parseRegister(cmd[2]);
        break;
      case null:
      default:
        break;
    }

    return ins;
  }

  String parseError(CpuFault fault) {
    switch (fault) {
      case ILLEGAL_INSTRUCTION:
        return "ILLEGAL INSTRUCTION";
      case INVALID_MEM:
        return "INVALID_MEM";
      case INVALID_PC:
        return "INVALID_PC";
      default:
        return "NONE";
    }
  }
}

class Instruction {
  OpCode opcode;
  int dest;
  int src;
}

public class CpuLand {
  Register reg = new Register();
  Memory mem = new Memory();

  Boolean halted = false;
  CpuFault fault = CpuFault.None;

  Integer programCounter = 0;

  ArrayList<Instruction> program = new ArrayList<Instruction>();

  public void tick() {
    if (programCounter < 0) {
      halted = true;
      fault = CpuFault.INVALID_PC;
      return;
    }

    if (program.size() < 0) {
      halted = true;
      fault = CpuFault.INVALID_PC;
      return;
    }

    if (programCounter > program.size()) {
      halted = true;
      fault = CpuFault.INVALID_PC;
      return;
    }

    Instruction inst = program.get(programCounter);

    switch (inst.opcode) {
      case LOAD:
        reg.r[inst.dest] = inst.src;
        programCounter++;
        break;
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
        // register to register
        reg.r[inst.dest] += reg.r[inst.src];
        programCounter++;
        break;
      case JMP:
        programCounter = inst.dest;
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
}

class Register {
  int[] r = new int[4];

}

class Memory {
  int[] ram = new int[100];
}
