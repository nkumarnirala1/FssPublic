package com.fss.core.fssCalculation;

import com.fss.core.fssCalculation.service.elements.bendingMoment.BendingMomentCal;
import com.fss.core.fssCalculation.service.elements.deflection.DeflectionCal;
import com.fss.core.fssCalculation.service.elements.momentCal.IxxCal;
import com.fss.core.fssCalculation.service.elements.CalculatedElements;
import com.fss.core.fssCalculation.service.elements.mullion.MullionAbyXandCbyZ;
import com.fss.core.fssCalculation.service.elements.mullion.MullionBbyY;
import com.fss.core.fssCalculation.service.elements.mullion.figures.Figure1Mullion;
import com.fss.core.fssCalculation.service.elements.mullion.figures.Figure2Mullion;
import com.fss.core.fssCalculation.service.elements.mullion.figures.Figure3Mullion;
import com.fss.core.fssCalculation.service.elements.transom.TransomAbyXandCbyZ;
import com.fss.core.fssCalculation.service.elements.transom.TransomBbyY;
import com.fss.core.fssCalculation.service.elements.transom.figures.Figure1Transom;
import com.fss.core.fssCalculation.service.elements.transom.figures.Figure2Transom;
import com.fss.core.fssCalculation.service.elements.transom.figures.Figure3Transom;
import com.fss.core.fssCalculation.service.ReportGen.ExcelSheetGenerator;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
class FssCalculationApplicationTests {

    @Autowired
    private BendingMomentCal bendingMomentCal;

    @Autowired
    private IxxCal ixxCal;

    @Autowired
    private DeflectionCal deflectionCal;

    @Autowired
    Figure1Mullion figure1Mullion;

    @Autowired
    MullionAbyXandCbyZ mullionAbyXandCbyZ;

    @Autowired
    Figure2Mullion figure2Mullion;

    @Autowired
    Figure3Mullion figure3Mullion;

    @Autowired
    MullionBbyY mullionBbyY;

    @Autowired
    TransomBbyY transomBbyY;

    @Autowired
    TransomAbyXandCbyZ transomAbyXandCbyZ;

    @Autowired
    Figure1Transom figure1Transom;

    @Autowired
    Figure2Transom figure2Transom;

    @Autowired
    Figure3Transom figure3Transom;

    @Autowired
    ExcelSheetGenerator excelSheetGenerater;

