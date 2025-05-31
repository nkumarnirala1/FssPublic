package com.fss.core.fssCalculation.modal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GlazingInput {
    @NotNull(message = "Unsupported Length is required")
    @Min(value = 1, message = "Unsupported Length must be greater than 0")
    private Double unsupportedLength;

    @NotNull(message = "Grid Length is required")
    @Min(value = 1, message = "Grid Length must be greater than 0")
    private Double gridLength;

    @NotNull(message = "Wind Pressure is required")
    @Min(value = 1, message = "Wind Pressure must be greater than 0")
    private Double windPressure;

    @NotNull(message = "Stack Bracket is required")
    @Min(value = 0, message = "Stack Bracket cannot be negative")
    private Double stackBracket;

    @NotNull(message = "Type of Glazing is required")
    private String typeOfGlazing;

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

