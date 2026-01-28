package org.cpu.sim.cpusim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class ControlUnit {
  Stream<String> programInstruction;

  void fetchProgram(String program) {
    programInstruction = Arrays.stream(program.split("\r\n"));
  }

  ArrayList<Instruction> decode() {
    return new ArrayList<Instruction>(programInstruction
        .filter(l -> !l.isBlank())
        .map(this::parse)
        .toList());
  }

  private int parseRegister(String cmd) {
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

  private Instruction parse(String line) {
    var cmd = line.replace(",", "").split(" ");
    var ins = new Instruction();
    ins.opcode = OpCode.fromString(cmd[0]);

    switch (ins.opcode) {
      case HALT:
        break;
      case JMP:
        ins.dest = Integer.parseInt(cmd[1]);
        break;
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
      case JZ:
        ins.dest = Integer.parseInt(cmd[1]);
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
