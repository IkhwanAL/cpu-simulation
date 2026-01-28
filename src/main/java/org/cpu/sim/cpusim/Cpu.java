package org.cpu.sim.cpusim;

import java.util.ArrayList;

class Register {
  int[] r = new int[4];
}

class Memory {
  int[] ram = new int[100];
}

public class Cpu {
  Memory mem;

  ControlUnit cu = new ControlUnit();
  Register reg = new Register();
  ALU alu = new ALU();

  ArrayList<Instruction> program = new ArrayList<Instruction>();

  public void setMemory(Memory ram) {
    mem = ram;
  }

  public void tick() {
    alu.execute(reg, mem, program);
  }
}
