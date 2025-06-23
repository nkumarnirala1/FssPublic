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

        double outerVolume = (horizontalOuter * (2 * gridLength) + verticalOuter * unsupportedLength) * outerCrossSectionalArea;

        double innerVolume = ((horizontalEndShutter * gridLength) + (verticalEndShutter * unsupportedLength)) * endShutterCrossSectionalArea + unsupportedLength * (innerLockACrossSectionalArea + innerLockBCrossSectionalArea);

        double totalWeight = density * (innerVolume + outerVolume);
        return totalWeight * rate;
    }
}
