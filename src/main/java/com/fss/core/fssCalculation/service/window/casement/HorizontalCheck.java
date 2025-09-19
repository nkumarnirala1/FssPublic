package com.fss.core.fssCalculation.service.window.casement;

import org.springframework.stereotype.Component;

@Component
public class HorizontalCheck {

    public void CheckHorizontalProfile(
            double maxUnsupportedLength,   // S
            double topPanelHeight,         // H1
            double bottomPanelHeight,      // H2
            double distanceOfSettingBlock, // a
            double majorMomentOfInertia,   // Ixx
            double minorMomentOfInertia,   // Iyy
            double crossSectionalArea,     // A
            double boundingBoxX,           // X
            double boundingBoxY,
            double windPressure,// Y
            double glassThickness
    ) {
        double E = 65600;
        double majorSectionalModulus = majorMomentOfInertia / boundingBoxY; //zxx
        double minorSectionalModulus = minorMomentOfInertia / boundingBoxX;// zyy
        double selfWeight = 27.1 * (crossSectionalArea / 100);
        double radiusOfGyration_rx = majorMomentOfInertia / crossSectionalArea;
        double radiusOfGyration_ry = minorMomentOfInertia / crossSectionalArea;

        double effectiveAreaA1 = 0.0;
        if (maxUnsupportedLength > topPanelHeight) {
            effectiveAreaA1 = maxUnsupportedLength * topPanelHeight - 2 * (0.5 * Math.pow(topPanelHeight, 2)) / 4.0;
        } else {
            effectiveAreaA1 = Math.pow(maxUnsupportedLength, 2) / 4.0;
        }
        double effectiveAreaA2 = 0.0;
        if (maxUnsupportedLength > bottomPanelHeight) {
            effectiveAreaA2 = maxUnsupportedLength * bottomPanelHeight - 2 * (0.5 * Math.pow(bottomPanelHeight, 2)) / 4.0;
        } else {
            effectiveAreaA2 = Math.pow(maxUnsupportedLength, 2) / 4.0;
        }

        double udlDueToWindLoadW1 = (windPressure * effectiveAreaA1) / maxUnsupportedLength;
        double udlDueToWindLoadW2 = (windPressure * effectiveAreaA2) / maxUnsupportedLength;

        double totalUdlDueToWindLoad = udlDueToWindLoadW1 + udlDueToWindLoadW2;

        double dlIntensity = ((glassThickness * 25.0) * 110.0) / 100000.0;

        double udlDueToDeadLoadWd1 = (dlIntensity * effectiveAreaA1) / maxUnsupportedLength;
        double udlDueToDeadLoadWd2 = (dlIntensity * effectiveAreaA2) / maxUnsupportedLength;

        double totalUdlDueToDeadLoad = udlDueToDeadLoadWd1 + udlDueToDeadLoadWd2;

        double bendingMomentDueToWindLoad = Math.pow(totalUdlDueToWindLoad, 2) / 8.0;
        double shearForceDueToWindLoad = totalUdlDueToWindLoad / 2.0;

        double bendingMomentDueToDeadLoad = 0.5 * totalUdlDueToDeadLoad * topPanelHeight * distanceOfSettingBlock;
        double shearForceDueToDeadLoad = 0.5 * totalUdlDueToDeadLoad * topPanelHeight;

        double maxDeflectionDueToWindLoad = (5.0 / 384.0) * totalUdlDueToWindLoad * Math.pow(maxUnsupportedLength, 4) / (E * majorMomentOfInertia); //( (5/384)X(wxS^4/EIxx) )
        double allowableDeflectionDueToWindLoad = Math.min(maxUnsupportedLength / 240.00, 19.0);

        // double maxDeflectionDueToDeadLoad = ;
        double allowableDeflectionDueToDeadLoad = Math.min(maxUnsupportedLength / 300.00, 3.0);

    }

}
