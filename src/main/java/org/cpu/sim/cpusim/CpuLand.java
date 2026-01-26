package org.cpu.sim.cpusim;

import java.util.ArrayList;

public class CpuLand {
  Register reg = new Register();
  Memory mem = new Memory();

  Integer programCounter = 0;

  public String hello() {
    return "HH";
  }
}

class Register {
  ArrayList<Integer> allocation = new ArrayList<Integer>(4);
}

class Memory {
  ArrayList<Integer> ram = new ArrayList<Integer>(100);
}
