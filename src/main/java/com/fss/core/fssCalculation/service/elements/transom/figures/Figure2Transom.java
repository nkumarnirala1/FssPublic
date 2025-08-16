package com.fss.core.fssCalculation.service.elements.transom.figures;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Figure2Transom {

    //ransomUnsupportedLength= gridlength
    public double calculateFigure2Value(double fig3Value, double transomUnsupportedLength, double sectionWidthB) {
        double lambda_lmd = calculateLambdaAtTransom(fig3Value, transomUnsupportedLength, sectionWidthB);
        double point2_x_lmd = calculatePoint2x_lmd(lambda_lmd);
        double point1_x_lmd = calculatePoint1x_lmd(lambda_lmd);
        double point2_y_lmd = calculatePoint2y_lmd(point2_x_lmd);
        double point1_y_lmd = calculatePoint1y_lmd(point1_x_lmd);

        return calculate(point2_y_lmd, point1_y_lmd, point2_x_lmd, point1_x_lmd, lambda_lmd);


    }

    public double calculate(
            double point2_y_lmd,
            double point1_y_lmd,
            double point2_x_lmd,
            double point1_x_lmd,
            double lambda_lmd
    ) {
        if (point2_x_lmd == point1_x_lmd) {
            throw new IllegalArgumentException("point2_x_lmd and point1_x_lmd cannot be equal (division by zero).");
        }

        double slope = (point2_y_lmd - point1_y_lmd) / (point2_x_lmd - point1_x_lmd);
        return slope * (lambda_lmd - point1_x_lmd) + point1_y_lmd;
    }

    public double calculatePoint2y_lmd(double point2_x_lmd) {
        Map<Integer, Double> lookup = new HashMap<>();
        lookup.put(0, 96.0);
        lookup.put(5, 96.0);
        lookup.put(10, 95.0);
        lookup.put(15, 91.0);
        lookup.put(20, 88.0);
        lookup.put(25, 86.0);
        lookup.put(30, 82.0);
        lookup.put(35, 79.0);
        lookup.put(40, 77.0);
        lookup.put(45, 73.0);
        lookup.put(50, 70.0);
        lookup.put(55, 67.0);
        lookup.put(60, 64.0);
        lookup.put(65, 61.0);
        lookup.put(70, 58.0);
        lookup.put(75, 55.0);
        lookup.put(80, 52.0);
        lookup.put(85, 47.0);
        lookup.put(90, 42.0);
        lookup.put(95, 37.0);
        lookup.put(100, 34.0);
        lookup.put(105, 31.0);
        lookup.put(110, 28.0);
        lookup.put(115, 26.0);
        lookup.put(120, 24.0);
        lookup.put(125, 22.0);
        lookup.put(130, 20.0);
        lookup.put(135, 18.0);
        lookup.put(140, 16.0);
        lookup.put(145, 15.5);
        lookup.put(150, 15.0);
        lookup.put(155, 14.5);
        lookup.put(160, 13.0);
        lookup.put(165, 12.0);
        lookup.put(170, 11.0);
        lookup.put(175, 10.0);
        lookup.put(180, 10.0);
        lookup.put(185, 9.0);
        lookup.put(190, 9.0);
        lookup.put(195, 9.0);
        lookup.put(200, 8.0);
        lookup.put(205, 7.0);
        lookup.put(210, 6.0);
        lookup.put(215, 5.0);

        return lookup.getOrDefault((int) point2_x_lmd, 5.0);
    }

    public double calculatePoint1y_lmd(double point1_x_lmd) {
        Map<Integer, Double> lookup = new HashMap<>();
        lookup.put(0, 96.0);
        lookup.put(5, 96.0);
        lookup.put(10, 95.0);
        lookup.put(15, 91.0);
        lookup.put(20, 88.0);
        lookup.put(25, 86.0);
        lookup.put(30, 82.0);
        lookup.put(35, 79.0);
        lookup.put(40, 77.0);
        lookup.put(45, 73.0);
        lookup.put(50, 70.0);
        lookup.put(55, 67.0);
        lookup.put(60, 64.0);
        lookup.put(65, 61.0);
        lookup.put(70, 58.0);
        lookup.put(75, 55.0);
        lookup.put(80, 52.0);
        lookup.put(85, 47.0);
        lookup.put(90, 42.0);
        lookup.put(95, 37.0);
        lookup.put(100, 34.0);
        lookup.put(105, 31.0);
        lookup.put(110, 28.0);
        lookup.put(115, 26.0);
        lookup.put(120, 24.0);
        lookup.put(125, 22.0);
        lookup.put(130, 20.0);
        lookup.put(135, 18.0);
        lookup.put(140, 16.0);
        lookup.put(145, 15.5);
        lookup.put(150, 15.0);
        lookup.put(155, 14.5);
        lookup.put(160, 13.0);
        lookup.put(165, 12.0);
        lookup.put(170, 11.0);
        lookup.put(175, 10.0);
        lookup.put(180, 10.0);
        lookup.put(185, 9.0);
        lookup.put(190, 9.0);
        lookup.put(195, 9.0);
        lookup.put(200, 8.0);
        lookup.put(205, 7.0);
        lookup.put(210, 6.0);
        lookup.put(215, 5.0);

        return lookup.getOrDefault((int) point1_x_lmd, 5.0);
    }

    public int calculatePoint2x_lmd(double lambda_lmd) {
        return (int) (Math.ceil(lambda_lmd / 5.0) * 5);
    }

    public int calculatePoint1x_lmd(double lambda_lmd) {
        return (int) (Math.floor(lambda_lmd / 5.0) * 5);
    }

    public double calculateLambdaAtTransom(double fig3Value, double transomUnsupportedLength, double sectionWidthB) {
        if (sectionWidthB == 0) {
            throw new IllegalArgumentException("Section width (b) cannot be zero.");
        }
        return fig3Value * Math.sqrt(transomUnsupportedLength / sectionWidthB);
    }


}
