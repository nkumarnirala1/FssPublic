package com.fss.core.fssCalculation.service.elements.transom;

import org.springframework.stereotype.Service;

@Service
public class TransomAbyXandCbyZ {


    public double calculateAbyXandCbyZ(
            double windPressure,
            double transomTopPanelHeight,
            double transomBottomPanelHeight,
            double transomIxx,
            double transomBoundingBoxY,
            double transomCrossSectionArea,
            double transomUnsupportedLength,
            double glassThickness,
            double fig1Value,
            double fig2Value
    ) {
        double transomSelfWeight = calculateTransomSelfweight(transomCrossSectionArea);
        double transomFacadeGlassWeight = calculateTransomFacadeGlassWeight(transomUnsupportedLength, transomTopPanelHeight, transomBottomPanelHeight, glassThickness);
        double transomP = calculateTransomP(transomFacadeGlassWeight, transomSelfWeight);
        double transomW1 = calculateTransomW1(windPressure, transomTopPanelHeight);
        double transomW2 = calculateTransomW2(transomBottomPanelHeight, windPressure);
        double transomMx = calculateTransomMx(transomW1, transomW2, transomUnsupportedLength);
        double transomZxx = calculateTransomZxx(transomIxx, transomBoundingBoxY);
        double mByZ = calculateMbyZ(transomMx, transomZxx);

        return calculate(transomP, fig1Value, mByZ, fig2Value);


    }

    private double calculate(double transomP, double fig1Value, double mByZ, double fig2Value) {
        double part1 = transomP / fig1Value;
        double part2 = mByZ / fig2Value;
        return part1 + part2;
    }

    public double calculateTransomP(double transomFacadeGlassWeight, double transomSelfWeight) {
        return (transomFacadeGlassWeight + transomSelfWeight/100.0)/2.0;
    }

    public static double calculateTransomFacadeGlassWeight(double transomUnsupportedLength,
                                                           double transomTopPanelHeight,
                                                           double transomBottomPanelHeight,
                                                           double glassThickness) {
        double maxPanelHeight = Math.max(transomTopPanelHeight, transomBottomPanelHeight);
        return transomUnsupportedLength * maxPanelHeight * 25 * glassThickness * 1e-9;
    }

    public double calculateMbyZ(double transomMx, double transomZxx) {
        if (transomZxx == 0) {
            throw new IllegalArgumentException("Transom_Zxx cannot be zero to avoid division by zero.");
        }
        return (transomMx * 1000) / transomZxx;
    }

    // both are user input
    public double calculateTransomZxx(double transomIxx, double transomBoundingBoxY) {
        if (transomBoundingBoxY == 0) {
            throw new IllegalArgumentException("Transom_BoundingBox_y cannot be zero to avoid division by zero.");
        }
        return transomIxx / transomBoundingBoxY;
    }

    public double calculateTransomMx(double transomW1, double transomW2, double unsupportedLength) {
        double lengthInMeters = unsupportedLength / 1000.0;

        double transomMxt = (transomW1 * Math.pow(lengthInMeters, 2)) / 8.0;
        double transomMxb = (transomW2 * Math.pow(lengthInMeters, 2)) / 8.0;

        return transomMxt + transomMxb;
    }

    // wind pressure already taking input from user, panel hieight user input
    public double calculateTransomW1(double windPressure, double transomTopPanelHeight) {
        return (windPressure * transomTopPanelHeight) / 2000.0;
    }

    // bottom panel user input
    public double calculateTransomW2(double transomBottomPanelHeight, double windPressure) {
        return (transomBottomPanelHeight * windPressure) / 2000.0;
    }

    public double calculateTransomSelfweight(double transomCrossSectionArea) {
        return 27.10 * transomCrossSectionArea / 100.0;
    }


}
