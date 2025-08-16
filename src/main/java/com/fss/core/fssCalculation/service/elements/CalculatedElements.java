package com.fss.core.fssCalculation.service.elements;

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

    public static Boolean checkIfDeflectionSafeForMullion(double unsupportedLength, double calculatedDeflection) {
        double allowableDeflection;

        if (unsupportedLength <= 4150) {
            // L/175 or 19mm, whichever is lesser
            allowableDeflection = Math.min(unsupportedLength / 175.0, 19.0);
        } else {
            // (L/240 + 6.35) or 25mm, whichever is lesser
            allowableDeflection = Math.min((unsupportedLength / 240.0) + 6.35, 25.0);
        }

        return calculatedDeflection <= allowableDeflection;
    }



}

