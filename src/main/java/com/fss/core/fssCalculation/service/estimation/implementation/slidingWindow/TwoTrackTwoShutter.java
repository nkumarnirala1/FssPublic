package com.fss.core.fssCalculation.service.estimation.implementation.slidingWindow;

import com.fss.core.fssCalculation.service.estimation.Interface.IEstimation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwoTrackTwoShutter implements IEstimation {
    @Value("${material.aluminiumDensity:2710}")
    private double density;

    @Override
    public double profileEstimation(double endShutterCrossSectionalArea, double innerLockACrossSectionalArea, double innerLockBCrossSectionalArea, double outerCrossSectionalArea, double gl, double ul, double rate) {

        double unsupportedLength = ul / 1000.0;

        double gridLength = gl / 1000.0;
        double horizontalEndShutter = 4;
        double verticalEndShutter = 4;

        double horizontalOuter = 2;
        double verticalOuter = 2;

        double outerVolume = (horizontalOuter * (2 * gridLength) + verticalOuter * unsupportedLength) * outerCrossSectionalArea/(1000000.0);

        double innerVolume = ((horizontalEndShutter * gridLength) + (verticalEndShutter * unsupportedLength)) * (endShutterCrossSectionalArea/(1000000.0)) + unsupportedLength * ((innerLockACrossSectionalArea + innerLockBCrossSectionalArea)/(1000000.0));

        double totalWeight = density * (innerVolume + outerVolume);

        double totalAmount= totalWeight * rate;

        double totalArea = unsupportedLength * (2*gridLength);
        return totalAmount/totalArea;
    }
}
