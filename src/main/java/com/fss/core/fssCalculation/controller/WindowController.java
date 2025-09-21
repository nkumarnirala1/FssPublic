package com.fss.core.fssCalculation.controller;


import com.fss.core.fssCalculation.controller.utility.DefaultInput;
import com.fss.core.fssCalculation.modal.input.CentralProfileInput;
import com.fss.core.fssCalculation.modal.input.MullionInput;
import com.fss.core.fssCalculation.modal.input.OuterProfileInput;
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


        Map<String, Object> inputs = new LinkedHashMap<>();
        inputs.put("Unsupported Length (mm)", slidingInput.getUnsupportedLength());
        inputs.put("Grid Length (mm)", slidingInput.getGridLength());
        inputs.put("Wind Pressure (kN/m²)", slidingInput.getWindPressure());
        inputs.put("Glass Thickness (mm)", slidingInput.getGlassThickness());
        inputs.put("Central Meeting Profile", slidingInput.getCentralMeetingProfile());

        session.setAttribute("unsupportedLength", slidingInput.getUnsupportedLength());
        session.setAttribute("gridLength", slidingInput.getGridLength());
        session.setAttribute("windPressure", slidingInput.getWindPressure());
        session.setAttribute("typeOfGlazing", "Sliding window");
        session.setAttribute("slidingInput", slidingInput);

        // entry wrapper
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("formName", "Combined Input");   // friendly title
        entry.put("inputs", inputs);

        history.add(entry);


        session.setAttribute("window_calculationMethod", slidingInput.getCalculationMethod());

        if (slidingInput.getCalculationMethod().equalsIgnoreCase("a+b")) {
            model.addAttribute("window_calculationMethodIsAB", true);
        } else {
            model.addAttribute("window_calculationMethodIsAB", false);
        }


        // Keep input values so form can re-render with them
        model.addAttribute("sliding_input", slidingInput);
        model.addAttribute("Ixx", 9.9);
        model.addAttribute("deflection", 9.9);
        model.addAttribute("activeMenu", "sliding");
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

        CentralProfileInput centralProfileDefaultInput = new CentralProfileInput();

        MullionInput mullionInput = new MullionInput();
        defaultInput.prepareMullionDefaults(model, session, mullionInput);
        centralProfileDefaultInput.setShutterA(mullionInput);
        centralProfileDefaultInput.setShutterB(mullionInput);


        model.addAttribute("centralProfileInput", centralProfileDefaultInput);


        model.addAttribute("show_central_profile_form", true);
        model.addAttribute("centralProfileTitle", "central Profile Input");

        return "glazing-form"; // loads your main page
    }

    @PostMapping("/submitCentralProfile")
    public String submitCentralProfile(@ModelAttribute CentralProfileInput input, Model model, HttpSession session) {
        // process input.getShutterA(), input.getShutterB()

        model.addAttribute("bendingStressA", true);
        model.addAttribute("shearStressA", true);

        model.addAttribute("bendingStressB", true);
        model.addAttribute("shearStressB", true);

        model.addAttribute("activeMenu", "sliding");
        model.addAttribute("show_central_profile_result", true);

        //session.setAttribute("window_calculationMethod", slidingInput.getCalculationMethod());

        String calculationMethod = session.getAttribute("window_calculationMethod") != null ? session.getAttribute("window_calculationMethod").toString() : null;

        if (calculationMethod != null && calculationMethod.equalsIgnoreCase("a+b")) {

            Object obj = session.getAttribute("redirectedFromMullionAB");

            boolean redirectedFromMullionAB= false;
            if(obj!=null)
            {
                redirectedFromMullionAB = (Boolean) obj;
            }

                model.addAttribute("window_calculationMethodIsAB", redirectedFromMullionAB);
            session.setAttribute("redirectedFromMullionAB", false);


            Boolean isCetralProfileCheckRequired = false;
            if (session.getAttribute("slidingInput") != null) {
                Object slifingObject = session.getAttribute("slidingInput");

                SlidingInput slidingInput = (SlidingInput) slifingObject;

                isCetralProfileCheckRequired = slidingInput.getCentralMeetingProfile();
            }
            model.addAttribute("isCentralProfileCheckRequired", isCetralProfileCheckRequired);

        }
        else
        {
            model.addAttribute("window_calculationMethodIsAB", false);
            model.addAttribute("isCentralProfileCheckRequired", false);


        }


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
    public String mullionProfileCheckAB(@RequestParam(required = false) String activeMenu, Model model, HttpSession session) {

        if (activeMenu == null) {
            activeMenu = "sliding"; // default tab
        }

        // keep previous inputs if needed
        model.addAttribute("activeMenu", activeMenu);

        CentralProfileInput centralProfileDefaultInput = new CentralProfileInput();

        MullionInput mullionInput = new MullionInput();
        defaultInput.prepareMullionDefaults(model, session, mullionInput);
        centralProfileDefaultInput.setShutterA(mullionInput);
        centralProfileDefaultInput.setShutterB(mullionInput);


        model.addAttribute("centralProfileInput", centralProfileDefaultInput);


        model.addAttribute("show_central_profile_form", true);
        model.addAttribute("centralProfileTitle", "A+B interlock Profile Input");
        session.setAttribute("redirectedFromMullionAB", true);

        return "glazing-form"; // loads your main page
    }

}
