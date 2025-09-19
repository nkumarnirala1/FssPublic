package com.fss.core.fssCalculation.modal.output;

import lombok.Data;

@Data
public class HorizontalProfileOutput {

    private double majorSectionalModulus;     // zxx
    private double minorSectionalModulus;     // zyy
    private double selfWeight;                // selfWeight
    private double radiusOfGyrationRx;        // rx
    private double radiusOfGyrationRy;        // ry
    private double effectiveAreaA1;           // effectiveAreaA1
    private double effectiveAreaA2;           // effectiveAreaA2
    private double udlDueToWindLoadW1;        // W1
    private double udlDueToWindLoadW2;        // W2
    private double totalUdlDueToWindLoad;     // total wind load
    private double dlIntensity;               // dlIntensity
    private double udlDueToDeadLoadWd1;       // Wd1
    private double udlDueToDeadLoadWd2;       // Wd2
    private double totalUdlDueToDeadLoad;     // total dead load
    private double bendingMomentDueToWindLoad;// BM wind
    private double shearForceDueToWindLoad;   // SF wind
    private double shearForceDueToDeadLoad;   // SF dead load
    private double allowableDeflectionDueToWindLoad; // Allowable deflection (wind)
    private double allowableDeflectionDueToDeadLoad;
    private double bendingMomentDueToDeadLoad;
    private double maxDeflectionDueToWindLoad;
    private double maxDeflectionDueToDeadLoad;
}
