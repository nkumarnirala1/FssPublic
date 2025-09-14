package com.fss.core.fssCalculation.controller;


import com.fss.core.fssCalculation.modal.SlidingInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/window")
public class WindowController {

    @Autowired
    DefaultInput defaultInput;
    // Show form
    @GetMapping("/ui")
    public String showUi(@RequestParam(required = false) String activeMenu, Model model) {
        if (activeMenu == null) {
            activeMenu = "sliding"; // default landing form
        }
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("sliding_input", defaultInput.prepareSlidingWindowInput());

        return "glazing-form"; // loads your main page
    }

    // Handle form submit
    @PostMapping("/calculate")
    public String calculate(@ModelAttribute("sliding_input") SlidingInput input, Model model) {


        // Keep input values so form can re-render with them
        model.addAttribute("sliding_input", input);
        model.addAttribute("Ixx", 9.9);
        model.addAttribute("deflection", 9.9);
        model.addAttribute("activeMenu", "sliding");


        return "glazing-form"; // or redirect to a result page
    }
}
