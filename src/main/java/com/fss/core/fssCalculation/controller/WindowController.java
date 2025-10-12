package com.fss.core.fssCalculation.controller;


import com.fss.core.fssCalculation.constants.Constants;
import com.fss.core.fssCalculation.controller.utility.ControllerHelper;
import com.fss.core.fssCalculation.controller.utility.DefaultInput;
import com.fss.core.fssCalculation.controller.utility.FlowContext;
import com.fss.core.fssCalculation.controller.utility.PopulateInputHistory;
import com.fss.core.fssCalculation.modal.generic.DownloadReportElement;
import com.fss.core.fssCalculation.modal.input.*;
import com.fss.core.fssCalculation.persistance.UserRepository;
import com.fss.core.fssCalculation.securityconfig.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/window")
public class WindowController {

    @Autowired
    DefaultInput defaultInput;

    @Autowired
    PopulateInputHistory populateInputHistory;

    @Autowired
    FlowContext flowContext;

    @Autowired
    ControllerHelper controllerHelper;

    @Autowired
    private UserRepository userRepository;



    // Show form
    @GetMapping({"/ui", ""})
    public String showUi(@RequestParam(required = false) String activeMenu, Model model, HttpSession session) {

        if (activeMenu == null && flowContext.getActiveMenu() == null) {
            flowContext.setActiveMenu("sliding"); // default landing form
        } else if (activeMenu != null) {
            flowContext.setActiveMenu(activeMenu);
        }


        List<String> activeForms = new ArrayList<>();

        if ("sliding".equalsIgnoreCase(flowContext.getActiveMenu()) || (activeMenu == null && "casement".equalsIgnoreCase(flowContext.getActiveMenu()))) {
            activeForms.add("show_window_form");
            model.addAttribute("sliding_input", defaultInput.prepareSlidingWindowInput());


        } else if ("casement".equalsIgnoreCase(activeMenu)) {
            activeForms.add("show_window_casement_form");
            model.addAttribute("casement_input", defaultInput.prepareCasementWindowInput());

        }
        flowContext.setActiveForm(activeForms);
        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());
        flowContext.getDownloadFormList().clear(); //clear form
        populateInputHistory.handleInputHistory(session, null, model);
        model.addAttribute("activeMenu", flowContext.getActiveMenu());

