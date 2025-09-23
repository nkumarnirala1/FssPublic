package com.fss.core.fssCalculation.controller.utility;

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
}
