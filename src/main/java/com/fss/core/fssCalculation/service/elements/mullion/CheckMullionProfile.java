package com.fss.core.fssCalculation.service.elements.mullion;

import com.fss.core.fssCalculation.constants.GlazingType;
import com.fss.core.fssCalculation.modal.input.GlazingInput;
import com.fss.core.fssCalculation.modal.input.MullionInput;
import com.fss.core.fssCalculation.modal.output.FigureOneOutput;
import com.fss.core.fssCalculation.modal.output.MullionProfileOutput;
import com.fss.core.fssCalculation.service.ReportGen.Utility;
import com.fss.core.fssCalculation.service.elements.CalculatedElements;
import com.fss.core.fssCalculation.service.elements.mullion.figures.Figure1Mullion;
import com.fss.core.fssCalculation.service.elements.mullion.figures.Figure2Mullion;
import com.fss.core.fssCalculation.service.elements.mullion.figures.Figure3Mullion;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CheckMullionProfile {

    final MullionAbyXandCbyZ mullionAbyXandCbyZ;
    final MullionBbyY mullionBbyY;
    final Figure1Mullion figure1Mullion;
    final Figure2Mullion figure2Mullion;
    final Figure3Mullion figure3Mullion;

    public CheckMullionProfile(MullionAbyXandCbyZ mullionAbyXandCbyZ, MullionBbyY mullionBbyY, Figure1Mullion figure1Mullion, Figure2Mullion figure2Mullion, Figure3Mullion figure3Mullion) {
        this.mullionAbyXandCbyZ = mullionAbyXandCbyZ;
        this.mullionBbyY = mullionBbyY;
        this.figure1Mullion = figure1Mullion;
        this.figure2Mullion = figure2Mullion;
        this.figure3Mullion = figure3Mullion;
    }

    public MullionProfileOutput checkForMullionprofile(MullionInput mullionInput, GlazingInput glazingInput, double bendingMoment) {
        double glassThickness = mullionInput.getGlassThickness();
        double crossSectionalArea = mullionInput.getCrossSectionalArea();
        double transomToTransomDistance = mullionInput.getTransomToTransomDistance();
        double b = mullionInput.getB();
        double a = mullionInput.getA();
        double t2 = mullionInput.getT2();
        double t1 = mullionInput.getT1();
        double iyy = mullionInput.getIyy();
        double ixx = mullionInput.getUserIxx();

        MullionProfileOutput mullionProfileOutput = new MullionProfileOutput();

        String typeOfGlazingValue = GlazingType.findCode(glazingInput.getTypeOfGlazing());
        mullionProfileOutput.setTypeOfGlazingValue(typeOfGlazingValue);
        double unsupportedLength = glazingInput.getUnsupportedLength();
        mullionProfileOutput.setUnsupportedLength(unsupportedLength);
        double gridLength = glazingInput.getGridLength();
        mullionProfileOutput.setGridLength(gridLength);
        double windPressure = glazingInput.getWindPressure();
        mullionProfileOutput.setWindPressure(windPressure);

        double boundingboxy = mullionInput.getBoundingboxy();
        mullionProfileOutput.setBoundingBoxy(boundingboxy);
        double selfWeight = CalculatedElements.calculateSelfWeight(crossSectionalArea);
        mullionProfileOutput.setSelfWeight(selfWeight);
        double udldeadload = CalculatedElements.calculateUdlDueToDeadLoad(gridLength, unsupportedLength, glassThickness);
        mullionProfileOutput.setUdlDeadLoad(udldeadload);
        double effectiveArea = CalculatedElements.calculateEffectiveArea(gridLength, unsupportedLength);
        mullionProfileOutput.setEffectiveArea(effectiveArea);
        double axialForce = CalculatedElements.calculateAxialForce(udldeadload, selfWeight, unsupportedLength);
        mullionProfileOutput.setAxialForce(axialForce);

        double ft_mullion = Utility.roundTo2Decimal((axialForce * 1000.0) / (crossSectionalArea * 100.0));
        mullionProfileOutput.setFtMullion(ft_mullion);

        FigureOneOutput fig1Value = figure1Mullion.calculateFig1Value(typeOfGlazingValue, unsupportedLength, transomToTransomDistance, ixx, iyy, crossSectionalArea);
        mullionProfileOutput.setFig1Value(fig1Value);

        double mbyz = mullionAbyXandCbyZ.calculateMbyZ(bendingMoment, ixx, boundingboxy);
        mullionProfileOutput.setMByz(mbyz);

        //-------------------------
        double point1Xk1 = figure3Mullion.calculatePoint1xk1(t2, t1);
        mullionProfileOutput.setPoint1Xk1(point1Xk1);
        double point1Yk1 = figure3Mullion.calculatePoint1yk1(a, b, (int) point1Xk1);
        mullionProfileOutput.setPoint1Yk1(point1Yk1);
        double point2Xk1 = figure3Mullion.calculatePoint2xk1(t2, t1);
        mullionProfileOutput.setPoint2Xk1(point2Xk1);
        double point2Yk1 = figure3Mullion.calculatePoint2yk1(a, b, (int) point2Xk1);
        mullionProfileOutput.setPoint2Yk1(point2Yk1);

        double fig3Value = figure3Mullion.calculateFig3Value(point1Xk1, point1Yk1, point2Xk1, point2Yk1, t2, t1);
        mullionProfileOutput.setFig3Value(fig3Value);
        double lambdaAt = figure2Mullion.calculateLambdaAt(fig3Value, transomToTransomDistance, b);
        mullionProfileOutput.setLambdaAt(lambdaAt);
        double point1x_lmd = figure2Mullion.calculatepoint1X_lmd(lambdaAt);
        mullionProfileOutput.setPoint1xLmd(point1x_lmd);
        double point1Y_lmd = figure2Mullion.calculatepoint1Y_lmd((int) point1x_lmd);
        mullionProfileOutput.setPoint1YLmd(point1Y_lmd);
        double point2X_lmd = figure2Mullion.calculatepoint2X_lmd(lambdaAt);
        mullionProfileOutput.setPoint2XLmd(point2X_lmd);
        double point2Y_lmd = figure2Mullion.calculatepoint2Y_lmd((int) point2X_lmd);
        mullionProfileOutput.setPoint2YLmd(point2Y_lmd);
        double fig2Value = figure2Mullion.calculateFig2Value(point1x_lmd, point1Y_lmd, point2X_lmd, point2Y_lmd, lambdaAt);
        mullionProfileOutput.setFig2Value(fig2Value);


        double udlWindLoad = CalculatedElements.calculateUDLDueToWindLoad(gridLength, unsupportedLength, windPressure);
        mullionProfileOutput.setUdlWindLoad(udlWindLoad);
        double shearForce = CalculatedElements.calculateShearForce(typeOfGlazingValue, udlWindLoad, unsupportedLength, bendingMoment);
        mullionProfileOutput.setShearForce(shearForce);
        double hx = mullionBbyY.calculateHx(a, t2);
        mullionProfileOutput.setHx(hx);

        double aByXandCbyZ = mullionAbyXandCbyZ.calculateAbyXandCbyZ(axialForce, crossSectionalArea, fig1Value.getResult(), mbyz, fig2Value);
        mullionProfileOutput.setAByXandCbyZ(aByXandCbyZ);

        double bByY = mullionBbyY.calculateBbyY(shearForce, hx, t1);
        mullionProfileOutput.setBByY(bByY);

        Map<String, Boolean> resultMap = new HashMap<>();
        mullionProfileOutput.setResultMap(resultMap);

        resultMap.put("bendingStress", aByXandCbyZ < 1);
        resultMap.put("shearStress", bByY < 1);

        return mullionProfileOutput;
    }
}
