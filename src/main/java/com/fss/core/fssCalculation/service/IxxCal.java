package com.fss.core.fssCalculation.service;

import com.fss.core.fssCalculation.constants.GlazingType;
import com.fss.core.fssCalculation.service.elements.CalculatedElements;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class IxxCal {

    @Value("${material.elasticity.E1:65600}")
    private double E;

    @Autowired
    private BendingMomentCal bendingMomentCal;

    public GlazingType glazingType;

    public double calculateRequiredIxx(String typeOfGlazing,
                                              double unsupportedLength,
                                              double gridLength, double windPressure, double stackBracket) {

        double allowableDeflection = CalculatedElements.calculateAllowableDeflection(unsupportedLength);
        double udlWindLoad = CalculatedElements.calculateUDLDueToWindLoad(gridLength, unsupportedLength, windPressure);
        double effectiveLength = CalculatedElements.calculateEffectiveLength(gridLength, unsupportedLength);

        double bendingMoment = bendingMomentCal.calculateBendingMoment(typeOfGlazing, unsupportedLength, gridLength, windPressure, stackBracket);


        double L = unsupportedLength / 1000.0; // Convert mm to meters
        double result = 0;
        glazingType = GlazingType.fromCode(GlazingType.findCode(typeOfGlazing));
        switch (glazingType) {
            case SLIDING_WINDOW:
                // 10^8*([UDL due to wind load]*([Unsupported length]/1000)^4*(5-4*[effective_lenght]^2)^2)/
                // (1920*(1-[effective_lenght])*[E]*[allowable def])
                double factor = Math.pow((5 - 4 * Math.pow(effectiveLength, 2)), 2);

                double numerator1 = udlWindLoad * Math.pow(L, 4) * factor;
                double denominator1 = 1920.0 * (1 - effectiveLength) * E * allowableDeflection;
                result = (numerator1 / denominator1) * 1e8;
                break;

            case IN_TO_IN_SEMI_UNITIZED:
                double numerator2 = 5 * udlWindLoad * Math.pow(L, 4);
                double denominator2 = 384 * E * allowableDeflection;
                result = 1e8 * (numerator2 / denominator2);
                break;


            case SINGLE_BRACKET_SEMI_UNITIZED:

                double term1 = ((udlWindLoad * L) / 2 + (bendingMoment / L)) * (Math.pow(L, 3) / 16);
                double term2 = bendingMoment * Math.pow(L, 2) / 8;
                double term3 = 7 * udlWindLoad * Math.pow(L, 4) / 384;

                double numerator3 = term1 - term2 - term3;
                double denominator3 = E * allowableDeflection;

                result = (numerator3 / denominator3) * 1e8;
                break;

            case DOUBLE_BRACKET_SEMI_UNITIZED:
                double t1 = ((udlWindLoad * L) / 2 + (bendingMoment / L)) * (Math.pow(L, 3) / 16);
                double t2 = bendingMoment * Math.pow(L, 2) / 8;
                double t3 = 7 * udlWindLoad * Math.pow(L, 4) / 384;

                double numerator = t1 - t2 - t3;
                double denominator = E * allowableDeflection;
                result = (numerator / denominator) * 1e8;
                break;

            default:
                throw new IllegalArgumentException("Unsupported type of glazing: " + typeOfGlazing);
        }

        double scaled = result;

        // Round to 1 decimal using BigDecimal
        BigDecimal rounded = new BigDecimal(scaled ).setScale(1, RoundingMode.HALF_UP);
        return rounded.doubleValue();
    }


}
