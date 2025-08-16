package com.fss.core.fssCalculation.service.elements.transom.figures;

import org.springframework.stereotype.Service;

@Service
public class Figure3Transom {

    public double calculateFig3Value(double aByB, double t2t, double t1t) {
        double t2byt1 = t2t / t1t;

        double point1X_k1at = calculatePoint1X_k1at(t2byt1);
        double point2X_k1at = calculatePoint2X_k1at(t2byt1);
        double point1Y_k1at = calculatePoint1Y_k1at(aByB, (int) point1X_k1at);
        double point2Y_k1at = calculatePoint2Y_k1at(aByB, (int) point2X_k1at);

        return calculate(point1X_k1at, point2X_k1at, point1Y_k1at, point2Y_k1at, t2byt1);


    }

    private double calculate(
            double point1X_k1at,
            double point2X_k1at,
            double point1Y_k1at,
            double point2Y_k1at,
            double t2byt1 // This is t2/t1 (transom)
    ) {
        if (point2X_k1at == point1X_k1at) {
            throw new IllegalArgumentException("Division by zero: point1_X_k1at and point2_x_k1at cannot be the same.");
        }

        double slope = (point2Y_k1at - point1Y_k1at) / (point2X_k1at - point1X_k1at);
        return slope * (t2byt1 - point1X_k1at) + point1Y_k1at;
    }

    //t2/1 transom , user input
    private int calculatePoint1X_k1at(double t2byt1) {
        return (int) Math.floor(t2byt1 / 0.99999999999);
    }

    private static int calculatePoint2X_k1at(double t2byt1) {
        return (int) Math.ceil(t2byt1 / 0.99999999999);
    }

    //user will feed abyb
    private static double calculatePoint2Y_k1at(double abyB, int point2_x_k1at) {
        double ceilingHalfStep = Math.ceil(abyB / 0.5) * 0.5;

        switch ((int) (ceilingHalfStep * 10)) {
            case 10:
                return lookup1(point2_x_k1at, 2, 2, 2, 2, 2, 2);
            case 15:
                return lookup1(point2_x_k1at, 2, 3.1, 3.5, 3.8, 4, 4);
            case 20:
                return lookup1(point2_x_k1at, 2.7, 3.3, 3.6, 4.2, 4.4, 4.4);
            case 25:
                return lookup1(point2_x_k1at, 3.1, 3.6, 3.9, 4.15, 4.15, 4.15);
            case 30:
                return lookup1(point2_x_k1at, 3.25, 3.75, 4.1, 4.1, 4.1, 4.1);
            case 35:
                return lookup1(point2_x_k1at, 3.4, 3.8, 4.2, 4.2, 4.2, 4.2);
            case 40:
                return lookup1(point2_x_k1at, 3.5, 4, 4.25, 4.5, 4.5, 4.5);
            case 45:
                return lookup1(point2_x_k1at, 3.6, 4.2, 4.5, 4.7, 4.7, 4.7);
            case 50:
                return lookup1(point2_x_k1at, 3.75, 4.25, 4.6, 5, 5, 5);
            case 55:
                return lookup1(point2_x_k1at, 3.8, 4.35, 4.8, 5.25, 5.25, 5.25);
            case 60:
                return lookup1(point2_x_k1at, 3.9, 4.5, 5, 5.5, 5.5, 5.5);
            case 65:
                return lookup1(point2_x_k1at, 4, 4.6, 5.2, 5.75, 5.75, 5.75);
            case 70:
                return lookup1(point2_x_k1at, 4.15, 4.75, 5.1, 5.7, 5.9, 5.9);
            case 75:
                return lookup1(point2_x_k1at, 4.25, 4.8, 5.25, 5.7, 6.2, 6.2);
            case 80:
                return lookup1(point2_x_k1at, 4.3, 5, 5.5, 6, 6.3, 6.3);
            case 85:
                return lookup1(point2_x_k1at, 4.45, 5.05, 5.55, 6.1, 6.6, 6.6);
            case 90:
                return lookup1(point2_x_k1at, 4.5, 5.15, 5.65, 6.1, 6.7, 6.7);
            case 95:
                return lookup1(point2_x_k1at, 4.6, 5.25, 5.65, 6, 6.5, 6.5);
            case 100:
                return lookup1(point2_x_k1at, 4.6, 5.35, 5.75, 6.2, 6.8, 6.8);
            default:
                return 6.8;
        }
    }

