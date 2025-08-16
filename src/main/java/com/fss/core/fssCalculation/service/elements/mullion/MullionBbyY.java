package com.fss.core.fssCalculation.service.elements.mullion;

import org.springframework.stereotype.Component;

@Component
public class MullionBbyY {

    // thincknessTa= t1 of t1/t2 (user input) thickness of depth
    public double calculateBbyY(double shearForceMullion, double hx, double thicknessTa) {

        double Y= 41.0;

        if (hx == 0 || thicknessTa == 0) {
            throw new IllegalArgumentException("Depth of section or thickness cannot be zero.");
        }

        return ((shearForceMullion * 1000) / (2 * hx * thicknessTa))/Y;
    }

    //user input depthOfSectionMullion -
    public double calculateHx(double depthOfSectionMullion, double t2) {
        return depthOfSectionMullion - 2 * t2;
    }

    public double calculateShearForceMullion(String glazingType, double udlWindLoad, double unsupportedLength, double bendingMoment) {
        double unsupportedLengthInMeters = unsupportedLength / 1000.0;

        switch (glazingType) {
            case "1":
            case "3":
                return (udlWindLoad * unsupportedLengthInMeters) / 2;

            case "4":
                return (udlWindLoad * unsupportedLengthInMeters * 11 * 0.5) / 10;

            case "5":
                return (udlWindLoad * unsupportedLengthInMeters) / 2
                        + (bendingMoment / unsupportedLengthInMeters);

            default:
                throw new IllegalArgumentException("Unsupported glazing type: " + glazingType);
        }
    }

}
