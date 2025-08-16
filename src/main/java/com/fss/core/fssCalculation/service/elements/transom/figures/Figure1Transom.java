package com.fss.core.fssCalculation.service.elements.transom.figures;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Figure1Transom {

    public double calculateFig1ValueTransom(double transomIxx, double transomIyy, double crossSectionArea, double gridLength) {

        double transomRadiusOfGyrationX = calculateRadiusOfGyrationX(transomIxx, crossSectionArea);
        double transomRadiusOfGyrationY = calculateRadiusOfGyrationY(transomIyy, crossSectionArea);
        double lxByRx = calculateLxByRxTransom(gridLength, transomRadiusOfGyrationX);
        double lyByRy = calculateLyByRyTransom(gridLength, transomRadiusOfGyrationY);

        double lByR = calculateLbyRTransom(lxByRx, lyByRy);
        double point1X = calculatePoint1X(lByR);
        double point2X = calculatePoint2X(lByR);
        double point1Y = calculatePoint1Y(point1X);
        double point2Y = calculatePoint2Y(point2X);

        return calculate(point1X, point1Y, point2X, point2Y, lByR);


    }

    private double calculate(
            double point1X, double point1Y,
            double point2X, double point2Y,
            double lByR
    ) {
        return ((point2Y - point1Y) / (point2X - point1X)) * (lByR - point1X) + point1Y;
    }

    public double calculatePoint2Y(double point2X) {
        switch ((int) point2X) {
            case 0:
            case 5:
                return 85;
            case 10:
                return 84;
            case 15:
                return 81;
            case 20:
                return 79;
            case 25:
                return 77;
            case 30:
                return 75;
            case 35:
                return 73;
            case 40:
                return 71;
            case 45:
                return 68;
            case 50:
                return 66;
            case 55:
                return 64;
            case 60:
                return 62;
            case 65:
                return 60;
            case 70:
                return 57;
            case 75:
                return 55;
            case 80:
                return 52;
            case 85:
                return 48;
            case 90:
                return 43;
            case 95:
                return 38;
            case 100:
                return 35;
            case 105:
                return 31;
            case 110:
                return 29;
            case 115:
                return 26;
            case 120:
                return 24;
            case 125:
                return 22;
            case 130:
                return 20;
            case 135:
                return 18;
            case 140:
                return 16;
            case 145:
                return 15;
            case 150:
                return 15;
            case 155:
                return 14;
            case 160:
                return 13;
            case 165:
                return 12;
            case 170:
                return 11;
            case 175:
                return 10;
            case 180:
                return 10;
            case 185:
                return 9;
            case 190:
                return 9;
            case 195:
                return 9;
            case 200:
                return 8;
            case 205:
                return 7;
            case 210:
                return 6;
            case 215:
                return 5;
            default:
                return 5;
        }
    }

    public double calculatePoint1Y(double point1X) {
        switch ((int) point1X) {
            case 0:
            case 5:
                return 85;
            case 10:
                return 84;
            case 15:
                return 81;
            case 20:
                return 79;
            case 25:
                return 77;
            case 30:
                return 75;
            case 35:
                return 73;
            case 40:
                return 71;
            case 45:
                return 68;
            case 50:
                return 66;
            case 55:
                return 64;
            case 60:
                return 62;
            case 65:
                return 60;
            case 70:
                return 57;
            case 75:
                return 55;
            case 80:
                return 52;
            case 85:
                return 48;
            case 90:
                return 43;
            case 95:
                return 38;
            case 100:
                return 35;
            case 105:
                return 31;
            case 110:
                return 29;
            case 115:
                return 26;
            case 120:
                return 24;
            case 125:
                return 22;
            case 130:
                return 20;
            case 135:
                return 18;
            case 140:
                return 16;
            case 145:
                return 15;
            case 150:
                return 15;
            case 155:
                return 14;
            case 160:
                return 13;
            case 165:
                return 12;
            case 170:
                return 11;
            case 175:
                return 10;
            case 180:
                return 10;
            case 185:
                return 9;
            case 190:
                return 9;
            case 195:
                return 9;
            case 200:
                return 8;
            case 205:
                return 7;
            case 210:
                return 6;
            case 215:
                return 5;
            default:
                return 5;
        }
    }

    public int calculatePoint2X(double lbyr) {
        return (int) (Math.ceil(lbyr / 5.0) * 5);
    }

    public int calculatePoint1X(double lbyr) {
        return (int) (Math.floor(lbyr / 5.0) * 5);
    }

    public double calculateLbyRTransom(double lxByRx, double lyByRy) {
        return Math.max(lxByRx, lyByRy);
    }

    public double calculateLxByRxTransom(double gridLength, double transomRadiusOfGyrationX) {
        return (gridLength / (transomRadiusOfGyrationX * 10)) * 0.85;
    }

    public double calculateLyByRyTransom(double gridLength, double transomRadiusOfGyrationY) {
        return (gridLength / (transomRadiusOfGyrationY * 10)) * 0.85;
    }

    //both are user input
    public double calculateRadiusOfGyrationX(double transomIxx, double crossSectionArea) {
        if (crossSectionArea <= 0) {
            throw new IllegalArgumentException("Cross section area must be greater than zero.");
        }
        return Math.sqrt(transomIxx / crossSectionArea);
    }

    //both are user input
    public double calculateRadiusOfGyrationY(double transomIyy, double crossSectionArea) {
        if (crossSectionArea <= 0) {
            throw new IllegalArgumentException("Cross section area must be greater than zero.");
        }
        return Math.sqrt(transomIyy / crossSectionArea);
    }


}
