package com.fss.core.fssCalculation.service.window.centermeetingprofile;

import com.fss.core.fssCalculation.constants.GlazingType;
import com.fss.core.fssCalculation.service.ReportGen.Utility;
import com.fss.core.fssCalculation.service.elements.CalculatedElements;
import com.fss.core.fssCalculation.service.elements.bendingMoment.BendingMomentCal;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class centralMeetingProfile {


    @Autowired
    BendingMomentCal bendingMomentCal;

    public double centralMeetingProfileCheck(
            String glazingType,
            double ixxA,
            double iyyA,
            double boundingBoxXofA,
            double boundingBoxYofA,
            double crossSectionalAreaA,
            double ixxB,
            double iyyB,
            double boundingBoxXofB,
            double boundingBoxYofB,
            double crossSectionalAreaB,
            double gridLength,
            double unsupportedLength,
            double windPressure,
            double glassThickness,
            double stackBracket // not needed if it's sliding window
    ) {

        //A
        double zxxA = calculateSectionModulus(ixxA, boundingBoxYofA);
        double zyyA = calculateSectionModulus(iyyA, boundingBoxXofA);
        double selfWeightofA = calculateSelfWeight(crossSectionalAreaA);
        double radiusOfGyrationXofA = calculateRadiusOfGyration(ixxA, crossSectionalAreaA);
        double radiusOfGyrationYofA = calculateRadiusOfGyration(iyyA, crossSectionalAreaA);

        //B
        double zxxB = calculateSectionModulus(ixxB, boundingBoxYofB);
        double zyyB = calculateSectionModulus(iyyB, boundingBoxXofB);
        double selfWeightofB = calculateSelfWeight(crossSectionalAreaB);
        double radiusOfGyrationXofB = calculateRadiusOfGyration(ixxB, crossSectionalAreaB);
        double radiusOfGyrationYofB = calculateRadiusOfGyration(iyyB, crossSectionalAreaB);

        double majorIeq = ixxA + ixxB;
        double majorZeq = zxxA + zxxB;
        double minorIeq = iyyA + iyyB;
        double minorZeq = zyyA + zyyB;
        double totalCrossSectionalArea = crossSectionalAreaA + crossSectionalAreaB;

        double effectiveArea = CalculatedElements.calculateEffectiveArea(gridLength, unsupportedLength);

        double udlDueToWindLoad = CalculatedElements.calculateUDLDueToWindLoad(gridLength, unsupportedLength, windPressure);

        double udlDueToDeadLoad = CalculatedElements.calculateUdlDueToDeadLoad(gridLength, unsupportedLength, glassThickness);

        double maxBendingMoment = bendingMomentCal.calculateBendingMoment("Sliding window", unsupportedLength, gridLength, windPressure, stackBracket);

        String typeOfGlazingValue = GlazingType.findCode(glazingType);

        double maxShearForce = CalculatedElements.calculateShearForce(typeOfGlazingValue, udlDueToWindLoad, unsupportedLength, maxBendingMoment);

        double selfWeight = CalculatedElements.calculateSelfWeight(totalCrossSectionalArea);

        double maxAxialForce = CalculatedElements.calculateAxialForce(udlDueToDeadLoad, selfWeight, unsupportedLength);

        HashMap<String, Double> distributionFactorShutterA = calculateDistributionFactor(ixxA, majorIeq, crossSectionalAreaA, totalCrossSectionalArea);
        HashMap<String, Double> distributionFactorShutterB = calculateDistributionFactor(ixxB, majorIeq, crossSectionalAreaB, totalCrossSectionalArea);


        return 0.0;
    }

    HashMap<String, Double> calculateDistributionFactor(double singleIntertia, double equivalentIntertia, double singleShutterArea, double totalArea) {
        HashMap<String, Double> distributionFactorMap = new HashMap<>();

        distributionFactorMap.put("bendingMoment", Utility.roundTo2Decimal(singleIntertia / equivalentIntertia));
        distributionFactorMap.put("shearForce", Utility.roundTo2Decimal(singleShutterArea / totalArea));
        distributionFactorMap.put("axialForce", Utility.roundTo2Decimal(singleShutterArea / totalArea));

        return distributionFactorMap;

    }


    double calculateSectionModulus(double intertia, double boundingBox)//zxx
    {
        return Utility.roundTo2Decimal(intertia / boundingBox);
    }

    double calculateSelfWeight(double crossSectionalArea) {
        return Utility.roundTo2Decimal((27.10 * crossSectionalArea) / 100.0);
    }

    double calculateRadiusOfGyration(double intertia, double crossSectionalArea) {
        return Utility.roundTo2Decimal(intertia / crossSectionalArea);
    }
}
