package com.fss.core.fssCalculation.modal.output;

import lombok.Data;

import java.util.Map;


@Data
public class MullionProfileOutput {

    private String typeOfGlazingValue;
    private double unsupportedLength;
    private double gridLength;
    private double windPressure;

    private double boundingBoxy;
    private double selfWeight;
    private double udlDeadLoad;
    private double effectiveArea;
    private double axialForce;
    private double ftMullion;

    private FigureOneOutput fig1Value;
    private double mByz;

    // Figure 3 values
    private double point1Xk1;
    private double point1Yk1;
    private double point2Xk1;
    private double point2Yk1;
    private double fig3Value;

    // Figure 2 values
    private double lambdaAt;
    private double point1xLmd;
    private double point1YLmd;
    private double point2XLmd;
    private double point2YLmd;
    private double fig2Value;

    private double udlWindLoad;
    private double shearForce;
    private double hx;

    private double aByXandCbyZ;
    private double bByY;

    private Map<String, Boolean> resultMap;
}
