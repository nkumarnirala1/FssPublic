package com.fss.core.fssCalculation.controller.utility;

import com.fss.core.fssCalculation.modal.input.CasementInput;
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
        inputs.put("calculation Method", slidingInput.getCalculationMethod());
        inputs.put("isCentralProfileCheckRequired", slidingInput.getCentralMeetingProfile());
        inputs.put("Transom to transom Distance", slidingInput.getTransomToTransomDistance());
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("formName", "Combined Input");   // friendly title
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
        inputs.put("calculation Method", casementInput.getCalculationMethod());
        inputs.put("isCentralProfileCheckRequired", casementInput.getCentralMeetingProfile());
        inputs.put("Transom to transom Distance", casementInput.getTransomToTransomDistance());
        inputs.put("Top panel Height", casementInput.getTopPanelHeight());
        inputs.put("Bottom panel Height", casementInput.getBottomPanelHeight());
        inputs.put("Setting Block Location", casementInput.getSettingBlockLocation());
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("formName", "Casement Window Input");   // friendly title
        entry.put("inputs", inputs);

        return entry;
    }
}
