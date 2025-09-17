package com.fss.core.fssCalculation.modal.output;

import lombok.Data;

import java.util.HashMap;

@Data
public class CentralMeetingProfileOutput {


    // A properties
    private double zxxA;
    private double zyyA;
    private double selfWeightOfA;
    private double radiusOfGyrationXOfA;
    private double radiusOfGyrationYOfA;

    // B properties
    private double zxxB;
    private double zyyB;
    private double selfWeightOfB;
    private double radiusOfGyrationXOfB;
    private double radiusOfGyrationYOfB;

    // Combined properties
    private double majorIeq;
    private double majorZeq;
    private double minorIeq;
    private double minorZeq;
    private double totalCrossSectionalArea;

    // Structural loads
    private double effectiveArea;
    private double udlDueToWindLoad;
    private double udlDueToDeadLoad;
    private double maxBendingMoment;
    private double maxShearForce;
    private double selfWeight;
    private double maxAxialForce;

    // Other details
    private String typeOfGlazingValue;

    // Distribution factors
    private HashMap<String, Double> distributionFactorShutterA;
    private HashMap<String, Double> distributionFactorShutterB;
}
