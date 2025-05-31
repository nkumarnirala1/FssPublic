package com.fss.core.fssCalculation.constants;

public enum GlazingType {

    SLIDING_WINDOW("1"),

    IN_TO_IN_SEMI_UNITIZED("3"),

    SINGLE_BRACKET_SEMI_UNITIZED("4"),

    DOUBLE_BRACKET_SEMI_UNITIZED("5");


    private final String code;

    GlazingType(String code) {
        this.code = code;
    }

    public static String findCode(String typeOfGlazing) {


            if (typeOfGlazing.equalsIgnoreCase("Sliding window")) {
                return "1";
            } else if (typeOfGlazing.equalsIgnoreCase("IN TO IN SEMI UNITIZED")) {
                return "3";
            } else if (typeOfGlazing.equalsIgnoreCase("SINGLE BRACKET SEMI UNITIZED")) {
                return "4";
            } else if (typeOfGlazing.equalsIgnoreCase("DOUBLE BRACKET SEMI UNITIZED")) {
                return "5";
            }

            return "";

        }


    public static GlazingType fromCode(String code) {
        for (GlazingType type : GlazingType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid glazing type code: " + code);

    }
}
