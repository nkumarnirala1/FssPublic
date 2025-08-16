package com.fss.core.fssCalculation.service.elements.transom;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TransomBbyY {
    public double calculateBbyY(double windPressure,
                                double transomTopPanelHeight,
                                double transomBottomPanelHeight,
                                double depthOfSectionTransom,
                                double gridLength,
                                double t2Transom,
                                double thicknessTaTransom_t1) {
        double transomVx = calculateTransomVx(windPressure, transomTopPanelHeight, transomBottomPanelHeight, gridLength);
        double hxTransom = calculateHxTransom(depthOfSectionTransom, t2Transom);

        return calculate(transomVx, thicknessTaTransom_t1, hxTransom);


    }

    private double calculate(double transomVx, double thicknessTaTransom_t1, double hxTransom) {
        return (transomVx * 1000) / (2 * thicknessTaTransom_t1 * hxTransom)/41.0;
    }

    private double calculateHxTransom(double depthOfSectionTransom, double t2Transom) {
        return depthOfSectionTransom - 2 * t2Transom;
    }

    //user input transom top and bottom panel height
    private double calculateTransomVx(
            double windPressure,
            double transomTopPanelHeight,
            double transomBottomPanelHeight,
            double gridLength
    ) {
        // Calculate w1 and w2
        double transomW1 = (windPressure * transomTopPanelHeight) / 2000.0;
        double transomW2 = (transomBottomPanelHeight * windPressure) / 2000.0;

        // Calculate Vxt and Vxb
        double transomVxt = (transomW1 * gridLength) / 2000.0;
        double transomVxb = (transomW2 * gridLength) / 2000.0;

        // Final Transom Vx
        return transomVxt + transomVxb;
    }

}
