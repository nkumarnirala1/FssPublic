package com.fss.core.fssCalculation.modal;

import lombok.Data;

@Data
public class GlazingInput {
    private String typeOfGlazing;
    private double unsupportedLength;
    private double gridLength;
    private double windPressure;
    private double stackBracket;

    @Override
    public String toString() {
        return "GlazingInput{" +
                "typeOfGlazing='" + typeOfGlazing + '\'' +
                ", unsupportedLength=" + unsupportedLength +
                ", gridLength=" + gridLength +
                ", windPressure=" + windPressure +
                ", stackBracket=" + stackBracket +
                '}';
    }
}

