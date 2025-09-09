package com.fss.core.fssCalculation.service.elements.mullion.figures;

import com.fss.core.fssCalculation.service.ReportGen.Utility;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Figure3Mullion {

    public double calculateFig3Value(
            double point1Xk1, double point1Yk1,
            double point2Xk1, double point2Yk1,
            double t2, double t1
    ) {
        if (point2Xk1 == point1Xk1) {
            throw new IllegalArgumentException("point1X and point2X cannot be the same (division by zero).");
        }

        double ratio = t2 / t1;
        double slope = (point2Yk1 - point1Yk1) / (point2Xk1 - point1Xk1);
        return Utility.roundTo2Decimal(slope * (ratio - point1Xk1) + point1Yk1);
    }

    public double calculatePoint2yk1(double a, double b, int point2Xk1at) {
        // Round up a/b to the nearest 0.5
        double key = Math.ceil((a / b) / 0.5) * 0.5;

        // Define the lookup table as a map of maps
        Map<Double, Map<Integer, Double>> table = new HashMap<>();

        // Each entry corresponds to a key (rounded a/b), with sub-maps for point2_x_k1at values
        table.put(1.0, Map.of(1, 2.0, 2, 2.0, 3, 2.0, 4, 2.0, 5, 2.0));
        table.put(1.5, Map.of(1, 2.0, 2, 3.1, 3, 3.5, 4, 3.8, 5, 4.0));
        table.put(2.0, Map.of(1, 2.7, 2, 3.3, 3, 3.6, 4, 4.2, 5, 4.4));
        table.put(2.5, Map.of(1, 3.1, 2, 3.6, 3, 3.9, 4, 4.15, 5, 4.15));
        table.put(3.0, Map.of(1, 3.25, 2, 3.75, 3, 4.1, 4, 4.1, 5, 4.1));
        table.put(3.5, Map.of(1, 3.4, 2, 3.8, 3, 4.2, 4, 4.2, 5, 4.2));
        table.put(4.0, Map.of(1, 3.5, 2, 4.0, 3, 4.25, 4, 4.5, 5, 4.5));
        table.put(4.5, Map.of(1, 3.6, 2, 4.2, 3, 4.5, 4, 4.7, 5, 4.7));
        table.put(5.0, Map.of(1, 3.75, 2, 4.25, 3, 4.6, 4, 5.0, 5, 5.0));
        table.put(5.5, Map.of(1, 3.8, 2, 4.35, 3, 4.8, 4, 5.25, 5, 5.25));
        table.put(6.0, Map.of(1, 3.9, 2, 4.5, 3, 5.0, 4, 5.5, 5, 5.5));
        table.put(6.5, Map.of(1, 4.0, 2, 4.6, 3, 5.2, 4, 5.75, 5, 5.75));
        table.put(7.0, Map.of(1, 4.15, 2, 4.75, 3, 5.1, 4, 5.7, 5, 5.9));
        table.put(7.5, Map.of(1, 4.25, 2, 4.8, 3, 5.25, 4, 5.7, 5, 6.2));
        table.put(8.0, Map.of(1, 4.3, 2, 5.0, 3, 5.5, 4, 6.0, 5, 6.3));
        table.put(8.5, Map.of(1, 4.45, 2, 5.05, 3, 5.55, 4, 6.1, 5, 6.6));
        table.put(9.0, Map.of(1, 4.5, 2, 5.15, 3, 5.65, 4, 6.1, 5, 6.7));
        table.put(9.5, Map.of(1, 4.6, 2, 5.25, 3, 5.65, 4, 6.0, 5, 6.5));
        table.put(10.0, Map.of(1, 4.6, 2, 5.35, 3, 5.75, 4, 6.2, 5, 6.8));

        // Get the row from the table
        Map<Integer, Double> innerMap = table.getOrDefault(key, Map.of());

        // Return the matching y_k1at or fallback default
        return innerMap.getOrDefault(point2Xk1at, 6.8);
    }

    public double calculatePoint1yk1(double a, double b, int point1Xk1at) {
        // Round up a/b to the nearest 0.5
        double key = Math.ceil((a / b) / 0.5) * 0.5;

        // Define lookup table
        Map<Double, Map<Integer, Double>> table = new HashMap<>();

        table.put(1.0, Map.of(1, 2.0, 2, 2.0, 3, 2.0, 4, 2.0, 5, 2.0));
        table.put(1.5, Map.of(1, 2.0, 2, 3.1, 3, 3.5, 4, 3.8, 5, 4.0));
        table.put(2.0, Map.of(1, 2.7, 2, 3.3, 3, 3.6, 4, 4.2, 5, 4.4));
        table.put(2.5, Map.of(1, 3.1, 2, 3.6, 3, 3.9, 4, 4.15, 5, 4.15));
        table.put(3.0, Map.of(1, 3.25, 2, 3.75, 3, 4.1, 4, 4.1, 5, 4.1));
        table.put(3.5, Map.of(1, 3.4, 2, 3.8, 3, 4.2, 4, 4.2, 5, 4.2));
        table.put(4.0, Map.of(1, 3.5, 2, 4.0, 3, 4.25, 4, 4.5, 5, 4.5));
        table.put(4.5, Map.of(1, 3.6, 2, 4.2, 3, 4.5, 4, 4.7, 5, 4.7));
        table.put(5.0, Map.of(1, 3.75, 2, 4.25, 3, 4.6, 4, 5.0, 5, 5.0));
        table.put(5.5, Map.of(1, 3.8, 2, 4.35, 3, 4.8, 4, 5.25, 5, 5.25));
        table.put(6.0, Map.of(1, 3.9, 2, 4.5, 3, 5.0, 4, 5.5, 5, 5.5));
        table.put(6.5, Map.of(1, 4.0, 2, 4.6, 3, 5.2, 4, 5.75, 5, 5.75));
        table.put(7.0, Map.of(1, 4.15, 2, 4.75, 3, 5.1, 4, 5.7, 5, 5.9));
        table.put(7.5, Map.of(1, 4.25, 2, 4.8, 3, 5.25, 4, 5.7, 5, 6.2));
        table.put(8.0, Map.of(1, 4.3, 2, 5.0, 3, 5.5, 4, 6.0, 5, 6.3));
        table.put(8.5, Map.of(1, 4.45, 2, 5.05, 3, 5.55, 4, 6.1, 5, 6.6));
        table.put(9.0, Map.of(1, 4.5, 2, 5.15, 3, 5.65, 4, 6.1, 5, 6.7));
        table.put(9.5, Map.of(1, 4.6, 2, 5.25, 3, 5.65, 4, 6.0, 5, 6.5));
        table.put(10.0, Map.of(1, 4.6, 2, 5.35, 3, 5.75, 4, 6.2, 5, 6.8));

        // Lookup based on key and point1Xk1at, with fallback to 6.8
        Map<Integer, Double> innerMap = table.getOrDefault(key, Map.of());
        return innerMap.getOrDefault(point1Xk1at, 6.8);
    }

    //t1, t2 width depth -- user input both
    public double calculatePoint2xk1(double t2, double t1) {
        if (t1 == 0) {
            throw new IllegalArgumentException("t1 cannot be zero to avoid division by zero.");
        }
        return Math.ceil(t2 / t1);
    }

    public double calculatePoint1xk1(double t2, double t1) {
        if (t1 == 0) {
            throw new IllegalArgumentException("t1 cannot be zero to avoid division by zero.");
        }
        return Math.floor(t2 / t1);
    }


}
