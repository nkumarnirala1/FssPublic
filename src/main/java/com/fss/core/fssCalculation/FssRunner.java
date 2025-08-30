package com.fss.core.fssCalculation;


import com.fss.core.fssCalculation.modal.ExcelElement;
import com.fss.core.fssCalculation.service.utility.ExcelSheetGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class FssRunner {

//    @Autowired
//    private BendingMomentCal bendingMomentCal;
//
//    @Autowired
//    private IxxCal ixxCal;
//
//    @Autowired
//    private DeflectionCal deflectionCal;
//
//    @Autowired
//    Figure1Mullion figure1Mullion;
//
//    @Autowired
//    MullionAbyXandCbyZ mullionAbyXandCbyZ;
//
//    @Autowired
//    Figure2 figure2Mullion;
//
//    @Autowired
//    Figure3 figure3Mullion;
//
//    @Autowired
//    MullionBbyY mullionBbyY;
//
//
     @Autowired
    ExcelSheetGenerator excelSheetGenerator;

    @PostConstruct
    public void run() throws IOException {

        //ArrayList<ExcelElement> excelElementArrayList = excelSheetGenerator.enrichElements(null);


//
//        String typeOfGlazing= "SLIDING WINDOW";
//        String typeOfGlazingValue = "1";
//        double unsupportedLength= 3200;
//        double gridLength= 1200;
//        double windPressure= 2.35;
//        double stackBracket= 300;
//        //--------------------------------
//        double glassThickness= 12;
//        double crossSectionalArea = 6.25;
//        double transomToTransomDistance = unsupportedLength;
//        double b= 60;
//        double a= 100;
//
//        double t2 =3;  // for both transom and mullion but later it should be user input for each case
//        double t1=2;
//        double iyy= 46.24;
//
//        double boundingboxy= 2;
//
//        double  Ixx= ixxCal.calculateRequiredIxx(typeOfGlazing,unsupportedLength,gridLength,windPressure,stackBracket);
//        System.out.println("Calculated,Ixx ="+ Ixx);
//        System.out.println("Allowable deflection is "+ deflectionCal.calculateDeflection(typeOfGlazing,unsupportedLength,gridLength,windPressure,stackBracket, Ixx));
//        System.out.println("Calculated,Deflection ="+ deflectionCal.calculateDeflection(typeOfGlazing,unsupportedLength,gridLength,windPressure,stackBracket, 200));
//
//        double bendingMoment = bendingMomentCal.calculateBendingMoment(typeOfGlazing, unsupportedLength, gridLength, windPressure, stackBracket);
//
//        BigDecimal roundedMoment = new BigDecimal(bendingMoment).setScale(2, RoundingMode.HALF_UP);
//
//        System.out.println("Bending Moment is = "+roundedMoment);
//
//
//        double selfWeight = mullionAbyXandCbyZ.calculateSelfWeight(crossSectionalArea);
//        double udldeadload= mullionAbyXandCbyZ.calculateUdlLoad(gridLength,unsupportedLength,glassThickness);
//        double axialForce = mullionAbyXandCbyZ.calculateAxialForce(udldeadload,selfWeight, unsupportedLength);//wrong
//        double fig1Value = figure1Mullion.calculateFig1Value(typeOfGlazingValue, unsupportedLength, transomToTransomDistance, Ixx,iyy , crossSectionalArea);
//
//        double mbyz = mullionAbyXandCbyZ.calculateMbyZ(Double.valueOf(roundedMoment.toString()),Ixx, boundingboxy);
//
//        //-------------------------
//        double point1Xk1 = figure3Mullion.calculatePoint1xk1(t2,t1);
//        double point1Yk1= figure3Mullion.calculatePoint1yk1(a,b, (int) point1Xk1);
//        double point2Xk1= figure3Mullion.calculatePoint2xk1(t2,t1);
//        double point2Yk1= figure3Mullion.calculatePoint2yk1(a,b, (int)point2Xk1);
//
//        double figure3Value = figure3Mullion.calculateFig3Value(point1Xk1, point1Yk1, point2Xk1, point2Yk1, t2, t1);
//        double lambdaAt= figure2Mullion.calculateLambdaAt(figure3Value,transomToTransomDistance, b);
//        double point1x_lmd = figure2Mullion.calculatepoint1X_lmd(lambdaAt);
//        double point1Y_lmd = figure2Mullion.calculatepoint1Y_lmd( (int)point1x_lmd);
//        double point2X_lmd = figure2Mullion.calculatepoint2X_lmd(lambdaAt);
//        double point2Y_lmd = figure2Mullion.calculatepoint2Y_lmd((int)point2X_lmd);
//        double fig2Value = figure2Mullion.calculateFig2Value(point1x_lmd,point1Y_lmd,point2X_lmd, point2Y_lmd,lambdaAt);
//
//
//        double axcz= mullionAbyXandCbyZ.calculateAbyXandCbyZ(axialForce, crossSectionalArea, fig1Value, mbyz, fig2Value);
//
//        double udlWindLoad= CalculatedElements.calculateUDLDueToWindLoad(gridLength, unsupportedLength, windPressure);
//        double shearForce = mullionBbyY.calculateShearForceMullion("1", udlWindLoad,unsupportedLength, bendingMoment);//wrong
//        double hx = mullionBbyY.calculateHx(a,t2);
//        double by = mullionBbyY.calculateBbyY(shearForce,hx ,t1);
//
//        System.out.println("calculated - a/x+b/y="+axcz);
//        System.out.println("calculated - c/z="+by);


    }
}
