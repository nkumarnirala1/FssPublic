package com.fss.core.fssCalculation.service;

public class ZxxCal {


    double calculateZxx(double ixx) {
        double boundingBox_y = calculateBoundingBoxY(ixx);

        return ixx / boundingBox_y;

    }

    private double calculateBoundingBoxY(double ixx) {
        double by = 1;
        while (isSafe(ixx, by)) {
            by++;
        }

        return by-1;
    }

    private boolean isSafe(double ixx, double by) {

        //logic to check safety
        return true;
    }
}
