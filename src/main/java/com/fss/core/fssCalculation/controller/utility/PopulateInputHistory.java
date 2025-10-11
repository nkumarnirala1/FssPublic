package com.fss.core.fssCalculation.controller.utility;

import com.fss.core.fssCalculation.modal.input.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PopulateInputHistory {

    public void handleInputHistory(HttpSession session, Map<String, Object> input, Model model) {

        List<Map<String, Object>> inputHistory =
                (List<Map<String, Object>>) session.getAttribute("inputHistory");

        if (inputHistory == null) {
            inputHistory = new ArrayList<>();
        } else {
            // create a copy to avoid referencing old session object if it wasn’t cleared properly
            inputHistory = new ArrayList<>(inputHistory);
        }

// Insert latest entry at top
        if (input != null) {
            inputHistory.add(0, input);
        }

// Save back cleanly
        session.setAttribute("inputHistory", inputHistory);

        List<Map<String, Object>> sessionHistory =
                (List<Map<String, Object>>) session.getAttribute("inputHistory");
        if (sessionHistory != null) {
            model.addAttribute("inputHistory", sessionHistory);
        } else {
            model.addAttribute("inputHistory", new ArrayList<>());
        }

    }

    public Map<String, Object> populateSlidingWindowHistory(SlidingInput slidingInput) {
        Map<String, Object> inputs = new LinkedHashMap<>();
        inputs.put("Unsupported Length (mm)", slidingInput.getUnsupportedLength());
        inputs.put("Grid Length (mm)", slidingInput.getGridLength());
        inputs.put("Wind Pressure (kN/m²)", slidingInput.getWindPressure());
        inputs.put("Glass Thickness (mm)", slidingInput.getGlassThickness());
        inputs.put("Central Meeting Profile", slidingInput.getCentralMeetingProfile());
        inputs.put("Calculation Method", slidingInput.getCalculationMethod());
        inputs.put("Is Central Profile Check Required", slidingInput.getCentralMeetingProfile());
        inputs.put("Transom to Transom Distance", slidingInput.getTransomToTransomDistance());
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("formName", "Sliding Window Input");
        entry.put("inputs", inputs);
        return entry;
    }

    public Map<String, Object> populateCasementInputHistory(CasementInput casementInput) {
        Map<String, Object> inputs = new LinkedHashMap<>();
        inputs.put("Unsupported Length (mm)", casementInput.getUnsupportedLength());
        inputs.put("Grid Length (mm)", casementInput.getGridLength());
        inputs.put("Wind Pressure (kN/m²)", casementInput.getWindPressure());
        inputs.put("Glass Thickness (mm)", casementInput.getGlassThickness());
        inputs.put("Central Meeting Profile", casementInput.getCentralMeetingProfile());
        inputs.put("Calculation Method", casementInput.getCalculationMethod());
        inputs.put("Is Central Profile Check Required", casementInput.getCentralMeetingProfile());
        inputs.put("Transom to Transom Distance", casementInput.getTransomToTransomDistance());
        inputs.put("Top Panel Height", casementInput.getTopPanelHeight());
        inputs.put("Bottom Panel Height", casementInput.getBottomPanelHeight());
        inputs.put("Setting Block Location", casementInput.getSettingBlockLocation());
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("formName", "Casement Window Input");
        entry.put("inputs", inputs);
        return entry;
    }

    public Map<String, Object> populateSemiUnitizedInputHistory(SemiUnitizedInput semiUnitizedInput) {
        Map<String, Object> inputs = new LinkedHashMap<>();
        inputs.put("Unsupported Length (mm)", semiUnitizedInput.getUnsupportedLength());
        inputs.put("Grid Length (mm)", semiUnitizedInput.getGridLength());
        inputs.put("Wind Pressure (kN/m²)", semiUnitizedInput.getWindPressure());
        inputs.put("Glass Thickness (mm)", semiUnitizedInput.getGlassThickness());
        inputs.put("Central Meeting Profile", semiUnitizedInput.getCentralMeetingProfile());
        inputs.put("Calculation Method", semiUnitizedInput.getCalculationMethod());
        inputs.put("Is Central Profile Check Required", semiUnitizedInput.getCentralMeetingProfile());
        inputs.put("Transom to Transom Distance", semiUnitizedInput.getTransomToTransomDistance());
        inputs.put("Top Panel Height", semiUnitizedInput.getTopPanelHeight());
        inputs.put("Bottom Panel Height", semiUnitizedInput.getBottomPanelHeight());
        inputs.put("Setting Block Location", semiUnitizedInput.getSettingBlockLocation());
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("formName", "Semi-Unitized Input");
        entry.put("inputs", inputs);
        return entry;
    }

    public Map<String, Object> populateCentralProfileHistory(CentralProfileInput centralProfileInput, String formName) {
        Map<String, Object> inputs = new LinkedHashMap<>();

        MullionInput shutterA = centralProfileInput.getShutterA();
        inputs.put("ShutterA UserIxx", shutterA.getUserIxx());
        inputs.put("ShutterA Glass Thickness (mm)", shutterA.getGlassThickness());
        inputs.put("ShutterA Cross sectional Area ", shutterA.getCrossSectionalArea());
        inputs.put("ShutterA Transom to transom Distance", shutterA.getTransomToTransomDistance());
        inputs.put("ShutterA width", shutterA.getB());
        inputs.put("ShutterA Height", shutterA.getA());
        inputs.put("ShutterA T1", shutterA.getT1());
        inputs.put("ShutterA T2", shutterA.getT2());

        MullionInput shutterB = centralProfileInput.getShutterB();
        inputs.put("ShutterB UserIxx", shutterB.getUserIxx());
        inputs.put("ShutterB Glass Thickness (mm)", shutterB.getGlassThickness());
        inputs.put("ShutterB Cross sectional Area ", shutterB.getCrossSectionalArea());
        inputs.put("ShutterB Transom to transom Distance", shutterB.getTransomToTransomDistance());
        inputs.put("ShutterB width", shutterB.getB());
        inputs.put("ShutterB Height", shutterB.getA());
        inputs.put("ShutterB T1", shutterB.getT1());
        inputs.put("ShutterB T2", shutterB.getT2());

        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("formName", formName);
        entry.put("inputs", inputs);
        return entry;
    }

    public Map<String, Object> populateOuterProfileHistory(OuterProfileInput outerProfileInput) {
        Map<String, Object> inputs = new LinkedHashMap<>();
        inputs.put("Eccentricity", outerProfileInput.getEccentricity());
        inputs.put("Leg Thickness", outerProfileInput.getLegThickness());
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("formName", "Outer Profile Input");
        entry.put("inputs", inputs);
        return entry;

    }

    public Map<String, Object> populateHorizontalProfileHistory(MullionInput mullionInput, String formName) {
        Map<String, Object> inputs = new LinkedHashMap<>();

        inputs.put("UserIxx", mullionInput.getUserIxx());
        inputs.put("Glass Thickness (mm)", mullionInput.getGlassThickness());
        inputs.put("Cross sectional Area ", mullionInput.getCrossSectionalArea());
        inputs.put("Transom to transom Distance", mullionInput.getTransomToTransomDistance());
        inputs.put("width", mullionInput.getB());
        inputs.put("Height", mullionInput.getA());
        inputs.put("T1", mullionInput.getT1());
        inputs.put("T2", mullionInput.getT2());

        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("formName", formName);
        entry.put("inputs", inputs);
        return entry;

    }


}
