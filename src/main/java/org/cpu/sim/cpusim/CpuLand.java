package org.cpu.sim.cpusim;

import java.util.ArrayList;

enum OpCode {
  LOAD, STORE, ADD, JMP, HALT
}

interface Parser {
  static Instruction parse(String line) {
    var cmd = line.replace(",", "").split(" ");
    var ins = new Instruction();
    ins.opcode = OpCode.valueOf(cmd[0]);

    if (ins.opcode == OpCode.HALT) {
      return ins;
    }

    switch (cmd[1]) {
      case "A":
        ins.dest = 0;
        break;
      case "B":
        ins.dest = 1;
        break;
      case "C":
        ins.dest = 2;
        break;
      case "D":
        ins.dest = 3;
        break;
      default:
        throw new IllegalArgumentException("unknow register: " + cmd[1]);
    }

    switch (cmd[2]) {
      case "A":
        ins.src = 0;
        break;
      case "B":
        ins.src = 1;
        break;
      case "C":
        ins.src = 2;
        break;
      case "D":
        ins.src = 3;
        break;
      default:
        if (ins.opcode == OpCode.ADD) {
          throw new IllegalArgumentException("unknow register: " + cmd[2]);
        }
        if (ins.opcode == OpCode.STORE) {
          ins.src = Integer.decode(cmd[2]);
          break;
        }
        ins.src = Integer.parseInt(cmd[2]);
        break;
    }

    return ins;
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

  Integer programCounter = 0;

  ArrayList<Instruction> program = new ArrayList<Instruction>();

  public void tick() {
    Instruction inst = program.get(programCounter);

    switch (inst.opcode) {
      case LOAD:
        reg.r[inst.dest] = inst.src;
        programCounter++;
        break;
      case STORE:
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
    }
  }
}

class Register {
  int[] r = new int[4];
}

class Memory {
  int[] ram = new int[100];
}
