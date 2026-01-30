package org.cpu.sim.cpusim;

import java.util.ArrayList;
import java.util.Set;

class Register {
  int[] r = new int[4];
  static final Set<String> CODE = Set.of("A", "B", "C", "D");
}

class Memory {
  int[] ram = new int[100];
}

public class Cpu {
  Memory mem;

  ControlUnit cu = new ControlUnit();
  Register reg = new Register();
  ALU alu = new ALU(0);

  ArrayList<Instruction> program = new ArrayList<Instruction>();

  public void setMemory(Memory ram) {
    mem = ram;
    alu.stackPointer = ram.ram.length;
  }

  public void tick() {
    alu.execute(reg, mem, program);
  }
}
