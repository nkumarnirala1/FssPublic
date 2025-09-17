package com.fss.core.fssCalculation.modal.output;


import lombok.Data;

@Data
public class OuterLegOutput {

    private double perimeter;

    private double windLoadOnTrackLeg;

    private double windLoadPerMeter;

    private double bendingMoment;


    private double sectionModulus;

    private double bendingStress;

    private double udlDueToWindLoad;

    private double shearArea;

    private double shearStress;

    private boolean isBendingSafe;

    private boolean isShearSafe;
}
