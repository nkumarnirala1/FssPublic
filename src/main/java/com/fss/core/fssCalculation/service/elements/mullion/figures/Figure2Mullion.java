package com.fss.core.fssCalculation.service.elements.mullion.figures;

import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class Figure2Mullion {

    public double calculateFig2Value(
            double point1X_lmd, double point1Y_lmd,
            double point2X_lmd, double point2Y_lmd,
            double lambdaAt
    ) {
        double slope = (point2Y_lmd - point1Y_lmd) / (point2X_lmd - point1X_lmd);
        return slope * (lambdaAt - point1X_lmd) + point1Y_lmd;
    }

    public double calculatepoint2Y_lmd(int point2X_lmd) {
        Map<Integer, Double> xToYMap = Map.ofEntries(
                Map.entry(0, 96.0), Map.entry(5, 96.0), Map.entry(10, 95.0), Map.entry(15, 91.0),
                Map.entry(20, 88.0), Map.entry(25, 86.0), Map.entry(30, 82.0), Map.entry(35, 79.0),
                Map.entry(40, 77.0), Map.entry(45, 73.0), Map.entry(50, 70.0), Map.entry(55, 67.0),
                Map.entry(60, 64.0), Map.entry(65, 61.0), Map.entry(70, 58.0), Map.entry(75, 55.0),
                Map.entry(80, 52.0), Map.entry(85, 47.0), Map.entry(90, 42.0), Map.entry(95, 37.0),
                Map.entry(100, 34.0), Map.entry(105, 31.0), Map.entry(110, 28.0), Map.entry(115, 26.0),
                Map.entry(120, 24.0), Map.entry(125, 22.0), Map.entry(130, 20.0), Map.entry(135, 18.0),
                Map.entry(140, 16.0), Map.entry(145, 15.5), Map.entry(150, 15.0), Map.entry(155, 14.5),
                Map.entry(160, 13.0), Map.entry(165, 12.0), Map.entry(170, 11.0), Map.entry(175, 10.0),
                Map.entry(180, 10.0), Map.entry(185, 9.0), Map.entry(190, 9.0), Map.entry(195, 9.0),
                Map.entry(200, 8.0), Map.entry(205, 7.0), Map.entry(210, 6.0), Map.entry(215, 5.0)
        );

        return xToYMap.getOrDefault(point2X_lmd, 5.0);
    }

    public double calculatepoint1Y_lmd(int point1X_lmd) {
        Map<Integer, Double> xToYMap = Map.ofEntries(
                Map.entry(0, 96.0), Map.entry(5, 96.0), Map.entry(10, 95.0), Map.entry(15, 91.0),
                Map.entry(20, 88.0), Map.entry(25, 86.0), Map.entry(30, 82.0), Map.entry(35, 79.0),
                Map.entry(40, 77.0), Map.entry(45, 73.0), Map.entry(50, 70.0), Map.entry(55, 67.0),
                Map.entry(60, 64.0), Map.entry(65, 61.0), Map.entry(70, 58.0), Map.entry(75, 55.0),
                Map.entry(80, 52.0), Map.entry(85, 47.0), Map.entry(90, 42.0), Map.entry(95, 37.0),
                Map.entry(100, 34.0), Map.entry(105, 31.0), Map.entry(110, 28.0), Map.entry(115, 26.0),
                Map.entry(120, 24.0), Map.entry(125, 22.0), Map.entry(130, 20.0), Map.entry(135, 18.0),
                Map.entry(140, 16.0), Map.entry(145, 15.5), Map.entry(150, 15.0), Map.entry(155, 14.5),
                Map.entry(160, 13.0), Map.entry(165, 12.0), Map.entry(170, 11.0), Map.entry(175, 10.0),
                Map.entry(180, 10.0), Map.entry(185, 9.0), Map.entry(190, 9.0), Map.entry(195, 9.0),
                Map.entry(200, 8.0), Map.entry(205, 7.0), Map.entry(210, 6.0), Map.entry(215, 5.0)
        );

        return xToYMap.getOrDefault(point1X_lmd, 5.0);
    }


    public double calculatepoint2X_lmd(double lambdaAt) {
        return Math.ceil(lambdaAt / 5.0) * 5;
    }

    public double calculatepoint1X_lmd(double lambdaAt) {
        return Math.floor(lambdaAt / 5.0) * 5;
    }

    public double calculateLambdaAt(double fig3Value, double transomToTransomDistance, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Parameter 'b' cannot be zero.");
        }
        return fig3Value * Math.sqrt(transomToTransomDistance / b);
    }


}