        return "glazing-form";
    }

    @PostMapping("/sliding")
    public String sliding(@ModelAttribute("sliding_input") SlidingInput slidingInput, Model model, HttpSession session) {


        String activeMenu = flowContext.getActiveMenu();
        flowContext.setCalculationMethod(slidingInput.getCalculationMethod());
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("sliding_input", slidingInput);
        flowContext.setInputValuesMap(inputMap);

        List<String> activeForms = new ArrayList<>();


        populateInputHistory.handleInputHistory(session, populateInputHistory.populateSlidingWindowHistory(slidingInput), model);
        session.setAttribute("typeOfGlazing", "Sliding window");

        // Keep input values so form can re-render with them
        model.addAttribute("sliding_input", slidingInput);
        model.addAttribute("Ixx", 9.9);
        model.addAttribute("deflection", 9.9);
        model.addAttribute("activeMenu", flowContext.getActiveMenu());
        activeForms.add("show_window_result");
        if ("a+b".equalsIgnoreCase(flowContext.getCalculationMethod())) {
            activeForms.add("isMullionCheckForABRequired");
        } else {

            activeForms.add("isMullionCheckRequired");
        }

        flowContext.setIsAB_InterlockCheckDone(false);//reset flag

        flowContext.setActiveForm(activeForms);


        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());
        return "glazing-form"; // or redirect to a result page
    }


    @GetMapping("/centralProfileCheck")
    public String centralProfileCheck(Model model, HttpSession session) {

        String activeMenu = flowContext.getActiveMenu();
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("centralProfileInput", defaultInput.CentralProfileDefaultInput(model, session));


        if ("a+b".equalsIgnoreCase(flowContext.getCalculationMethod()) && !flowContext.getIsAB_InterlockCheckDone()) {
            model.addAttribute("centralProfileTitle", "A+B INTERLOCK PROFILE");

        } else {
            model.addAttribute("centralProfileTitle", "CENTRAL PROFILE");
        }


        model.addAttribute("show_central_profile_form", true);

        populateInputHistory.handleInputHistory(session, null, model);


        return "glazing-form"; // loads your main page
    }

    @PostMapping("/submitCentralProfile")
    public String submitCentralProfile(@ModelAttribute CentralProfileInput input, Model model, HttpSession session) {

        String activeMenu = flowContext.getActiveMenu();
        model.addAttribute("activeMenu", activeMenu);

        List<String> activeForms = new ArrayList<>();
        activeForms.add("show_central_profile_result");
        boolean isCentralProfileCheckRequired = false;

        Object slidingObject = flowContext.getInputValuesMap().get("sliding_input");

        if (slidingObject != null) {
            SlidingInput slidingInput = (SlidingInput) slidingObject;

            isCentralProfileCheckRequired = slidingInput.getCentralMeetingProfile();
        }
        if ("a+b".equalsIgnoreCase(flowContext.getCalculationMethod()) && !flowContext.getIsAB_InterlockCheckDone()) {
            model.addAttribute("centralProfileTitle", "A+B interlock Profile");

            populateInputHistory.handleInputHistory(session, populateInputHistory.populateCentralProfileHistory(input, "A+B interlock Profile Input"), model);

            if (isCentralProfileCheckRequired) {

                activeForms.add("isRedirectToCentralCheckRequired");
            }
            else
            {
                activeForms.add("isOuterCheckRequired");

            }
            flowContext.setIsAB_InterlockCheckDone(true);


        } else {

            if(flowContext.getIsAB_InterlockCheckDone())
            {
                flowContext.setIsAB_InterlockCheckDone(false);
            }
            model.addAttribute("centralProfileTitle", "CENTRAL PROFILE");

            populateInputHistory.handleInputHistory(session, populateInputHistory.populateCentralProfileHistory(input, "Central Profile Input"), model);
            activeForms.add("isOuterCheckRequired");
            flowContext.setIsAB_InterlockCheckDone(false);

        }


        flowContext.setActiveForm(activeForms);


        model.addAttribute("bendingStressA", true);
        model.addAttribute("shearStressA", true);

        model.addAttribute("bendingStressB", true);
        model.addAttribute("shearStressB", true);


        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());

        DownloadReportElement downloadReportElement = new DownloadReportElement(Constants.CENTRAL_CHECK_EXCEL);
        downloadReportElement.getObjectList().add("");//TODO add output Object
        flowContext.getDownloadFormList().add(downloadReportElement);

        return "glazing-form";
    }

    @GetMapping("/outerProfileCheck")
    public String outerProfileCheck(@RequestParam(required = false) String activeMenu, Model model, HttpSession session) {

        if (activeMenu == null) {
            activeMenu = "sliding"; // default tab
        }

        // keep previous inputs if needed
        model.addAttribute("activeMenu", flowContext.getActiveMenu());
        model.addAttribute("show_outer_profile_form", true);
        model.addAttribute("outerProfileInput", defaultInput.prepareOuterProfileInput());

        populateInputHistory.handleInputHistory(session, null, model);


        return "glazing-form"; // loads your main page
    }

    @PostMapping("/submitOuterProfile")
    public String submitOuter(@ModelAttribute OuterProfileInput outerProfileInput, Model model, HttpSession session) {

        model.addAttribute("legBendingStress", true);
        model.addAttribute("legShearStress", true);

        model.addAttribute("activeMenu", flowContext.getActiveMenu());

        model.addAttribute("show_outer_profile_result", true);

        populateInputHistory.handleInputHistory(session, populateInputHistory.populateOuterProfileHistory(outerProfileInput), model);

        DownloadReportElement downloadReportElement = new DownloadReportElement(Constants.OUTER_LEG_CHECK_EXCEL);
        downloadReportElement.getObjectList().add("");//TODO add output Object
        flowContext.getDownloadFormList().add(downloadReportElement);


        return "glazing-form";
    }

    @PostMapping("/casement")
    public String casement(@ModelAttribute("casement_input") CasementInput casementInput, Model model, HttpSession session) {


        String activeMenu = flowContext.getActiveMenu();
        flowContext.setCalculationMethod(casementInput.getCalculationMethod());
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("casement_input", casementInput);
        flowContext.setInputValuesMap(inputMap);

        populateInputHistory.handleInputHistory(session, populateInputHistory.populateCasementInputHistory(casementInput), model);

        List<String> activeForms = new ArrayList<>();

        if (activeMenu.equalsIgnoreCase("casement")) {

            // Keep input values so form can re-render with them
            model.addAttribute("casement_input", casementInput);
            model.addAttribute("Ixx", 9.9);
            model.addAttribute("deflection", 9.9);
            model.addAttribute("activeMenu", activeMenu);
            activeForms.add("show_window_result");

            activeForms.add("isHorizontalCheckRequired");
            flowContext.setActiveForm(activeForms);

        }
        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());

        flowContext.getDownloadFormList().clear(); //clear form
        return "glazing-form"; // or redirect to a result page
    }

    @GetMapping("/horizontal")
    public String loadHorizontalProfile(@RequestParam(required = false) String activeMenu, Model model, HttpSession session) {

        if (activeMenu == null) {
            activeMenu = "casement"; // default tab
        }

        // keep previous inputs if needed
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("show_Horizontal_form", true);

        MullionInput mullionInput = new MullionInput();
        defaultInput.prepareMullionDefaults(model, session, mullionInput);

        model.addAttribute("mullion_input", mullionInput);
        populateInputHistory.handleInputHistory(session, null, model);


        return "glazing-form"; // loads your main page
    }

    @PostMapping("/submitHorizontalProfile")
    public String submitHorizontalProfile(@ModelAttribute MullionInput mullionInput, Model model, HttpSession session) {

        String activeMenu = flowContext.getActiveMenu();
        if (activeMenu == null) {
            activeMenu = "casement"; // default tab
        }

        // keep previous inputs if needed
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("horizontalBendingStress", true);
        model.addAttribute("horizontalShearStress", true);
        model.addAttribute("show_horizontal_profile_result", true);

        populateInputHistory.handleInputHistory(session, populateInputHistory.populateHorizontalProfileHistory(mullionInput, "Horizontal Profile Input"), model);
        DownloadReportElement downloadReportElement = new DownloadReportElement(Constants.HORIZONTAL_CHECK_EXCEL);
        downloadReportElement.getObjectList().add("");//TODO add output Object
        flowContext.getDownloadFormList().add(downloadReportElement);

        return "glazing-form";
    }


}