    @Test
    void mullionStressAndDeflectionCheck() throws IOException {

        String typeOfGlazing = "SLIDING WINDOW";
        String typeOfGlazingValue = "1";
        double unsupportedLength = 3200;
        double gridLength = 1200;
        double windPressure = 2.35;
        double stackBracket = 300;
        //--------------------------------
        double glassThickness = 12;
        double crossSectionalArea = 6.25;
        double transomToTransomDistance = unsupportedLength;
        double b = 60;
        double a = 100;

        double t2 = 3;  // for both transom and mullion but later it should be user input for each case
        double t1 = 2;
        double iyy = 46.24;

        double boundingboxy = 2;

        HttpSession session = new MockHttpSession();

        double Ixx = ixxCal.calculateRequiredIxx(typeOfGlazing, unsupportedLength, gridLength, windPressure, stackBracket , session);
        double allowableDeflection = deflectionCal.calculateDeflection(typeOfGlazing, unsupportedLength, gridLength, windPressure, stackBracket, Ixx);
        double calculatedDeflection = deflectionCal.calculateDeflection(typeOfGlazing, unsupportedLength, gridLength, windPressure, stackBracket, 200);

        double bendingMoment = bendingMomentCal.calculateBendingMoment(typeOfGlazing, unsupportedLength, gridLength, windPressure, stackBracket);

        BigDecimal roundedMoment = new BigDecimal(bendingMoment).setScale(2, RoundingMode.HALF_UP);

        System.out.println("Bending Moment is = " + roundedMoment);


        double selfWeight = mullionAbyXandCbyZ.calculateSelfWeight(crossSectionalArea);
        double udldeadload = mullionAbyXandCbyZ.calculateUdlLoad(gridLength, unsupportedLength, glassThickness, session);
        double axialForce = mullionAbyXandCbyZ.calculateAxialForce(udldeadload, selfWeight, unsupportedLength);//wrong
        double fig1Value = figure1Mullion.calculateFig1Value(typeOfGlazingValue, unsupportedLength, transomToTransomDistance, Ixx, iyy, crossSectionalArea, session);

        double mbyz = mullionAbyXandCbyZ.calculateMbyZ(Double.valueOf(roundedMoment.toString()), Ixx,boundingboxy , session);

        //-------------------------
        double point1Xk1 = figure3Mullion.calculatePoint1xk1(t2, t1);
        double point1Yk1 = figure3Mullion.calculatePoint1yk1(a, b, (int) point1Xk1);
        double point2Xk1 = figure3Mullion.calculatePoint2xk1(t2, t1);
        double point2Yk1 = figure3Mullion.calculatePoint2yk1(a, b, (int) point2Xk1);

        double figure3Value = figure3Mullion.calculateFig3Value(point1Xk1, point1Yk1, point2Xk1, point2Yk1, t2, t1);
        double lambdaAt = figure2Mullion.calculateLambdaAt(figure3Value, transomToTransomDistance, b);
        double point1x_lmd = figure2Mullion.calculatepoint1X_lmd(lambdaAt);
        double point1Y_lmd = figure2Mullion.calculatepoint1Y_lmd((int) point1x_lmd);
        double point2X_lmd = figure2Mullion.calculatepoint2X_lmd(lambdaAt);
        double point2Y_lmd = figure2Mullion.calculatepoint2Y_lmd((int) point2X_lmd);
        double fig2Value = figure2Mullion.calculateFig2Value(point1x_lmd, point1Y_lmd, point2X_lmd, point2Y_lmd, lambdaAt);


        double axcz = mullionAbyXandCbyZ.calculateAbyXandCbyZ(axialForce, crossSectionalArea, fig1Value, mbyz, fig2Value);

        double udlWindLoad = CalculatedElements.calculateUDLDueToWindLoad(gridLength, unsupportedLength, windPressure);
        double shearForce = mullionBbyY.calculateShearForceMullion("1", udlWindLoad, unsupportedLength, bendingMoment);//wrong
        double hx = mullionBbyY.calculateHx(a, t2);
        double by = mullionBbyY.calculateBbyY(shearForce, hx, t1);
        Assertions.assertEquals(303.2, Ixx, "Ixx should be positive");
        Assertions.assertEquals(18.3, allowableDeflection, "Allowable deflection should be positive");
        Assertions.assertEquals(27.7, calculatedDeflection, "Calculated deflection should be positive");
        Assertions.assertEquals(3.44, roundedMoment.doubleValue(), "Bending moment should be positive");
        Assertions.assertAll(
                () -> Assertions.assertEquals(1.69375, selfWeight, "Self weight should be positive"),
                () -> Assertions.assertEquals(0.2925, udldeadload, "UDL dead load should be positive"),
                () -> Assertions.assertEquals(0.9902, axialForce, "Axial force should be positive"),
                () -> Assertions.assertEquals(35, (int) fig1Value, "Figure 1 value should be positive"),
                () -> Assertions.assertEquals(22.69, roundTo2Decimal(mbyz), "MbyZ should be positive")
        );
        Assertions.assertAll(
                () -> Assertions.assertEquals(3.0, figure3Value, "Figure 3 value should be positive")
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(21.91, roundTo2Decimal(lambdaAt), "LambdaAt should be positive"),
                () -> Assertions.assertEquals(87.24, roundTo2Decimal(fig2Value), "Figure 2 value should be positive")
        );
        Assertions.assertEquals(0.31, roundTo2Decimal(axcz), "A by X and C by Z should be positive");
        Assertions.assertAll(
                () -> Assertions.assertEquals(2.29, roundTo2Decimal(udlWindLoad), "UDL wind load should be positive"),
                () -> Assertions.assertEquals(3.67, roundTo2Decimal(shearForce), "Shear force should be positive"),
                () -> Assertions.assertEquals(94.0, hx, "Hx should be positive"),
                () -> Assertions.assertEquals(0.24, roundTo2Decimal(by), "B by Y should be positive")
        );
    }

    @Test
    void transomStressAndDeflectionCheck() {

        double gridLength = 1200;
        double windPressure = 2.35;
        double glassThickness = 12;
        double transomTopPanelHeight = 1700;
        double transomBottomPanelHeight = 1500;
       // can take from daigramm
        double depthOfSectionTransom = 101; //a
        double t2Transom = 2.6;
        double thicknessTaTransom_t1 = 2.6;

        double transomIxx = 46.24;
        double transomIyy = 150.9;

        double transomBoundingBoxY = 2.85;
        double transomCrossSectionArea = 9.6;
        double transomUnsupportedLength = 1200;
        double sectionWidthB = 10;

        double crossSectionalAreaTransom = 9.6;



        double bByY = transomBbyY.calculateBbyY(windPressure, transomTopPanelHeight, transomBottomPanelHeight, depthOfSectionTransom, gridLength, t2Transom, thicknessTaTransom_t1);
        double abyB = depthOfSectionTransom / sectionWidthB;

        double fig1Value = figure1Transom.calculateFig1ValueTransom(transomIxx, transomIyy, crossSectionalAreaTransom, gridLength);
        double fig3Value = figure3Transom.calculateFig3Value(abyB, t2Transom, thicknessTaTransom_t1);
        double fig2Value = figure2Transom.calculateFigure2Value(fig3Value, transomUnsupportedLength, sectionWidthB);
        double aByXandCbyZ = transomAbyXandCbyZ.calculateAbyXandCbyZ(windPressure, transomTopPanelHeight, transomBottomPanelHeight, transomIxx, transomBoundingBoxY, transomCrossSectionArea, transomUnsupportedLength, glassThickness, fig1Value, fig2Value);

        Assertions.assertEquals(0.11, roundTo2Decimal(bByY));
        Assertions.assertEquals(67.41, roundTo2Decimal(fig1Value));
        Assertions.assertEquals(55.31, roundTo2Decimal(fig2Value));
        Assertions.assertEquals(6.8, roundTo2Decimal(fig3Value));
        Assertions.assertEquals(0.76, roundTo2Decimal(aByXandCbyZ));

    }

    public double roundTo2Decimal(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


}
