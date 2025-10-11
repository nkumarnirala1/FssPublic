package com.fss.core.fssCalculation.controller;

import com.fss.core.fssCalculation.constants.Constants;
import com.fss.core.fssCalculation.controller.utility.*;

import com.fss.core.fssCalculation.modal.generic.DownloadReportElement;
import com.fss.core.fssCalculation.modal.generic.ExcelElement;
import com.fss.core.fssCalculation.modal.input.*;
import com.fss.core.fssCalculation.modal.output.MullionProfileOutput;
import com.fss.core.fssCalculation.modal.output.TransomOutput;
import com.fss.core.fssCalculation.service.ReportGen.ExcelSheetGenerator;
import com.fss.core.fssCalculation.service.ReportGen.PdfGenerator;
import com.fss.core.fssCalculation.service.ReportGen.Utility;
import com.fss.core.fssCalculation.service.elements.bendingMoment.BendingMomentCal;
import com.fss.core.fssCalculation.service.elements.deflection.DeflectionCal;
import com.fss.core.fssCalculation.service.elements.inertia.IxxCal;
import com.fss.core.fssCalculation.service.elements.mullion.CheckMullionProfile;
import com.fss.core.fssCalculation.service.elements.transom.CheckTransomProfile;

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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("inputHistory")
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
    DefaultInput defaultInput;

    @Autowired
    Utility utility;

    @Autowired
    StoreSessionAttribute storeSessionAttribute;

    @Autowired
    FlowContext flowContext;

    @Autowired
    ControllerHelper controllerHelper;

    @Autowired
    PopulateInputHistory populateInputHistory;


    @ModelAttribute("inputHistory")
    public List<Map<String, Object>> inputHistory() {
        return new ArrayList<>();
    }


    @GetMapping({"/home", "/", "/calculate", "/calculate-deflection"})
    public String home(@RequestParam(required = false) String activeMenu, Model model, @ModelAttribute("inputHistory") List<Map<String, Object>> history) {
        if (activeMenu == null) {
            activeMenu = "sliding"; // default landing form
        }

        flowContext.setActiveMenu(activeMenu);
        flowContext.setActiveForm(new ArrayList<>(List.of("show_window_form")));
        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("sliding_input", defaultInput.prepareSlidingWindowInput());

        return "index";
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

    @PostMapping("/calculateFully")
    public String calculateFully(@Valid @ModelAttribute("input") GlazingInput input,
                                 BindingResult bindingResult,
                                 Model model,
                                 HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "glazing-form"; // Validation error
        }

        if (input.getUnsupportedLength() <= 0 || input.getGridLength() <= 0 ||
                input.getWindPressure() <= 0 || input.getStackBracket() < 0) {
            return handleError(model, "All input values must be positive and non-zero", input);
        }

        // Run calculations (you may adjust logic for Fully Unitized)
        double Ixx = ixxCal.calculateRequiredIxx(
                input.getTypeOfGlazing(),
                input.getUnsupportedLength(),
                input.getGridLength(),
                input.getWindPressure(),
                input.getStackBracket()
        );

        double deflection = deflectionCal.calculateDeflection(
                input.getTypeOfGlazing(),
                input.getUnsupportedLength(),
                input.getGridLength(),
                input.getWindPressure(),
                input.getStackBracket(),
                Ixx
        );

        double bendingMoment = bendingMomentCal.calculateBendingMoment(
                input.getTypeOfGlazing(),
                input.getUnsupportedLength(),
                input.getGridLength(),
                input.getWindPressure(),
                input.getStackBracket()
        );

        BigDecimal roundedMoment = new BigDecimal(bendingMoment).setScale(2, RoundingMode.HALF_UP);

        // Store results in session
        session.setAttribute("typeOfGlazing", input.getTypeOfGlazing());
        session.setAttribute("unsupportedLength", input.getUnsupportedLength());
        session.setAttribute("gridLength", input.getGridLength());
        session.setAttribute("windPressure", input.getWindPressure());
        session.setAttribute("stackBracket", input.getStackBracket());
        session.setAttribute("Ixx", Ixx);
        session.setAttribute("df", deflection);
        session.setAttribute("bm", roundedMoment);

        // Prepare model for results
        prepareModel(model, 0, Ixx, deflection, roundedMoment, Ixx);

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
            model.addAttribute("input", defaultInput.prepareDefaultInput());
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
        String typeOfGlazing = null;
        Double gridLength = null;
        Double windPressure = null;
        Double unsupportedLength = null;
        Double stackBracket = null;

        String activeMenu = flowContext.getActiveMenu();
        model.addAttribute("activeMenu", activeMenu);

        List<String> activeForms = new ArrayList<>();
        activeForms.add("show_mullion_Form");


        if ("sliding".equalsIgnoreCase(activeMenu)) {
            Object slidingObject = flowContext.getInputValuesMap().get("sliding_input");
            if (!("a+b".equalsIgnoreCase(flowContext.getCalculationMethod()))) {
                model.addAttribute("centralProfileTitle", "combined interlock Profile");

            }

            if (slidingObject != null) {
                SlidingInput slidingInput = (SlidingInput) slidingObject;
                gridLength = slidingInput.getGridLength();
                windPressure = slidingInput.getWindPressure();
                unsupportedLength = slidingInput.getUnsupportedLength();
                stackBracket = 0.0;
                typeOfGlazing = "Sliding window";
            }
            model.addAttribute("mullionProfileTitle", "COMBINED INTERLOCK PROFILE");


        } else if ("semi-unitized".equalsIgnoreCase(activeMenu)) {
            Object semiUnitizedObject = flowContext.getInputValuesMap().get("semiUnitized_input");

            if (semiUnitizedObject != null) {
                SemiUnitizedInput semiUnitizedInput = (SemiUnitizedInput) semiUnitizedObject;
                gridLength = semiUnitizedInput.getGridLength();
                windPressure = semiUnitizedInput.getWindPressure();
                unsupportedLength = semiUnitizedInput.getUnsupportedLength();
                stackBracket = 0.0;
                typeOfGlazing = semiUnitizedInput.getCalculationMethod();
            }

            model.addAttribute("mullionProfileTitle", "MULLION PROFILE");
        }

        MullionInput mullionInput = new MullionInput();
        defaultInput.prepareMullionDefaults(model, session, mullionInput);

        // Add the mullionInput object to the model for form binding
        flowContext.getInputValuesMap().put("mullion_input", mullionInput);

        if (gridLength == null || windPressure == null || unsupportedLength == null) {
            model.addAttribute("input", defaultInput.prepareDefaultInput());
            return "glazing-form";//TODO
        }

        addInputToModel(model, unsupportedLength, windPressure, gridLength, stackBracket, typeOfGlazing);

        flowContext.setActiveForm(activeForms);

        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());

        return "glazing-form";
    }

    @PostMapping("/submitMullionProfile")
    public String submitMullionProfile(@ModelAttribute MullionInput mullionInput,
                                       Model model,@ModelAttribute("inputHistory") List<Map<String, Object>> history,
                                       HttpSession session) {

        String typeOfGlazing = (String) session.getAttribute("typeOfGlazing");
        Double gridLength = null;
        Double windPressure = null;
        Double unsupportedLength = null;
        Double stackBracket = null;

        history.add(populateInputHistory.populateHorizontalProfileHistory(mullionInput));
        String activeMenu = flowContext.getActiveMenu();
        model.addAttribute("activeMenu", activeMenu);

        List<String> activeForms = new ArrayList<>();
        activeForms.add("show_mullion_result");


        if ("sliding".equalsIgnoreCase(activeMenu)) {
            Object slidingObject = flowContext.getInputValuesMap().get("sliding_input");

            boolean isCentralProfileCheckRequired = false;
            if (slidingObject != null) {
                SlidingInput slidingInput = (SlidingInput) slidingObject;
                gridLength = slidingInput.getGridLength();
                windPressure = slidingInput.getWindPressure();
                unsupportedLength = slidingInput.getUnsupportedLength();
                stackBracket = 0.0;
                isCentralProfileCheckRequired = slidingInput.getCentralMeetingProfile();
            }

            if (isCentralProfileCheckRequired) {
                activeForms.add("isCentralProfileCheckRequired");
            } else {
                activeForms.add("isOuterProfileCheckRequired");
            }

            model.addAttribute("mullionProfileResultTitle", "Combined Interlock Profile");


        } else if ("semi-unitized".equalsIgnoreCase(activeMenu)) {
            Object semiUnitizedObject = flowContext.getInputValuesMap().get("semiUnitized_input");

            if (semiUnitizedObject != null) {
                SemiUnitizedInput semiUnitizedInput = (SemiUnitizedInput) semiUnitizedObject;
                gridLength = semiUnitizedInput.getGridLength();
                windPressure = semiUnitizedInput.getWindPressure();
                unsupportedLength = semiUnitizedInput.getUnsupportedLength();
                stackBracket = 0.0;
            }

            activeForms.add("isTransomProfileCheckRequired");
            model.addAttribute("mullionProfileResultTitle", "Mullion Profile");
        }

        if (gridLength == null || windPressure == null || unsupportedLength == null) {
            model.addAttribute("input", defaultInput.prepareDefaultInput());
            return "glazing-form";//TODO
        }

        GlazingInput glazingInput = new GlazingInput();
        glazingInput.setTypeOfGlazing(typeOfGlazing);
        glazingInput.setGridLength(gridLength);
        glazingInput.setStackBracket(stackBracket);
        glazingInput.setUnsupportedLength(unsupportedLength);
        glazingInput.setWindPressure(windPressure);

        double bendingMoment = session.getAttribute("bm") != null
                ? ((BigDecimal) session.getAttribute("bm")).doubleValue()
                : 0.0;
        // Prepare defaults and add mullionInput to model
        defaultInput.prepareMullionDefaults(model, session, mullionInput);
        model.addAttribute("mullionInput", mullionInput);

        if (glazingInput.getTypeOfGlazing() == null) {
            glazingInput.setTypeOfGlazing(session.getAttribute("typeOfGlazing").toString());
        }

        try {
            MullionProfileOutput mullionProfileOutput = checkMullionProfile.checkForMullionprofile(mullionInput, glazingInput, bendingMoment);
            storeSessionAttribute.populateMullionSessionAttribute(mullionInput, mullionProfileOutput, session);
            Map<String, Boolean> mullionProfileResult = mullionProfileOutput.getResultMap();
            model.addAttribute("bendingStress", mullionProfileResult.get("bendingStress"));
            model.addAttribute("shearStress", mullionProfileResult.get("shearStress"));

        } catch (Exception ex) {
            return handleErrorMullion(model, session, "All input values must be positive and non-zero", mullionInput);
        }

        flowContext.setActiveForm(activeForms);

        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());

        DownloadReportElement downloadReportElement = new DownloadReportElement(Constants.OUTER_LEG_CHECK_EXCEL);
        downloadReportElement.getObjectList().add("");//TODO add output Object
        flowContext.getDownloadFormList().add(downloadReportElement);

        return "glazing-form";
    }

    @GetMapping("/checkTransom")
    public String checkTransomProfile(Model model, HttpSession session) {
        String typeOfGlazing = null;
        Double gridLength = null;
        Double windPressure = null;
        Double unsupportedLength = null;

        String activeMenu = flowContext.getActiveMenu();
        model.addAttribute("activeMenu", activeMenu);

        List<String> activeForms = new ArrayList<>();
        activeForms.add("show_transom_form");
        if ("semi-unitized".equalsIgnoreCase(activeMenu)) {
            Object semiUnitizedObject = flowContext.getInputValuesMap().get("semiUnitized_input");

            if (semiUnitizedObject != null) {
                SemiUnitizedInput semiUnitizedInput = (SemiUnitizedInput) semiUnitizedObject;
                gridLength = semiUnitizedInput.getGridLength();
                windPressure = semiUnitizedInput.getWindPressure();
                unsupportedLength = semiUnitizedInput.getUnsupportedLength();
                typeOfGlazing = semiUnitizedInput.getCalculationMethod();
            }

        }

        if (gridLength == null || windPressure == null || unsupportedLength == null) {
            model.addAttribute("input", defaultInput.prepareDefaultInput());
            return "glazing-form";//TODO
        }

        TransomInput transomInput = new TransomInput();
        defaultInput.prepareTransomDefaults(model, session, transomInput);
        model.addAttribute("transomInput", transomInput);

        flowContext.setActiveForm(activeForms);
        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());

        return "glazing-form";
    }

    @PostMapping("/submitTransomProfile")
    public String submitTransomProfile(@ModelAttribute TransomInput transomInput,
                                       Model model,
                                       HttpSession session) {

        String typeOfGlazing = null;
        Double gridLength = null;
        Double windPressure = null;
        Double unsupportedLength = null;
        Double stackBracket = 0.0;

        String activeMenu = flowContext.getActiveMenu();
        model.addAttribute("activeMenu", activeMenu);

        List<String> activeForms = new ArrayList<>();
        activeForms.add("show_transom_result");
        if ("semi-unitized".equalsIgnoreCase(activeMenu)) {
            Object semiUnitizedObject = flowContext.getInputValuesMap().get("semiUnitized_input");

            if (semiUnitizedObject != null) {
                SemiUnitizedInput semiUnitizedInput = (SemiUnitizedInput) semiUnitizedObject;
                gridLength = semiUnitizedInput.getGridLength();
                windPressure = semiUnitizedInput.getWindPressure();
                unsupportedLength = semiUnitizedInput.getUnsupportedLength();
                typeOfGlazing = semiUnitizedInput.getCalculationMethod();
            }

        }

        if (gridLength == null || windPressure == null || unsupportedLength == null) {
            model.addAttribute("input", defaultInput.prepareDefaultInput());
            return "glazing-form";//TODO
        }
        GlazingInput glazingInput = new GlazingInput();
        glazingInput.setTypeOfGlazing(typeOfGlazing);
        glazingInput.setGridLength(gridLength);
        glazingInput.setStackBracket(stackBracket);
        glazingInput.setUnsupportedLength(unsupportedLength);
        glazingInput.setWindPressure(windPressure);

        defaultInput.prepareTransomDefaults(model, session, transomInput);
        try {
            TransomOutput transomOutput = checkTransomProfile.checkForTransomProfile(transomInput);

            Map<String, Boolean> transomProfileResult = transomOutput.getResultMap();

            model.addAttribute("bendingStress", transomProfileResult.get("bendingStress"));
            model.addAttribute("shearStress", transomProfileResult.get("shearStress"));

        } catch (Exception ex) {
            return handleErrorTransom(model, session, "All input values must be positive and non-zero", transomInput);
        }

        // add back glazing values from session if needed
        model.addAttribute("unsupportedLength", unsupportedLength);
        model.addAttribute("gridLength", gridLength);
        model.addAttribute("windPressure", windPressure);
        model.addAttribute("stackBracket", stackBracket);
        model.addAttribute("typeOfGlazing", typeOfGlazing);
        flowContext.setActiveForm(activeForms);
        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());

        DownloadReportElement downloadReportElement = new DownloadReportElement(Constants.OUTER_LEG_CHECK_EXCEL);
        downloadReportElement.getObjectList().add("");//TODO add output Object
        flowContext.getDownloadFormList().add(downloadReportElement);
        return "glazing-form";
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
            model.addAttribute("input", defaultInput.prepareDefaultInput());
        }

        return "glazing-form";
    }

    public String handleErrorMullion(Model model, HttpSession session, String errorMessage, MullionInput mullionInput) {
        model.addAttribute("error", errorMessage);
        model.addAttribute("mullionInput", mullionInput);
        return "mullion-form";
    }

    public String handleErrorTransom(Model model, HttpSession session, String errorMessage, TransomInput transomInput) {
        model.addAttribute("error", errorMessage);
        return "transom-form";
    }


}
