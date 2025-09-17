package com.fss.core.fssCalculation.service.window.sliding;

import com.fss.core.fssCalculation.modal.output.OuterLegOutput;
import com.fss.core.fssCalculation.service.elements.CalculatedElements;

public class OuterCheck {


    public OuterLegOutput checkOuterProfile(double minThickness, double gridLength, double panelHeight, double eccentricity, double unsupportedLength, double windPressure) {

        OuterLegOutput outerLegOutput = new OuterLegOutput();

        // Perimeter
        double perimeter = 2 * (gridLength + panelHeight);
        outerLegOutput.setPerimeter(perimeter);

        // Wind load on track leg
        double windLoadOnTrackLeg = windPressure * gridLength * panelHeight;
        outerLegOutput.setWindLoadOnTrackLeg(windLoadOnTrackLeg);

        // Wind load per meter
        double windLoadPerMeter = windLoadOnTrackLeg * perimeter;
        outerLegOutput.setWindLoadPerMeter(windLoadPerMeter);

        // Bending moment
        double bendingMoment = windLoadPerMeter * eccentricity;
        outerLegOutput.setBendingMoment(bendingMoment);

        // Section modulus
        double sectionModulus = gridLength * Math.pow(minThickness, 2) / 6.0;
        outerLegOutput.setSectionModulus(sectionModulus);

        // Bending stress
        double bendingStress = bendingMoment / sectionModulus;
        outerLegOutput.setBendingStress(bendingStress);

        // UDL due to wind load
        double udlDueToWindLoad = CalculatedElements.calculateUDLDueToWindLoad(gridLength, unsupportedLength, windPressure);
        outerLegOutput.setUdlDueToWindLoad(udlDueToWindLoad);

        // Shear area
        double shearArea = gridLength * minThickness;
        outerLegOutput.setShearArea(shearArea);

        // Shear stress
        double shearStress = (windLoadPerMeter * 1000.0) / shearArea;
        outerLegOutput.setShearStress(shearStress);

        // You can add safety checks and set boolean results
        outerLegOutput.setBendingSafe(bendingStress < 96);
        outerLegOutput.setShearSafe(shearStress < 51);

        return outerLegOutput;
    }
}
