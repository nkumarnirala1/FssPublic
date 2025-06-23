package com.fss.core.fssCalculation.controller;

import com.fss.core.fssCalculation.modal.GlazingInput;
import com.fss.core.fssCalculation.service.BendingMomentCal;
import com.fss.core.fssCalculation.service.DeflectionCal;
import com.fss.core.fssCalculation.service.IxxCal;
import com.fss.core.fssCalculation.service.utility.PdfGenerator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
public class HomeController {

    @Autowired
    IxxCal ixxCal;

    @Autowired
    DeflectionCal deflectionCal;


    @Autowired
    BendingMomentCal bendingMomentCal;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping({"/home", "/", "/calculate", "/calculate-deflection"})
    public String showForm(Model model) {

        model.addAttribute("input", prepareDefaultInput());
        return "glazing-form";
    }


    @PostMapping("/calculate")
    public String calculate(@Valid @ModelAttribute("input") GlazingInput input,
                            BindingResult bindingResult,
                            Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "glazing-form"; // Return form with validation errors
        }

        if (input.getUnsupportedLength() <= 0 || input.getGridLength() <= 0 ||
                input.getWindPressure() <= 0 || input.getStackBracket() < 0) {


            return handleError(model, "All input values must be positive and non-zero", input);
        }

        // Continue with calculation
        double Ixx = ixxCal.calculateRequiredIxx(input.getTypeOfGlazing(), input.getUnsupportedLength(), input.getGridLength(), input.getWindPressure(), input.getStackBracket());
        double df = deflectionCal.calculateDeflection(input.getTypeOfGlazing(), input.getUnsupportedLength(), input.getGridLength(), input.getWindPressure(), input.getStackBracket(), Ixx);
        double bendingMoment = bendingMomentCal.calculateBendingMoment(input.getTypeOfGlazing(), input.getUnsupportedLength(), input.getGridLength(), input.getWindPressure(), input.getStackBracket());

        BigDecimal roundedMoment = new BigDecimal(bendingMoment).setScale(2, RoundingMode.HALF_UP);


        prepareModel(model, 0, Ixx, df, roundedMoment, Ixx);


        session.setAttribute("typeOfGlazing", input.getTypeOfGlazing());
        session.setAttribute("unsupportedLength", input.getUnsupportedLength());
        session.setAttribute("gridLength", input.getGridLength());
        session.setAttribute("windPressure", input.getWindPressure());
        session.setAttribute("stackBracket", input.getStackBracket());
        session.setAttribute("Ixx", Ixx);
        session.setAttribute("df", df);
        session.setAttribute("bm", roundedMoment);

        return "glazing-form";
    }

    @PostMapping("/calculate-deflection")
    public String calculateDeflectionFromUserIxx(@RequestParam double userIxx,
                                                 Model model,
                                                 HttpSession session) {
        String typeOfGlazing = (String) session.getAttribute("typeOfGlazing");
        Double gridLength = (Double) session.getAttribute("gridLength");
        Double windPressure = (Double) session.getAttribute("windPressure");
        Double unsupportedLength = (Double) session.getAttribute("unsupportedLength");
        Double stackBracket = (Double) session.getAttribute("stackBracket");


        if (gridLength == null || windPressure == null || unsupportedLength == null) {

            model.addAttribute("input", prepareDefaultInput());

            return "glazing-form";
        }

        addInputToModel(model, unsupportedLength, windPressure, gridLength, stackBracket, typeOfGlazing);


        if (userIxx == 0) {

            return handleError(model, "Please perform the initial Ixx calculation first", null);
        }


        double cf = deflectionCal.calculateDeflection(typeOfGlazing, unsupportedLength, gridLength, windPressure, stackBracket, userIxx);

        prepareModel(model, cf, session.getAttribute("Ixx"), session.getAttribute("df"), session.getAttribute("dm"), userIxx);

        session.setAttribute("userIxx", userIxx);
        session.setAttribute("cf", cf);
        return "glazing-form";
    }

    @GetMapping("/download-pdf")
    public void downloadPdf(HttpServletResponse response, HttpSession session) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=glazing-result.pdf");

        Object ixxObj = session.getAttribute("Ixx");
        double Ixx = ixxObj != null ? (double) ixxObj : 0.0;
        Object dfObj = session.getAttribute("df");
        double df = dfObj!=null ? (double) dfObj : 0.0;

        Object bmObj = session.getAttribute("bm");
        double bm = bmObj instanceof BigDecimal ? ((BigDecimal) bmObj).doubleValue() : 0.0;

        Double cf = (session.getAttribute("cf") instanceof Double) ? (Double) session.getAttribute("cf") : null;

        Object userIxxObj = session.getAttribute("userIxx");
        double userIxx = userIxxObj != null ? (double) userIxxObj : 0.0;


        PdfGenerator.generateResultPdf(response, Ixx, df, bm, cf, userIxx);
    }


    public GlazingInput prepareDefaultInput() {
        GlazingInput input = new GlazingInput();
        input.setUnsupportedLength(3000.0);
        input.setGridLength(1200.0);
        input.setWindPressure(2.35);
        input.setStackBracket(0.0);

        return input;
    }

    public void addInputToModel(Model model, Double unsupportedLength, Double windPressure, Double gridLength, Double stackBracket, String typeOfGlazing) {

        GlazingInput input = new GlazingInput();
        input.setUnsupportedLength(unsupportedLength);
        input.setGridLength(gridLength);
        input.setWindPressure(windPressure);
        input.setStackBracket(stackBracket);
        input.setTypeOfGlazing(typeOfGlazing);
        model.addAttribute("input", input);
    }

    public void prepareModel(Model model, double cf, Object ixx, Object df, Object bm, Object userIxx) {

        if (cf != 0) {
            model.addAttribute("cf", String.format("%.2f", cf));
        }
        model.addAttribute("Ixx", ixx);
        model.addAttribute("df", df);
        model.addAttribute("bm", bm);

        if (userIxx != null) {
            model.addAttribute("userIxx", userIxx);
        }
    }

    public String handleError(Model model, String errorMessage, GlazingInput glazingInput) {
        model.addAttribute("error", errorMessage);

        if (glazingInput == null) {
            model.addAttribute("input", prepareDefaultInput());
        }

        return "glazing-form";
    }




}