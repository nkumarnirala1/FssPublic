package com.fss.core.fssCalculation.service;

import com.fss.core.fssCalculation.constants.GlazingType;
import com.fss.core.fssCalculation.service.elements.CalculatedElements;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class BendingMomentCal {

    @Value("${material.elasticity.E1:65600}")
    private double E;

    public GlazingType glazingType;

    public double calculateBendingMoment(String typeOfGlazing,
                                                double unsupportedLength,
                                                double gridLength, double windPressure, double stackBracket) {
        double udlWindLoad = CalculatedElements.calculateUDLDueToWindLoad(gridLength, unsupportedLength, windPressure);
        double effectiveLength = CalculatedElements.calculateEffectiveLength(gridLength, unsupportedLength);

        double L = unsupportedLength / 1000.0; // Convert to meters
        double moment = 0.0;
        glazingType = GlazingType.fromCode(GlazingType.findCode(typeOfGlazing));

        switch (glazingType) {
            case SLIDING_WINDOW:
                double numerator = udlWindLoad * Math.pow(L, 2) * (3 - 4 * Math.pow(effectiveLength, 2));
                double denominator = 24 * (1 - effectiveLength);
                moment = numerator / denominator;
                break;

            case IN_TO_IN_SEMI_UNITIZED:
                moment = udlWindLoad * Math.pow(L, 2) / 8;
                break;
            case SINGLE_BRACKET_SEMI_UNITIZED:
                moment = udlWindLoad * Math.pow(L, 2) / 8;
                break;

            case DOUBLE_BRACKET_SEMI_UNITIZED:
                double a = stackBracket / 1000.0;
                double numerator5 = udlWindLoad * (Math.pow(L, 3) + Math.pow(a, 3));
                double denominator5 = 4 * (2 * L + 3 * a);
                moment = numerator5 / denominator5;
                break;
        }

        return moment;
    }
}
