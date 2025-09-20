package com.fss.core.fssCalculation.controller;


import com.fss.core.fssCalculation.controller.utility.DefaultInput;
import com.fss.core.fssCalculation.modal.input.CentralProfileInput;
import com.fss.core.fssCalculation.modal.input.SlidingInput;
import jakarta.servlet.http.HttpSession;
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
    public String calculate(@ModelAttribute("sliding_input") SlidingInput slidingInput, @ModelAttribute("inputHistory") List<Map<String, Object>> history, Model model, HttpSession session) {


        // Keep input values so form can re-render with them
        model.addAttribute("sliding_input", slidingInput);
        model.addAttribute("Ixx", 9.9);
        model.addAttribute("deflection", 9.9);
        model.addAttribute("activeMenu", "sliding");

        Map<String,Object> inputs = new LinkedHashMap<>();
        inputs.put("Unsupported Length (mm)", slidingInput.getUnsupportedLength());
        inputs.put("Grid Length (mm)", slidingInput.getGridLength());
        inputs.put("Wind Pressure (kN/m²)", slidingInput.getWindPressure());
        inputs.put("Glass Thickness (mm)", slidingInput.getGlassThickness());
        inputs.put("Central Meeting Profile", slidingInput.getCentralMeetingProfile());

        session.setAttribute("unsupportedLength", slidingInput.getUnsupportedLength());
        session.setAttribute("gridLength", slidingInput.getGridLength());
        session.setAttribute("windPressure", slidingInput.getWindPressure());
        session.setAttribute("typeOfGlazing","Sliding window");
        session.setAttribute("slidingInput", slidingInput);

        // entry wrapper
        Map<String,Object> entry = new LinkedHashMap<>();
        entry.put("formName", "Combined Input");   // friendly title
        entry.put("inputs", inputs);

        history.add(entry);

        model.addAttribute("slidingResult", true);
        return "glazing-form"; // or redirect to a result page
    }


    @GetMapping("/centralProfileCheck")
    public String centralProfileCheck(@RequestParam(required = false) String activeMenu, Model model, HttpSession session) {

        if (activeMenu == null) {
            activeMenu = "sliding"; // default tab
        }

        // keep previous inputs if needed
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("sliding_input", defaultInput.prepareSlidingWindowInput());

        model.addAttribute("show_central_profile_form", true);

        return "glazing-form"; // loads your main page
    }

    @PostMapping("/submitCentralProfiles")
    public String submit(@ModelAttribute CentralProfileInput input) {
        // process input.getShutterA(), input.getShutterB()
        return "glazing-form";
    }

    @GetMapping("/outerProfileCheck")
    public String outerProfileCheck(@RequestParam(required = false) String activeMenu, Model model, HttpSession session) {

        model.addAttribute("show_outer_profile_form", true);

        return "glazing-form"; // loads your main page
    }
}
