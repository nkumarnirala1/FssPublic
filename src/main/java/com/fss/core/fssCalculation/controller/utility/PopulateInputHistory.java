package com.fss.core.fssCalculation.controller.utility;

import com.fss.core.fssCalculation.modal.input.CasementInput;
import com.fss.core.fssCalculation.modal.input.SemiUnitizedInput;
import com.fss.core.fssCalculation.modal.input.SlidingInput;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION,  proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PopulateInputHistory {

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
}
