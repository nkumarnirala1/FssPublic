package com.fss.core.fssCalculation.service.elements.mullion.figures;

import com.fss.core.fssCalculation.service.ReportGen.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class Figure1Mullion {

    public double calculateFig1Value(String typeOfGlazing, double unsupportedLength, double transomToTransomDistance, double ixx, double iyy, double crossSectionArea, HttpSession session) {

        double radiusOfGyrationX = calculateRadiusOfGyrationX(ixx, crossSectionArea);
        double radiusOfGyrationY = calculateRadiusOfGyrationY(iyy, crossSectionArea);
        session.setAttribute("radiusOfGyrationX_mullion", Utility.roundTo2Decimal(radiusOfGyrationX));
        session.setAttribute("radiusOfGyrationY_mullion", Utility.roundTo2Decimal(radiusOfGyrationY));
        session.setAttribute("radiusOfGyrationX_mullion_mm", Utility.roundTo2Decimal(radiusOfGyrationX*10.0));
        session.setAttribute("radiusOfGyrationY_mullion_mm", Utility.roundTo2Decimal(radiusOfGyrationY*10.0));


        double lxByRx = calculateLxByRx(typeOfGlazing, unsupportedLength, radiusOfGyrationX);
        double lyByRy = calculateLyByRy(typeOfGlazing, unsupportedLength, transomToTransomDistance, radiusOfGyrationY);

        session.setAttribute("lxByRx",Utility.roundTo2Decimal(lxByRx));
        session.setAttribute("lyByRy", Utility.roundTo2Decimal(lyByRy));
        session.setAttribute("max_lxByRxInlyByRy", Math.max(lxByRx, lyByRy));
        double slendernessRatioLbyR = getSlendernessRatioLbyR(lxByRx, lyByRy);
        double point1XPermissibleCompressiveStress = getPoint1XPermissibleCompressiveStress(slendernessRatioLbyR);
        double point2XPermissibleCompressiveStress = getPoint2XPermissibleCompressiveStress(slendernessRatioLbyR);
        double point1YPermissibleCompressiveStress = getPoint1YPermissibleCompressiveStress((int)point1XPermissibleCompressiveStress);
        double point2YPermissibleCompressiveStress = getPoint2YPermissibleCompressiveStress((int)point2XPermissibleCompressiveStress);

//        // Debug print for all local variables
//        System.out.println("---- calculateFig1Value Debug ----");
//        System.out.println("typeOfGlazing: " + typeOfGlazing);
//        System.out.println("unsupportedLength: " + unsupportedLength);
//        System.out.println("transomToTransomDistance: " + transomToTransomDistance);
//        System.out.println("ixx: " + ixx);
//        System.out.println("iyy: " + iyy);
//        System.out.println("crossSectionArea: " + crossSectionArea);
//        System.out.println("radiusOfGyrationX: " + radiusOfGyrationX);
//        System.out.println("radiusOfGyrationY: " + radiusOfGyrationY);
//        System.out.println("lxByRx: " + lxByRx);
//        System.out.println("lyByRy: " + lyByRy);
//        System.out.println("slendernessRatioLbyR: " + slendernessRatioLbyR);
//        System.out.println("point1XPermissibleCompressiveStress: " + point1XPermissibleCompressiveStress);
//        System.out.println("point2XPermissibleCompressiveStress: " + point2XPermissibleCompressiveStress);
//        System.out.println("point1YPermissibleCompressiveStress: " + point1YPermissibleCompressiveStress);
//        System.out.println("point2YPermissibleCompressiveStress: " + point2YPermissibleCompressiveStress);
//        System.out.println("----------------------------------");
        return Utility.roundTo2Decimal(calculate(point2YPermissibleCompressiveStress, point1YPermissibleCompressiveStress, point2XPermissibleCompressiveStress, point1XPermissibleCompressiveStress, slendernessRatioLbyR));


    }

    private double calculate(
            double point2YPermissibleCompressiveStress,
            double point1YPermissibleCompressiveStress,
            double point2XPermissibleCompressiveStress,
            double point1XPermissibleCompressiveStress,
            double slendernessRatioLbyR // [L/r]
    ) {
        double slope = (point2YPermissibleCompressiveStress - point1YPermissibleCompressiveStress) /
                (point2XPermissibleCompressiveStress - point1XPermissibleCompressiveStress);

        return slope * (slendernessRatioLbyR - point1XPermissibleCompressiveStress)
                + point1YPermissibleCompressiveStress;

        /*
        (([point2_Y_permisible_compressive stress]-[point1_Y_permisible_compressive stress])/([point2_X_permisible_compressive stress ]-[point1_X_permisible_compressive stress]))*

        ([L/r]-[point1_X_permisible_compressive stress]) + [point1_Y_permisible_compressive stress]
         */
    }

    public double getPoint2XPermissibleCompressiveStress(double slendernessRatioLbyR) {
        return (Math.ceil(slendernessRatioLbyR / 5.0) * 5.0);
    }

    public double getPoint1XPermissibleCompressiveStress(double slendernessRatioLbyR) {
        return (Math.floor(slendernessRatioLbyR / 5.0) * 5.0);
    }

    public double getPoint2YPermissibleCompressiveStress(int point2XPermissibleCompressiveStress) {
        Map<Integer, Integer> stressMap = new HashMap<>();
        stressMap.put(0, 85);
        stressMap.put(5, 85);
        stressMap.put(10, 84);
        stressMap.put(15, 81);
        stressMap.put(20, 79);
        stressMap.put(25, 77);
        stressMap.put(30, 75);
        stressMap.put(35, 73);
        stressMap.put(40, 71);
        stressMap.put(45, 68);
        stressMap.put(50, 66);
        stressMap.put(55, 64);
        stressMap.put(60, 62);
        stressMap.put(65, 60);
        stressMap.put(70, 57);
        stressMap.put(75, 55);
        stressMap.put(80, 52);
        stressMap.put(85, 48);
        stressMap.put(90, 43);
        stressMap.put(95, 38);
        stressMap.put(100, 35);
        stressMap.put(105, 31);
        stressMap.put(110, 29);
        stressMap.put(115, 26);
        stressMap.put(120, 24);
        stressMap.put(125, 22);
        stressMap.put(130, 20);
        stressMap.put(135, 18);
        stressMap.put(140, 16);
        stressMap.put(145, 15);
        stressMap.put(150, 15);
        stressMap.put(155, 14);
        stressMap.put(160, 13);
        stressMap.put(165, 12);
        stressMap.put(170, 11);
        stressMap.put(175, 10);
        stressMap.put(180, 10);
        stressMap.put(185, 9);
        stressMap.put(190, 9);
        stressMap.put(195, 9);
        stressMap.put(200, 8);
        stressMap.put(205, 7);
        stressMap.put(210, 6);
        stressMap.put(215, 5);

        // Default value is 5 if key is not found
        return stressMap.getOrDefault(point2XPermissibleCompressiveStress, 5);
    }

    public double getPoint1YPermissibleCompressiveStress(int point1XPermissibleCompressiveStress) {
        Map<Integer, Integer> stressMap = new HashMap<>();
        stressMap.put(0, 85);
        stressMap.put(5, 85);
        stressMap.put(10, 84);
        stressMap.put(15, 81);
        stressMap.put(20, 79);
        stressMap.put(25, 77);
        stressMap.put(30, 75);
        stressMap.put(35, 73);
        stressMap.put(40, 71);
        stressMap.put(45, 68);
        stressMap.put(50, 66);
        stressMap.put(55, 64);
        stressMap.put(60, 62);
        stressMap.put(65, 60);
        stressMap.put(70, 57);
        stressMap.put(75, 55);
        stressMap.put(80, 52);
        stressMap.put(85, 48);
        stressMap.put(90, 43);
        stressMap.put(95, 38);
        stressMap.put(100, 35);
        stressMap.put(105, 31);
        stressMap.put(110, 29);
        stressMap.put(115, 26);
        stressMap.put(120, 24);
        stressMap.put(125, 22);
        stressMap.put(130, 20);
        stressMap.put(135, 18);
        stressMap.put(140, 16);
        stressMap.put(145, 15);
        stressMap.put(150, 15);
        stressMap.put(155, 14);
        stressMap.put(160, 13);
        stressMap.put(165, 12);
        stressMap.put(170, 11);
        stressMap.put(175, 10);
        stressMap.put(180, 10);
        stressMap.put(185, 9);
        stressMap.put(190, 9);
        stressMap.put(195, 9);
        stressMap.put(200, 8);
        stressMap.put(205, 7);
        stressMap.put(210, 6);
        stressMap.put(215, 5);

        // Default value if no match
        return stressMap.getOrDefault(point1XPermissibleCompressiveStress, 5);
    }

    public double getSlendernessRatioLbyR(double LxByrx, double LyByry) {
        return Math.max(LxByrx, LyByry);
    }

    public double calculateLxByRx(String typeOfGlazing, double unsupportedLength, double radiusOfGyrationX) {
        if ("1".equals(typeOfGlazing) ||
                "3".equals(typeOfGlazing) ||
                "4".equals(typeOfGlazing) ||
                "5".equals(typeOfGlazing)) {

            return Utility.roundTo2Decimal( (unsupportedLength / (radiusOfGyrationX * 10.0))*0.85);// kx=0.85
        }
        return 0.0;
    }

    public double calculateLyByRy(
            String typeOfGlazing,
            double unsupportedLength,
            double transomToTransomDistance,
            double radiusOfGyrationY
    ) {
        if ("1".equals(typeOfGlazing)) {
            return (unsupportedLength / (radiusOfGyrationY * 10.0))*0.85;// kx=0.85;
        } else if ("3".equals(typeOfGlazing) ||
                "4".equals(typeOfGlazing) ||
                "5".equals(typeOfGlazing)) {
            return (transomToTransomDistance / (radiusOfGyrationY * 10.0))*0.85;// kx=0.85;
        }

        return 0.0;
    }

    public double calculateRadiusOfGyrationY(double Iyy, double crossSectionArea) {
        if (crossSectionArea <= 0) {
            throw new IllegalArgumentException("Cross section area must be greater than zero.");
        }
        return Math.sqrt(Iyy / crossSectionArea);
    }

    public double calculateRadiusOfGyrationX(double Ixx, double crossSectionArea) {
        if (crossSectionArea <= 0) {
            throw new IllegalArgumentException("Cross section area must be greater than zero.");
        }
        return Math.sqrt(Ixx / crossSectionArea);
    }


}
