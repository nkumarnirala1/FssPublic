package com.fss.core.fssCalculation.service.elements;

import com.fss.core.fssCalculation.service.ReportGen.Utility;
import jakarta.servlet.http.HttpSession;

public class CalculatedElements {


    public static double calculateUDLDueToWindLoad(double gridLength, double unsupportedLength, double windPressure) {
        double gridM = gridLength / 1000.0;            // Convert mm to meters
        double unsupportedM = unsupportedLength / 1000.0;

        double area = (gridM * unsupportedM) - (4 * (0.5 * Math.pow(gridM, 2) / 4));
        double udl = (area * windPressure) / unsupportedM;

        return udl;
    }


    public static double calculateAllowableDeflection(double unsupportedLength) {
        double unsupportedM = unsupportedLength / 1000.0; // Convert to meters

        if (unsupportedM < 4.15) {
            return Math.min(unsupportedLength / 175.0, 19.0);
        } else {
            return Math.min((unsupportedLength / 240.0) + 6.35, 25.0);
        }
    }

    //  #effective_lenght - [Grid length]/(2*[Unsupported length])

    public static double calculateEffectiveLength(double gridLength, double unsupportedLength) {
        return gridLength / (2 * unsupportedLength);
    }

    public static double calculateEffectiveArea(double gridLengthMeters, double unsupportedLengthMeters) {
        double area = (gridLengthMeters * unsupportedLengthMeters)
                - 4 * (0.5 * Math.pow(gridLengthMeters, 2) / 4);

        return Utility.roundTo2Decimal(area);

    }


    public  static double calculateUdlDueToDeadLoad(double gridLength, double unsupportedLength, double glassThickness) {
        double gridLengthMeters = gridLength / 1000.0;
        double unsupportedLengthMeters = unsupportedLength / 1000.0;
        double glassThicknessMeters = glassThickness / 1000.0;

        double area = CalculatedElements.calculateEffectiveArea(gridLengthMeters, unsupportedLengthMeters);

        double udlLoad = (area * glassThicknessMeters * 25) / unsupportedLengthMeters;
        return udlLoad;
    }

    public static double calculateShearForce(String glazingType, double udlDueToWindLoad, double unsupportedLength, double bendingMoment) {
        double unsupportedLengthInMeters = unsupportedLength / 1000.0;

        switch (glazingType) {
            case "1":
            case "3":
                return (udlDueToWindLoad * unsupportedLengthInMeters) / 2;

            case "4":
                return (udlDueToWindLoad * unsupportedLengthInMeters * 11 * 0.5) / 10;

            case "5":
                return (udlDueToWindLoad * unsupportedLengthInMeters) / 2
                        + (bendingMoment / unsupportedLengthInMeters);

            default:
                throw new IllegalArgumentException("Unsupported glazing type: " + glazingType);
        }
    }


    public static  double calculateSelfWeight(double crossSectionArea) {
        return 27.10 * (crossSectionArea / 100.0);
    }


    public static double calculateAxialForce(double udlDeadLoad, double selfWeight, double unsupportedLength) {
        double lengthInMeters = unsupportedLength / 1000.0;
        double axialForce = udlDeadLoad * lengthInMeters + (selfWeight * lengthInMeters) / 100.0;
        return axialForce;
    }

}

