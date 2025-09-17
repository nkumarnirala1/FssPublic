package com.fss.core.fssCalculation.controller;


import com.fss.core.fssCalculation.controller.utility.DefaultInput;
import com.fss.core.fssCalculation.modal.input.SlidingInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/window")
@SessionAttributes("inputHistory")
public class WindowController {

    @Autowired
    DefaultInput defaultInput;

    @ModelAttribute("inputHistory")
    public List<Map<String, Object>> inputHistory() {
        return new ArrayList<>();
    }

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
    public String calculate(@ModelAttribute("sliding_input") SlidingInput input, @ModelAttribute("inputHistory") List<Map<String, Object>> history, Model model) {


        // Keep input values so form can re-render with them
        model.addAttribute("sliding_input", input);
        model.addAttribute("Ixx", 9.9);
        model.addAttribute("deflection", 9.9);
        model.addAttribute("activeMenu", "sliding");

        Map<String,Object> inputs = new LinkedHashMap<>();
        inputs.put("Unsupported Length (mm)", input.getUnsupportedLength());
        inputs.put("Grid Length (mm)", input.getGridLength());
        inputs.put("Wind Pressure (kN/m²)", input.getWindPressure());
        inputs.put("Glass Thickness (mm)", input.getGlassThickness());
        inputs.put("Central Meeting Profile", input.getCentralMeetingProfile());

        // entry wrapper
        Map<String,Object> entry = new LinkedHashMap<>();
        entry.put("formName", "Combined Input");   // friendly title
        entry.put("inputs", inputs);

        history.add(entry);

        model.addAttribute("slidingResult", true);
        return "glazing-form"; // or redirect to a result page
    }
}
