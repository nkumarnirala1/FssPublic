package com.fss.core.fssCalculation.controller;

import com.fss.core.fssCalculation.service.estimation.Interface.IEstimation;
import com.fss.core.fssCalculation.service.utility.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EstimationController {

    @Autowired
    IEstimation iEstimation;

    @GetMapping("/estimation")
    public String showEstimationPage(Model model, HttpSession session) {

        Double unsupportedLength = (Double) session.getAttribute("unsupportedLength");
        Double gridLength = (Double) session.getAttribute("gridLength");
        String typeOfGlazing = (String) session.getAttribute("typeOfGlazing");

        model.addAttribute("unsupportedLength", unsupportedLength);
        model.addAttribute("gridLength", gridLength);
        model.addAttribute("typeOfGlazing", typeOfGlazing);

        return "estimation";
    }

    @PostMapping("/submit-estimation")
    public String processEstimation(
            @RequestParam String typeOfGlazing,
            @RequestParam double unsupportedLength,
            @RequestParam double gridLength,
            @RequestParam double rate,
            @RequestParam(required = false) String slidingType,
            @RequestParam(required = false) Double endShutterCrossSectionalArea,
            @RequestParam(required = false) Double innerLockACrossSectionalArea,
            @RequestParam(required = false) Double innerLockBCrossSectionalArea,
            @RequestParam(required = false) Double outerCrossSectionalArea,
            Model model
    ) {
        // ✅ Basic validation
        if (unsupportedLength <= 0 || gridLength <= 0 || rate <= 0) {
            model.addAttribute("error", "All dimensions and rate must be positive values.");
            return "estimation";
        }
        double estimatedCost = iEstimation.profileEstimation(endShutterCrossSectionalArea, innerLockACrossSectionalArea, innerLockBCrossSectionalArea, outerCrossSectionalArea, gridLength, unsupportedLength, rate);

        model.addAttribute("estimatedCost", Utility.formatAmount(estimatedCost));
        return "estimation"; // Create this page to show result
    }

}
