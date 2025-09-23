package com.fss.core.fssCalculation.controller;


import com.fss.core.fssCalculation.controller.utility.ControllerHelper;
import com.fss.core.fssCalculation.controller.utility.DefaultInput;
import com.fss.core.fssCalculation.controller.utility.FlowContext;
import com.fss.core.fssCalculation.controller.utility.PopulateInputHistory;
import com.fss.core.fssCalculation.modal.input.CentralProfileInput;
import com.fss.core.fssCalculation.modal.input.MullionInput;
import com.fss.core.fssCalculation.modal.input.OuterProfileInput;
import com.fss.core.fssCalculation.modal.input.SlidingInput;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/window")
@SessionAttributes("inputHistory")
public class WindowController {

    @Autowired
    DefaultInput defaultInput;

    @Autowired
    PopulateInputHistory populateInputHistory;

    @Autowired
    FlowContext flowContext;

    @Autowired
    ControllerHelper controllerHelper;

    @ModelAttribute("inputHistory")
    public List<Map<String, Object>> inputHistory() {
        return new ArrayList<>();
    }

    // Show form
    @GetMapping("/ui")
    public String showUi(@RequestParam(required = false) String activeMenu, Model model, HttpSession session) {
        if (activeMenu == null) {
            activeMenu = "sliding"; // default landing form
        }

        flowContext.setActiveMenu(activeMenu);
        flowContext.setActiveForm(new ArrayList<>(List.of("show_window_form")));
        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("sliding_input", defaultInput.prepareSlidingWindowInput());

        return "glazing-form";
    }

    @PostMapping("/calculate")
    public String calculate(@ModelAttribute("sliding_input") SlidingInput slidingInput, @ModelAttribute("inputHistory") List<Map<String, Object>> history, Model model, HttpSession session) {


        String activeMenu = flowContext.getActiveMenu();
        flowContext.setCalculationMethod(slidingInput.getCalculationMethod());
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("sliding_input", slidingInput);
        flowContext.setInputValuesMap(inputMap);

        List<String> activeForms = new ArrayList<>();

        if (activeMenu.equalsIgnoreCase("sliding")) {

            history.add(populateInputHistory.populateSlidingWindowHistory(slidingInput));
            session.setAttribute("typeOfGlazing", "Sliding window");


            // Keep input values so form can re-render with them
            model.addAttribute("sliding_input", slidingInput);
            model.addAttribute("Ixx", 9.9);
            model.addAttribute("deflection", 9.9);
            model.addAttribute("activeMenu", "sliding");
            activeForms.add("show_window_result");
            if ("a+b".equalsIgnoreCase(flowContext.getCalculationMethod())) {
                activeForms.add("isMullionCheckForABRequired");
            } else {

                activeForms.add("isMullionCheckRequired");
            }

            flowContext.setActiveForm(activeForms);

        }

        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());
        return "glazing-form"; // or redirect to a result page
    }


    @GetMapping("/centralProfileCheck")
    public String centralProfileCheck(Model model, HttpSession session) {

        String activeMenu = flowContext.getActiveMenu();
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("centralProfileInput", defaultInput.CentralProfileDefaultInput(model, session));


        if ("sliding".equalsIgnoreCase(activeMenu)) {


            if ("a+b".equalsIgnoreCase(flowContext.getCalculationMethod())) {
                model.addAttribute("centralProfileTitle", "A+B interlock Profile");

            } else {
                model.addAttribute("centralProfileTitle", "central Profile");
            }
        }

        model.addAttribute("show_central_profile_form", true);

        return "glazing-form"; // loads your main page
    }

    @PostMapping("/submitCentralProfile")
    public String submitCentralProfile(@ModelAttribute CentralProfileInput input, Model model, HttpSession session) {

        String activeMenu = flowContext.getActiveMenu();
        model.addAttribute("activeMenu", activeMenu);

        List<String> activeForms = new ArrayList<>();
        activeForms.add("show_central_profile_result");


        boolean isCentralProfileCheckRequired = false;
        if (activeMenu.equalsIgnoreCase("sliding")) {

            Object slidingObject = flowContext.getInputValuesMap().get("sliding_input");

            if (slidingObject != null) {
                SlidingInput slidingInput = (SlidingInput) slidingObject;

                isCentralProfileCheckRequired = slidingInput.getCentralMeetingProfile();
            }
            if ("a+b".equalsIgnoreCase(flowContext.getCalculationMethod())) {
                model.addAttribute("centralProfileTitle", "A+B interlock Profile");

                if (isCentralProfileCheckRequired && (null == flowContext.getRedirectToCentralProfile() || !flowContext.getRedirectToCentralProfile())) {
                    flowContext.setRedirectToCentralProfile(true);
                } else {
                    flowContext.setRedirectToCentralProfile(false);
                }


            } else {
                model.addAttribute("centralProfileTitle", "central Profile");
            }

            if (null != flowContext.getRedirectToCentralProfile() && flowContext.getRedirectToCentralProfile()) {
                activeForms.add("isRedirectToCentralCheckRequired");
            } else {
                activeForms.add("isOuterCheckRequired");
            }

            flowContext.setActiveForm(activeForms);
        }


        model.addAttribute("bendingStressA", true);
        model.addAttribute("shearStressA", true);

        model.addAttribute("bendingStressB", true);
        model.addAttribute("shearStressB", true);

        model.addAttribute("activeMenu", "sliding");

        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());

        return "glazing-form";
    }

    @GetMapping("/outerProfileCheck")
    public String outerProfileCheck(@RequestParam(required = false) String activeMenu, Model model, HttpSession session) {

        if (activeMenu == null) {
            activeMenu = "sliding"; // default tab
        }

        // keep previous inputs if needed
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("show_outer_profile_form", true);
        model.addAttribute("outerProfileInput", defaultInput.prepareOuterProfileInput());

        return "glazing-form"; // loads your main page
    }

    @PostMapping("/submitOuterProfile")
    public String submitOuter(@ModelAttribute OuterProfileInput outerProfileInput, Model model) {

        model.addAttribute("legBendingStress", true);
        model.addAttribute("legShearStress", true);

        model.addAttribute("activeMenu", "sliding");
        model.addAttribute("show_outer_profile_result", true);


        return "glazing-form";
    }

    @GetMapping("/mullionCheckForAB")
    public String mullionProfileCheckAB(Model model, HttpSession session) {

        String activeMenu = flowContext.getActiveMenu();
        List<String> activeForms = new ArrayList<>();


        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("centralProfileInput", defaultInput.CentralProfileDefaultInput(model, session));
        model.addAttribute("show_central_profile_form", true);
        model.addAttribute("centralProfileTitle", "A+B interlock Profile");


        return "glazing-form"; // loads your main page
    }


}
