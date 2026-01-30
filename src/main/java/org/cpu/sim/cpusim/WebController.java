package org.cpu.sim.cpusim;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("title", "Cpu Simulator");
    model.addAttribute("registerA", 0);
    model.addAttribute("registerB", 0);
    model.addAttribute("registerC", 0);
    model.addAttribute("registerD", 0);
    return "index";
  }

  private void setModel(Cpu cpu, String program, Model model) {
    model.addAttribute("title", "Cpu Simulator");
    model.addAttribute("registerA", cpu.reg.r[0]);
    model.addAttribute("registerB", cpu.reg.r[1]);
    model.addAttribute("registerC", cpu.reg.r[2]);
    model.addAttribute("registerD", cpu.reg.r[3]);

    model.addAttribute("memorys", cpu.mem.ram);

    model.addAttribute("program", program);
  }

  @PostMapping("/compile-run")
  public String getInstruction(@RequestParam String program, Model model) {
    var cpu = new Cpu();
    cpu.setMemory(new Memory());

    cpu.cu.fetchProgram(program);

    try {
      cpu.program = cpu.cu.decode();
    } catch (IllegalStateException e) {
      setModel(cpu, program, model);
      model.addAttribute("error", "failed to parse program");
      return "index";
    }

    while (!cpu.alu.halted) {
      cpu.tick();
    }

    setModel(cpu, program, model);

    if (cpu.alu.fault != CpuFault.None) {
      var error = cpu.cu.parseError(cpu.alu.fault);
      model.addAttribute("error", error);
    }
    return "index";
  }
}
