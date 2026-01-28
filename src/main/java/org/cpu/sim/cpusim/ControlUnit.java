package org.cpu.sim.cpusim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class ControlUnit {
  Stream<String> programInstruction;
  HashMap<String, Integer> labels = new HashMap<String, Integer>();
  int instructionLine = 0;

  class RawInstruction {
    OpCode opcode;
    Operand dest;
    Operand src;
  }

  void fetchProgram(String program) {
    programInstruction = Arrays.stream(program.split("\r\n"));
  }

  ArrayList<Instruction> decode() {
    var rawInstruction = new ArrayList<RawInstruction>(programInstruction
        .filter(l -> !l.isBlank())
        .map(this::parse)
        .toList());

    var compiledInstruction = new ArrayList<Instruction>();

    for (RawInstruction instruction : rawInstruction) {
      if (instruction.opcode == null) {
        continue;
      }
      compiledInstruction.add(this.resolver(instruction));
    }

    return compiledInstruction;
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

  private boolean isNumber(String token) {
    for (int i = 0; i < token.length(); i++) {
      if (!Character.isDigit(token.charAt(i)))
        return false;
    }

    return !token.isEmpty();
  }

  private Operand parseOperand(String token) {
    if (token.isEmpty()) {
      return null;
    }

    if (token.startsWith("0x")) {
      return new HexCode(token);
    }

    if (isNumber(token)) {
      return new Immediate(Integer.parseInt(token));
    }

    if (Register.CODE.contains(token)) {
      return new RegisterCode(token);
    }

    return new LabelRef(token);
  }

  private Boolean detectLabel(String token) {
    if (token.endsWith(":")) {
      return true;
    }

    return false;
  }

  private RawInstruction parse(String line) {
    var cmd = line.trim().replace(",", "").split(" ");
    var ins = new RawInstruction();
    ins.opcode = OpCode.fromString(cmd[0]);

    if (this.detectLabel(cmd[0])) {
      String label = cmd[0].substring(0, cmd[0].length() - 1);
      labels.put(label, instructionLine);
    }

    if (cmd.length > 1) {
      ins.dest = parseOperand(cmd[1]);
    }

    if (cmd.length > 2) {
      ins.src = parseOperand(cmd[2]);
    }

    instructionLine++;

    return ins;
  }

  private int resolveInstruction(Operand op) {
    if (op instanceof LabelRef label) {
      Integer addr = labels.get(label.name());

      if (addr == null) {
        throw new IllegalStateException("Unknow Label :" + label.name());
      }

      return addr;
    }

    if (op instanceof HexCode hx) {
      return Integer.decode(hx.value());
    }

    if (op instanceof Immediate imm) {
      return imm.value();
    }

    if (op instanceof RegisterCode rc) {
      return parseRegister(rc.value());
    }

    return 0;
  }

  private Instruction resolver(RawInstruction ins) {
    var instruction = new Instruction();
    instruction.opcode = ins.opcode;
    switch (ins.opcode) {
      case HALT:
        break;
      case JMP:
        instruction.dest = resolveInstruction(ins.dest);
        break;
      case STORE:
        instruction.dest = resolveInstruction(ins.dest);
        instruction.src = resolveInstruction(ins.src);
        break;
      case LOAD:
        instruction.dest = resolveInstruction(ins.dest);
        instruction.src = resolveInstruction(ins.src);
        break;
      case ADD:
        instruction.dest = resolveInstruction(ins.dest);
        instruction.src = resolveInstruction(ins.src);
        break;
      case JZ:
        instruction.dest = resolveInstruction(ins.dest);
        break;
      case null:
      default:
        break;
    }

    return instruction;
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
