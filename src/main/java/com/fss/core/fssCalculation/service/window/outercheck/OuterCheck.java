package com.fss.core.fssCalculation.service.window.outercheck;

import com.fss.core.fssCalculation.service.elements.CalculatedElements;

public class OuterCheck {


    public double checkOuterProfile(double minThickness, double gridlength, double panelHeight, double eccentricity, double unsupportedLength, double windPressure) {
        double perimeter = 2 * (gridlength + panelHeight);

        double windloadOnTrackleg = windPressure * gridlength * panelHeight;

        double windLoadPerMeter = windloadOnTrackleg * perimeter;

        double bendingMomemt = windLoadPerMeter * eccentricity;


        double sectionModulus = gridlength * Math.pow(minThickness, 2) / 6.0;

        double bendingStress = bendingMomemt / sectionModulus;

        double udlDueToWindLoad = CalculatedElements.calculateUDLDueToWindLoad(gridlength, unsupportedLength, windPressure);

        double shearArea = gridlength * minThickness;

        double shearStress = (windLoadPerMeter * 1000.0) / shearArea;

            // if bending Stress <96 --> safe
            // shear stress <51 ---> safe


        return 0.0;
    }
}
