package com.fss.core.fssCalculation.modal.output;

import lombok.Data;

import java.util.Map;

@Data
public class TransomOutput {

    private double abyB;

    private double fig1Value;
    private double fig3Value;
    private double fig2Value;

    private double aByXandCbyZ;
    private double bByY;

    private Map<String, Boolean> resultMap;

}
