package com.fss.core.fssCalculation.controller;

import com.fss.core.fssCalculation.modal.SlidingInput;
import org.springframework.stereotype.Component;

@Component
public class DefaultInput {

    public SlidingInput prepareSlidingWindowInput()
    {
        SlidingInput slidingInput = new SlidingInput();

        slidingInput.setUnsupportedLength(3000.0);
        slidingInput.setGridLength(1200.0);
        slidingInput.setWindPressure(2.35);
        slidingInput.setGlassThickness(12.00);
        slidingInput.setCentralMeetingProfile(false);
        slidingInput.setTransomToTransomDistance(3000.0);


        return slidingInput;
    }
}
