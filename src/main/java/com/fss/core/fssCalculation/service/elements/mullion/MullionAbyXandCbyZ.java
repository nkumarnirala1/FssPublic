package com.fss.core.fssCalculation.service.elements.mullion;

import com.fss.core.fssCalculation.service.ZxxCal;
import jakarta.servlet.http.HttpSession;
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

    public double calculateAxialForce(double udlDeadLoad, double selfWeight, double unsupportedLength) {
        double lengthInMeters = unsupportedLength / 1000.0;
        double axialForce = udlDeadLoad * lengthInMeters + (selfWeight * lengthInMeters) / 100.0;
        return axialForce;
    }

    public double calculateUdlLoad(double gridLength, double unsupportedLength, double glassThickness, HttpSession session) {
        double gridLengthMeters = gridLength / 1000.0;
        double unsupportedLengthMeters = unsupportedLength / 1000.0;
        double glassThicknessMeters = glassThickness / 1000.0;

        double area = (gridLengthMeters * unsupportedLengthMeters)
                - 4 * (0.5 * Math.pow(gridLengthMeters, 2) / 4);

        session.setAttribute("effectiveArea", area);

        double udlLoad = (area * glassThicknessMeters * 25) / unsupportedLengthMeters;
        return udlLoad;
    }

    public double calculateMbyZ(double bendingMoment, double ixx, double boundingBox_y, HttpSession session) {
        double zxx = zxxCal.calculateZxx(ixx, boundingBox_y);
        session.setAttribute("zxx_mullion", zxx);
        return ((bendingMoment * 1000.0) / zxx);
    }

    public double calculateSelfWeight(double crossSectionArea) {
        return 27.10 * (crossSectionArea / 100.0);
    }


}
