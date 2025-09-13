package com.fss.core.fssCalculation.controller;


import com.fss.core.fssCalculation.modal.SlidingInput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/window")
public class WindowController {


    // Show form
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("sliding_input", new SlidingInput());
        return "fragments/window"; // or main template that includes window fragment
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
