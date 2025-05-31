package com.fss.core.fssCalculation.controller;

import com.fss.core.fssCalculation.modal.GlazingInput;
import com.fss.core.fssCalculation.service.BendingMomentCal;
import com.fss.core.fssCalculation.service.DeflectionCal;
import com.fss.core.fssCalculation.service.IxxCal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
public class HomeController {


    @PostMapping("/calculate")
    public String calculate(@ModelAttribute GlazingInput input, Model model) {
        // Call your service method to calculate results
        double Ixx = IxxCal.calculateRequiredIxx(input.getTypeOfGlazing(), input.getUnsupportedLength(), input.getGridLength(), input.getWindPressure(), input.getStackBracket());
        double df = DeflectionCal.calculateDeflection(input.getTypeOfGlazing(), input.getUnsupportedLength(), input.getGridLength(), input.getWindPressure(), input.getStackBracket(), Ixx);
        double cf = DeflectionCal.calculateDeflection(input.getTypeOfGlazing(), input.getUnsupportedLength(), input.getGridLength(), input.getWindPressure(), input.getStackBracket(), Ixx);

        double bendingMoment = BendingMomentCal.calculateBendingMoment(input.getTypeOfGlazing(), input.getUnsupportedLength(), input.getGridLength(), input.getWindPressure(), input.getStackBracket());

        BigDecimal roundedMoment = new BigDecimal(bendingMoment).setScale(2, RoundingMode.HALF_UP);

        // Add results back to model
        model.addAttribute("Ixx", Ixx);
        model.addAttribute("df", df);
        model.addAttribute("bm", roundedMoment);
        model.addAttribute("glazingType",input.getTypeOfGlazing());

        // Also return input values so form retains them
        input.setTypeOfGlazing(input.getTypeOfGlazing());
        model.addAttribute("input", input);
        return "glazing-form"; // This is your input form HTML (Thymeleaf)
    }

    @GetMapping("/home")
    public String showForm(Model model) {

        GlazingInput input = new GlazingInput();
        input.setUnsupportedLength(3000);
        input.setGridLength(1200);
        input.setWindPressure(2.35);
        input.setStackBracket(0);
        model.addAttribute("input", input);
        return "glazing-form";
    }

}