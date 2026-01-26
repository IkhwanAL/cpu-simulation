package org.cpu.sim.cpusim;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("title", "Cpu Simulator");
    model.addAttribute("registerA", 0);
    model.addAttribute("registerB", 0);
    return "index";
  }
}
