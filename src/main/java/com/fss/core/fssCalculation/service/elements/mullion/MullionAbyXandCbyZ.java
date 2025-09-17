package com.fss.core.fssCalculation.service.elements.mullion;

import com.fss.core.fssCalculation.service.elements.inertia.ZxxCal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MullionAbyXandCbyZ {

    @Autowired
    ZxxCal zxxCal;

    public double calculateAbyXandCbyZ(double axialForce, double crossSectionArea, double fig1Value, double mByZ, double fig2Value) {
        double part1 = (axialForce * 1000) / (crossSectionArea * 100 * fig1Value);
        double part2 = mByZ / fig2Value;
        return part1 + part2;
    }


    public double calculateMbyZ(double bendingMoment, double ixx, double boundingBox_y) {
        double zxx = zxxCal.calculateZxx(ixx, boundingBox_y);
        return ((bendingMoment * 1000.0) / zxx);
    }


}
