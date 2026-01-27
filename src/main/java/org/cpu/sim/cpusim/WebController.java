package org.cpu.sim.cpusim;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
  Parser parser = new Parser();

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("title", "Cpu Simulator");
    model.addAttribute("registerA", 0);
    model.addAttribute("registerB", 0);
    model.addAttribute("registerC", 0);
    model.addAttribute("registerD", 0);
    return "index";
  }

  void parseInstruction(String line) {
    if (line.isBlank()) {
      return;
    }

  }

  Instruction parse(String line) {
    return parser.parse(line);
  }

  @PostMapping("/compile-run")
  public String getInstruction(@RequestParam String program, Model model) {
    var cpu = new CpuLand();

    var programText = Arrays.stream(program.split("\r\n"));

    programText.filter(l -> !l.isBlank()).map(this::parse).forEach(i -> cpu.program.add(i));

    while (!cpu.halted) {
      cpu.tick();
    }

    model.addAttribute("title", "Cpu Simulator");
    model.addAttribute("registerA", cpu.reg.r[0]);
    model.addAttribute("registerB", cpu.reg.r[1]);
    model.addAttribute("registerC", cpu.reg.r[2]);
    model.addAttribute("registerD", cpu.reg.r[3]);

    model.addAttribute("program", program);

    if (cpu.fault != CpuFault.None) {
      var error = parser.parseError(cpu.fault);
      model.addAttribute("error", error);
    }
    return "index";
  }
}