    private static double lookup1(int x, double v1, double v2, double v3, double v4, double v5, double def) {
        switch (x) {
            case 1:
                return v1;
            case 2:
                return v2;
            case 3:
                return v3;
            case 4:
                return v4;
            case 5:
                return v5;
            default:
                return def;
        }
    }

    private static double calculatePoint1Y_k1at(double aOverB, int point1_X_k1at) {
        double ceilingHalfStep = Math.ceil(aOverB / 0.5) * 0.5;

        switch ((int) (ceilingHalfStep * 10)) {
            case 10:
                return lookup(point1_X_k1at, 2, 2, 2, 2, 2, 2); // 1.0
            case 15:
                return lookup(point1_X_k1at, 2, 3.1, 3.5, 3.8, 4, 4);
            case 20:
                return lookup(point1_X_k1at, 2.7, 3.3, 3.6, 4.2, 4.4, 4.4);
            case 25:
                return lookup(point1_X_k1at, 3.1, 3.6, 3.9, 4.15, 4.15, 4.15);
            case 30:
                return lookup(point1_X_k1at, 3.25, 3.75, 4.1, 4.1, 4.1, 4.1);
            case 35:
                return lookup(point1_X_k1at, 3.4, 3.8, 4.2, 4.2, 4.2, 4.2);
            case 40:
                return lookup(point1_X_k1at, 3.5, 4, 4.25, 4.5, 4.5, 4.5);
            case 45:
                return lookup(point1_X_k1at, 3.6, 4.2, 4.5, 4.7, 4.7, 4.7);
            case 50:
                return lookup(point1_X_k1at, 3.75, 4.25, 4.6, 5, 5, 5);
            case 55:
                return lookup(point1_X_k1at, 3.8, 4.35, 4.8, 5.25, 5.25, 5.25);
            case 60:
                return lookup(point1_X_k1at, 3.9, 4.5, 5, 5.5, 5.5, 5.5);
            case 65:
                return lookup(point1_X_k1at, 4, 4.6, 5.2, 5.75, 5.75, 5.75);
            case 70:
                return lookup(point1_X_k1at, 4.15, 4.75, 5.1, 5.7, 5.9, 5.9);
            case 75:
                return lookup(point1_X_k1at, 4.25, 4.8, 5.25, 5.7, 6.2, 6.2);
            case 80:
                return lookup(point1_X_k1at, 4.3, 5, 5.5, 6, 6.3, 6.3);
            case 85:
                return lookup(point1_X_k1at, 4.45, 5.05, 5.55, 6.1, 6.6, 6.6);
            case 90:
                return lookup(point1_X_k1at, 4.5, 5.15, 5.65, 6.1, 6.7, 6.7);
            case 95:
                return lookup(point1_X_k1at, 4.6, 5.25, 5.65, 6, 6.5, 6.5);
            case 100:
                return lookup(point1_X_k1at, 4.6, 5.35, 5.75, 6.2, 6.8, 6.8);
            default:
                return 6.8;
        }
    }

    // Helper function: maps x=1..5 to corresponding y-values, with a fallback default
    private static double lookup(int x, double v1, double v2, double v3, double v4, double v5, double def) {
        switch (x) {
            case 1:
                return v1;
            case 2:
                return v2;
            case 3:
                return v3;
            case 4:
                return v4;
            case 5:
                return v5;
            default:
                return def;
        }
    }


}
