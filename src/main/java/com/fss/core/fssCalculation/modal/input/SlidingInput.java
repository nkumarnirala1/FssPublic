package com.fss.core.fssCalculation.modal.input;


import lombok.Data;

@Data
public class SlidingInput {
    private String calculationMethod;
    private Double unsupportedLength;
    private Double gridLength;
    private Double windPressure;
    private Double transomToTransomDistance;
    private Double glassThickness;
    private Boolean centralMeetingProfile;


}
