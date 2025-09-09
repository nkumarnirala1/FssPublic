package com.fss.core.fssCalculation.controller;

import com.fss.core.fssCalculation.modal.ExcelElement;
import com.fss.core.fssCalculation.modal.GlazingInput;
import com.fss.core.fssCalculation.modal.MullionInput;
import com.fss.core.fssCalculation.modal.TransomInput;
import com.fss.core.fssCalculation.service.elements.bendingMoment.BendingMomentCal;
import com.fss.core.fssCalculation.service.elements.deflection.DeflectionCal;
import com.fss.core.fssCalculation.service.elements.momentCal.IxxCal;
import com.fss.core.fssCalculation.service.elements.mullion.CheckMullionProfile;
import com.fss.core.fssCalculation.service.elements.transom.CheckTransomProfile;
import com.fss.core.fssCalculation.service.ReportGen.ExcelSheetGenerator;
import com.fss.core.fssCalculation.service.ReportGen.PdfGenerator;
import com.fss.core.fssCalculation.service.ReportGen.Utility;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    IxxCal ixxCal;

    @Autowired
    DeflectionCal deflectionCal;


    @Autowired
    BendingMomentCal bendingMomentCal;

    @Autowired
    ExcelSheetGenerator excelDownloadService;

    @Autowired
    CheckMullionProfile checkMullionProfile;

    @Autowired
    CheckTransomProfile checkTransomProfile;

    @Autowired
    ExcelSheetGenerator excelSheetGenerator;

    @Autowired
    PdfGenerator pdfGenerator;

    @Autowired
    Utility utility;

    @GetMapping("show")
    public String showMullionForm(Model model) {
        // you can add attributes if needed
        model.addAttribute("showMullionForm", true);
        return "transom-form";  // Thymeleaf looks for mullion-form.html
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
        double Ixx = ixxCal.calculateRequiredIxx(input.getTypeOfGlazing(), input.getUnsupportedLength(), input.getGridLength(), input.getWindPressure(), input.getStackBracket(), session);
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
        session.setAttribute("bm", Utility.roundTo2Decimal(bendingMoment));

        model.addAttribute("showMullionForm", false);
        model.addAttribute("showTransomForm", false);
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
        model.addAttribute("showMullionForm", false);
        model.addAttribute("showTransomForm", false);
        return "glazing-form";
    }

    @PostMapping("/checkMullion")
    public String checkMullionProfile(Model model, HttpSession session) {
        String typeOfGlazing = (String) session.getAttribute("typeOfGlazing");
        Double gridLength = (Double) session.getAttribute("gridLength");
        Double windPressure = (Double) session.getAttribute("windPressure");
        Double unsupportedLength = (Double) session.getAttribute("unsupportedLength");
        Double stackBracket = (Double) session.getAttribute("stackBracket");


        if (gridLength == null || windPressure == null || unsupportedLength == null) {

            model.addAttribute("input", prepareDefaultInput());

            return "glazing-form";//TODO
        }

        addInputToModel(model, unsupportedLength, windPressure, gridLength, stackBracket, typeOfGlazing);

        prepareMullionDefaults(model, session, new MullionInput());


        return "mullion-form";
    }

    @PostMapping("/submitMullionProfile")
    public String submitMullionProfile(@ModelAttribute MullionInput mullionInput,
                                       Model model,
                                       HttpSession session) {

        String typeOfGlazing = (String) session.getAttribute("typeOfGlazing");
        Double gridLength = (Double) session.getAttribute("gridLength");
        Double windPressure = (Double) session.getAttribute("windPressure");
        Double unsupportedLength = (Double) session.getAttribute("unsupportedLength");
        Double stackBracket = (Double) session.getAttribute("stackBracket");


        if (gridLength == null || windPressure == null || unsupportedLength == null) {

            model.addAttribute("input", prepareDefaultInput());

            return "glazing-form";//TODO
        }
        GlazingInput glazingInput = new GlazingInput();
        glazingInput.setTypeOfGlazing(typeOfGlazing);
        glazingInput.setGridLength(gridLength);
        glazingInput.setStackBracket(stackBracket);
        glazingInput.setUnsupportedLength(unsupportedLength);
        glazingInput.setWindPressure(windPressure);

        double bendingMoment = (session.getAttribute("bm") instanceof Double)
                ? (Double) session.getAttribute("bm") : 0.0;

        try {
            double cf = deflectionCal.calculateDeflection(typeOfGlazing, unsupportedLength, gridLength, windPressure, stackBracket, mullionInput.getUserIxx());
            session.setAttribute("cf", cf);

            Map<String, Boolean> mullionprofileResult = checkMullionProfile.checkForMullionprofile(mullionInput, glazingInput, bendingMoment, session);

            model.addAttribute("bendingStress", mullionprofileResult.get("bendingStress"));
            model.addAttribute("shearStress", mullionprofileResult.get("shearStress"));

        } catch (Exception ex) {
            return handleErrorMullion(model, session, "All input values must be positive and non-zero", mullionInput);

        }


        // add back glazing values from session if needed
        model.addAttribute("unsupportedLength", unsupportedLength);
        model.addAttribute("gridLength", gridLength);
        model.addAttribute("windPressure", windPressure);
        model.addAttribute("stackBracket", stackBracket);
        model.addAttribute("typeOfGlazing", typeOfGlazing);

        model.addAttribute("mullion_input", mullionInput);

        return "mullion-form";
    }


    @PostMapping("/checkTransom")
    public String checkTransomProfile(Model model, HttpSession session) {
        String typeOfGlazing = (String) session.getAttribute("typeOfGlazing");
        Double gridLength = (Double) session.getAttribute("gridLength");
        Double windPressure = (Double) session.getAttribute("windPressure");
        Double unsupportedLength = (Double) session.getAttribute("unsupportedLength");
        Double stackBracket = (Double) session.getAttribute("stackBracket");


        if (gridLength == null || windPressure == null || unsupportedLength == null) {

            model.addAttribute("input", prepareDefaultInput());

            return "glazing-form";//TODO
        }


        TransomInput transomInput = new TransomInput();
        prepareTransomDefaults(model, session, transomInput);
        model.addAttribute("transomInput", transomInput);

        return "transom-form";
    }

    @PostMapping("/submitTransomProfile")
    public String submitTransomProfile(@ModelAttribute TransomInput transomInput,
                                       Model model,
                                       HttpSession session) {

        String typeOfGlazing = (String) session.getAttribute("typeOfGlazing");
        Double gridLength = (Double) session.getAttribute("gridLength");
        Double windPressure = (Double) session.getAttribute("windPressure");
        Double unsupportedLength = (Double) session.getAttribute("unsupportedLength");
        Double stackBracket = (Double) session.getAttribute("stackBracket");


        if (gridLength == null || windPressure == null || unsupportedLength == null) {

            model.addAttribute("input", prepareDefaultInput());

            return "glazing-form";//TODO
        }
        GlazingInput glazingInput = new GlazingInput();
        glazingInput.setTypeOfGlazing(typeOfGlazing);
        glazingInput.setGridLength(gridLength);
        glazingInput.setStackBracket(stackBracket);
        glazingInput.setUnsupportedLength(unsupportedLength);
        glazingInput.setWindPressure(windPressure);


        prepareTransomDefaults(model, session, transomInput);
        try {
            Map<String, Boolean> transomprofileResult = checkTransomProfile.checkForTransomProfile(transomInput);

            model.addAttribute("bendingStress", transomprofileResult.get("bendingStress"));
            model.addAttribute("shearStress", transomprofileResult.get("shearStress"));

        } catch (Exception ex) {
            return handleErrorTransom(model, session, "All input values must be positive and non-zero", transomInput);

        }


        // add back glazing values from session if needed
        model.addAttribute("unsupportedLength", unsupportedLength);
        model.addAttribute("gridLength", gridLength);
        model.addAttribute("windPressure", windPressure);
        model.addAttribute("stackBracket", stackBracket);
        model.addAttribute("typeOfGlazing", typeOfGlazing);

        return "transom-form";
    }


    @GetMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(HttpServletResponse response, HttpSession session) throws IOException {

        utility.populateManualCalculatedValues(session);



        ArrayList<String> mullionDesignList = new ArrayList<>(Arrays.asList("Deflection_Check", "Stress_Check"));
        Map<String, ArrayList<ExcelElement>> excelElementListSheetMap = excelSheetGenerator.enrichElements(mullionDesignList, session);

        try {
            // Get modified Excel stream
            var bos = excelDownloadService.generateExcelReport(mullionDesignList, excelElementListSheetMap);

            // Convert to PDF
            byte[] pdfBytes = pdfGenerator.convertExcelToPdf(bos);

            // Return as downloadable file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=updated.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error: " + e.getMessage()).getBytes());
        }
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

    public String handleErrorMullion(Model model, HttpSession session, String errorMessage, MullionInput mullionInput) {
        model.addAttribute("error", errorMessage);

        return "mullion-form";
    }

    public String handleErrorTransom(Model model, HttpSession session, String errorMessage, TransomInput transomInput) {
        model.addAttribute("error", errorMessage);

        return "transom-form";
    }

    private void prepareMullionDefaults(Model model, HttpSession session, MullionInput mullionInput) {

        // Glass Thickness
        Double sessionGlassThickness = (session.getAttribute("glassThickness") instanceof Double)
                ? (Double) session.getAttribute("glassThickness") : null;
        model.addAttribute("glassThickness",
                mullionInput.getGlassThickness() != 0.0 ? mullionInput.getGlassThickness() :
                        (sessionGlassThickness != null && sessionGlassThickness != 0.0 ? sessionGlassThickness : 12.0));

        // Cross Sectional Area
        Double sessionCrossSectionalArea = (session.getAttribute("crossSectionalArea") instanceof Double)
                ? (Double) session.getAttribute("crossSectionalArea") : null;
        model.addAttribute("crossSectionalArea",
                mullionInput.getCrossSectionalArea() != 0.0 ? mullionInput.getCrossSectionalArea() :
                        (sessionCrossSectionalArea != null && sessionCrossSectionalArea != 0.0 ? sessionCrossSectionalArea : 6.25));

        // Transom to Transom Distance
        Double sessionTransomToTransomDistance = (session.getAttribute("transomToTransomDistance") instanceof Double)
                ? (Double) session.getAttribute("transomToTransomDistance") : null;
        model.addAttribute("transomToTransomDistance",
                mullionInput.getTransomToTransomDistance() != 0.0 ? mullionInput.getTransomToTransomDistance() :
                        (sessionTransomToTransomDistance != null && sessionTransomToTransomDistance != 0.0 ? sessionTransomToTransomDistance : 1200.0));

        // b
        Double sessionB = (session.getAttribute("b") instanceof Double) ? (Double) session.getAttribute("b") : null;
        model.addAttribute("b",
                mullionInput.getB() != 0.0 ? mullionInput.getB() :
                        (sessionB != null && sessionB != 0.0 ? sessionB : 60.0));

        // a
        Double sessionA = (session.getAttribute("a") instanceof Double) ? (Double) session.getAttribute("a") : null;
        model.addAttribute("a",
                mullionInput.getA() != 0.0 ? mullionInput.getA() :
                        (sessionA != null && sessionA != 0.0 ? sessionA : 100.0));

        // t2
        Double sessionT2 = (session.getAttribute("t2") instanceof Double) ? (Double) session.getAttribute("t2") : null;
        model.addAttribute("t2",
                mullionInput.getT2() != 0.0 ? mullionInput.getT2() :
                        (sessionT2 != null && sessionT2 != 0.0 ? sessionT2 : 3.0));

        // t1
        Double sessionT1 = (session.getAttribute("t1") instanceof Double) ? (Double) session.getAttribute("t1") : null;
        model.addAttribute("t1",
                mullionInput.getT1() != 0.0 ? mullionInput.getT1() :
                        (sessionT1 != null && sessionT1 != 0.0 ? sessionT1 : 2.0));

        // iyy
        Double sessionIyy = (session.getAttribute("iyy") instanceof Double) ? (Double) session.getAttribute("iyy") : null;
        model.addAttribute("iyy",
                mullionInput.getIyy() != 0.0 ? mullionInput.getIyy() :
                        (sessionIyy != null && sessionIyy != 0.0 ? sessionIyy : 46.24));

        // boundingboxy
        Double sessionBoundingboxy = (session.getAttribute("boundingboxy") instanceof Double)
                ? (Double) session.getAttribute("boundingboxy") : null;
        model.addAttribute("boundingboxy",
                mullionInput.getBoundingboxy() != 0.0 ? mullionInput.getBoundingboxy() :
                        (sessionBoundingboxy != null && sessionBoundingboxy != 0.0 ? sessionBoundingboxy : 2.0));

        Double sessionBoundingboxx = (session.getAttribute("boundingboxx") instanceof Double)
                ? (Double) session.getAttribute("boundingboxx") : null;
        model.addAttribute("boundingboxx",
                mullionInput.getBoundingboxy() != 0.0 ? mullionInput.getBoundingboxy() :
                        (sessionBoundingboxx != null && sessionBoundingboxx != 0.0 ? sessionBoundingboxx : 2.0));

        // userIxx
        Double sessionUserIxx = (session.getAttribute("userIxx") instanceof Double)
                ? (Double) session.getAttribute("userIxx") : null;
        model.addAttribute("userIxx",
                mullionInput.getUserIxx() != 0.0 ? mullionInput.getUserIxx() :
                        (sessionUserIxx != null && sessionUserIxx != 0.0 ? sessionUserIxx : 200.0));

        // Save to session so they persist
        session.setAttribute("glassThickness", model.getAttribute("glassThickness"));
        session.setAttribute("crossSectionalArea", model.getAttribute("crossSectionalArea"));
        session.setAttribute("transomToTransomDistance", model.getAttribute("transomToTransomDistance"));
        session.setAttribute("b", model.getAttribute("b"));
        session.setAttribute("a", model.getAttribute("a"));
        session.setAttribute("t2", model.getAttribute("t2"));
        session.setAttribute("t1", model.getAttribute("t1"));
        session.setAttribute("iyy", model.getAttribute("iyy"));
        session.setAttribute("boundingboxy", model.getAttribute("boundingboxy"));
        session.setAttribute("boundingboxx", model.getAttribute("boundingboxx"));
        session.setAttribute("userIxx", model.getAttribute("userIxx"));
    }

    private void prepareTransomDefaults(Model model, HttpSession session, TransomInput transomInput) {

        // Glass Thickness
        Double sessionGlassThickness = (session.getAttribute("glassThickness") instanceof Double)
                ? (Double) session.getAttribute("glassThickness") : null;
        model.addAttribute("glassThickness",
                transomInput.getGlassThickness() != 0.0 ? transomInput.getGlassThickness() :
                        (sessionGlassThickness != null && sessionGlassThickness != 0.0 ? sessionGlassThickness : 12.0));

        // Cross Sectional Area (specific to Transom)
        Double sessionCrossSectionalArea = (session.getAttribute("transomCrossSectionalArea") instanceof Double)
                ? (Double) session.getAttribute("transomCrossSectionalArea") : null;
        model.addAttribute("transomCrossSectionalArea",
                transomInput.getTransomCrossSectionalArea() != 0.0 ? transomInput.getTransomCrossSectionalArea() :
                        (sessionCrossSectionalArea != null && sessionCrossSectionalArea != 0.0 ? sessionCrossSectionalArea : 9.6));

        // Unsupported Length
        Double sessionUnsupportedLength = (session.getAttribute("transomUnsupportedLength") instanceof Double)
                ? (Double) session.getAttribute("transomUnsupportedLength") : null;
        model.addAttribute("transomUnsupportedLength",
                transomInput.getTransomUnsupportedLength() != 0.0 ? transomInput.getTransomUnsupportedLength() :
                        (sessionUnsupportedLength != null && sessionUnsupportedLength != 0.0 ? sessionUnsupportedLength : 1200.0));

        // Section Width B
        Double sessionB = (session.getAttribute("sectionWidthB") instanceof Double) ? (Double) session.getAttribute("sectionWidthB") : null;
        model.addAttribute("sectionWidthB",
                transomInput.getSectionWidthB() != 0.0 ? transomInput.getSectionWidthB() :
                        (sessionB != null && sessionB != 0.0 ? sessionB : 10.0));

        // Depth of Section (a)
        Double sessionDepth = (session.getAttribute("depthOfSectionTransom") instanceof Double) ? (Double) session.getAttribute("depthOfSectionTransom") : null;
        model.addAttribute("depthOfSectionTransom",
                transomInput.getDepthOfSectionTransom() != 0.0 ? transomInput.getDepthOfSectionTransom() :
                        (sessionDepth != null && sessionDepth != 0.0 ? sessionDepth : 101.0));

        // t2
        Double sessionT2 = (session.getAttribute("t2Transom") instanceof Double) ? (Double) session.getAttribute("t2Transom") : null;
        model.addAttribute("t2Transom",
                transomInput.getT2Transom() != 0.0 ? transomInput.getT2Transom() :
                        (sessionT2 != null && sessionT2 != 0.0 ? sessionT2 : 2.6));

        // t1
        Double sessionT1 = (session.getAttribute("thicknessTaTransom_t1") instanceof Double) ? (Double) session.getAttribute("thicknessTaTransom_t1") : null;
        model.addAttribute("thicknessTaTransom_t1",
                transomInput.getThicknessTaTransom_t1() != 0.0 ? transomInput.getThicknessTaTransom_t1() :
                        (sessionT1 != null && sessionT1 != 0.0 ? sessionT1 : 2.6));

        // Iyy
        Double sessionIyy = (session.getAttribute("transomIyy") instanceof Double) ? (Double) session.getAttribute("transomIyy") : null;
        model.addAttribute("transomIyy",
                transomInput.getTransomIyy() != 0.0 ? transomInput.getTransomIyy() :
                        (sessionIyy != null && sessionIyy != 0.0 ? sessionIyy : 150.9));

        // Ixx
        Double sessionIxx = (session.getAttribute("transomIxx") instanceof Double) ? (Double) session.getAttribute("transomIxx") : null;
        model.addAttribute("transomIxx",
                transomInput.getTransomIxx() != 0.0 ? transomInput.getTransomIxx() :
                        (sessionIxx != null && sessionIxx != 0.0 ? sessionIxx : 46.24));

        // Bounding Box Y
        Double sessionBoundingBoxY = (session.getAttribute("transomBoundingBoxY") instanceof Double)
                ? (Double) session.getAttribute("transomBoundingBoxY") : null;
        model.addAttribute("transomBoundingBoxY",
                transomInput.getTransomBoundingBoxY() != 0.0 ? transomInput.getTransomBoundingBoxY() :
                        (sessionBoundingBoxY != null && sessionBoundingBoxY != 0.0 ? sessionBoundingBoxY : 2.85));

        // Save to session
        session.setAttribute("glassThickness", model.getAttribute("glassThickness"));
        session.setAttribute("transomCrossSectionalArea", model.getAttribute("transomCrossSectionalArea"));
        session.setAttribute("transomUnsupportedLength", model.getAttribute("transomUnsupportedLength"));
        session.setAttribute("sectionWidthB", model.getAttribute("sectionWidthB"));
        session.setAttribute("depthOfSectionTransom", model.getAttribute("depthOfSectionTransom"));
        session.setAttribute("t2Transom", model.getAttribute("t2Transom"));
        session.setAttribute("thicknessTaTransom_t1", model.getAttribute("thicknessTaTransom_t1"));
        session.setAttribute("transomIyy", model.getAttribute("transomIyy"));
        session.setAttribute("transomIxx", model.getAttribute("transomIxx"));
        session.setAttribute("transomBoundingBoxY", model.getAttribute("transomBoundingBoxY"));
    }


}