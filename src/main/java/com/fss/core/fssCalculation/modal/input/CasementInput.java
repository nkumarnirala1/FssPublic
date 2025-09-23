package com.fss.core.fssCalculation.modal.input;

import lombok.Data;

@Data
public class CasementInput {

    private String calculationMethod;
    private Double unsupportedLength;
    private Double gridLength;
    private Double windPressure;
    private Double transomToTransomDistance;
    private Double glassThickness;
    private Boolean centralMeetingProfile;
    private Double topPanelHeight;
    private Double bottomPanelHeight;
    private Double settingBlockLocation;
}
