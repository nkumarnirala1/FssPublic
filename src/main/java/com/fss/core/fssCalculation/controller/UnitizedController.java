package com.fss.core.fssCalculation.controller;

import com.fss.core.fssCalculation.controller.utility.ControllerHelper;
import com.fss.core.fssCalculation.controller.utility.DefaultInput;
import com.fss.core.fssCalculation.controller.utility.FlowContext;
import com.fss.core.fssCalculation.controller.utility.PopulateInputHistory;
import com.fss.core.fssCalculation.modal.input.SemiUnitizedInput;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/unitized")
@SessionAttributes("inputHistory")
public class UnitizedController {

    @Autowired
    FlowContext flowContext;

    @Autowired
    DefaultInput defaultInput;

    @Autowired
    PopulateInputHistory populateInputHistory;

    @Autowired
    ControllerHelper controllerHelper;

    // Show form
    @GetMapping({"/semi-unitized"})
    public String loadsSemiUnitizedForm(@RequestParam(required = false) String activeMenu, Model model, HttpSession session) {
        if (activeMenu == null) {
            activeMenu = "sliding";
            model.addAttribute("sliding_input", defaultInput.prepareSlidingWindowInput());

        }

        flowContext.setActiveMenu(activeMenu);
        flowContext.setActiveForm(new ArrayList<>(List.of("show_semiUnitized_form")));
        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());
        model.addAttribute("activeMenu", activeMenu);

        model.addAttribute("semiUnitizedInput", defaultInput.prepareSemiUnitizedInput());
        flowContext.getDownloadFormList().clear(); //clear form

        return "glazing-form";
    }

    @PostMapping("/semi-unitized")
    public String executeSemiUnitizedFlow(@ModelAttribute("semiUnitizedInput") SemiUnitizedInput semiUnitizedInput, @ModelAttribute("inputHistory") List<Map<String, Object>> history, Model model, HttpSession session) {


        String activeMenu = flowContext.getActiveMenu();
        flowContext.setCalculationMethod(semiUnitizedInput.getCalculationMethod());
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("semiUnitized_input", semiUnitizedInput);
        flowContext.setInputValuesMap(inputMap);

        List<String> activeForms = new ArrayList<>();


        history.add(populateInputHistory.populateSemiUnitizedInputHistory(semiUnitizedInput));
        session.setAttribute("typeOfGlazing", semiUnitizedInput.getCalculationMethod());


        // Keep input values so form can re-render with them
        model.addAttribute("semiUnitizedInput", semiUnitizedInput);
        model.addAttribute("Ixx", 9.9);
        model.addAttribute("deflection", 9.9);
        model.addAttribute("activeMenu", activeMenu);
        activeForms.add("show_semiUnitized_result");

        flowContext.setActiveForm(activeForms);

        controllerHelper.addActiveFormsToModel(model, flowContext.getActiveForm());
        return "glazing-form"; // or redirect to a result page
    }


}
