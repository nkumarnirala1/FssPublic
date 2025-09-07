package com.fss.core.fssCalculation.service.elements.mullion;

import com.fss.core.fssCalculation.constants.GlazingType;
import com.fss.core.fssCalculation.modal.GlazingInput;
import com.fss.core.fssCalculation.modal.MullionInput;
import com.fss.core.fssCalculation.service.elements.CalculatedElements;
import com.fss.core.fssCalculation.service.elements.mullion.figures.Figure1Mullion;
import com.fss.core.fssCalculation.service.elements.mullion.figures.Figure2Mullion;
import com.fss.core.fssCalculation.service.elements.mullion.figures.Figure3Mullion;
import com.fss.core.fssCalculation.service.utility.Utility;
import jakarta.servlet.http.HttpSession;
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

    public Map<String, Boolean> checkForMullionprofile(MullionInput mullionInput , GlazingInput glazingInput, double bendingMoment, HttpSession session) {
        double glassThickness = mullionInput.getGlassThickness();
        double crossSectionalArea = mullionInput.getCrossSectionalArea();
        double transomToTransomDistance = mullionInput.getTransomToTransomDistance();
        double b = mullionInput.getB();
        double a = mullionInput.getA();
        double t2 = mullionInput.getT2();
        double t1 = mullionInput.getT1();

        session.setAttribute("a/b", a/b);
        session.setAttribute("t2/t1",t2/t1);
        session.setAttribute("hy",b-(2*t1) );
        double iyy = mullionInput.getIyy();
        double ixx = mullionInput.getUserIxx();

        String typeOfGlazingValue = GlazingType.findCode(glazingInput.getTypeOfGlazing());
        double unsupportedLength =glazingInput.getUnsupportedLength();
        double gridLength = glazingInput.getGridLength();
        double windPressure = glazingInput.getWindPressure();

        double boundingboxy = mullionInput.getBoundingboxy();
        double selfWeight = mullionAbyXandCbyZ.calculateSelfWeight(crossSectionalArea);
        double udldeadload = mullionAbyXandCbyZ.calculateUdlLoad(gridLength, unsupportedLength,glassThickness, session);
        double axialForce = mullionAbyXandCbyZ.calculateAxialForce(udldeadload, selfWeight, unsupportedLength);
        session.setAttribute("axialForce",  Utility.roundTo2Decimal(axialForce));

        double ft_mullion= (axialForce*1000.0)/(crossSectionalArea*100.0);
        session.setAttribute("ft_mullion",ft_mullion);

        session.setAttribute("fa",  axialForce/crossSectionalArea);

        session.setAttribute("udldeadload",Utility.roundTo2Decimal(udldeadload));
        session.setAttribute("selfWeight", Utility.roundTo2Decimal(selfWeight));
        double fig1Value = figure1Mullion.calculateFig1Value(typeOfGlazingValue, unsupportedLength, transomToTransomDistance, ixx, iyy, crossSectionalArea, session);

        double mbyz = mullionAbyXandCbyZ.calculateMbyZ(bendingMoment, ixx, boundingboxy, session);
        session.setAttribute("mbyz",mbyz);

        //-------------------------
        double point1Xk1 = figure3Mullion.calculatePoint1xk1(t2, t1);
        double point1Yk1 = figure3Mullion.calculatePoint1yk1(a, b, (int) point1Xk1);
        double point2Xk1 = figure3Mullion.calculatePoint2xk1(t2, t1);
        double point2Yk1 = figure3Mullion.calculatePoint2yk1(a, b, (int) point2Xk1);

        double fig3Value = figure3Mullion.calculateFig3Value(point1Xk1, point1Yk1, point2Xk1, point2Yk1, t2, t1);
        double lambdaAt = figure2Mullion.calculateLambdaAt(fig3Value, transomToTransomDistance, b);
        double point1x_lmd = figure2Mullion.calculatepoint1X_lmd(lambdaAt);
        double point1Y_lmd = figure2Mullion.calculatepoint1Y_lmd((int) point1x_lmd);
        double point2X_lmd = figure2Mullion.calculatepoint2X_lmd(lambdaAt);
        double point2Y_lmd = figure2Mullion.calculatepoint2Y_lmd((int) point2X_lmd);
        double fig2Value = figure2Mullion.calculateFig2Value(point1x_lmd, point1Y_lmd, point2X_lmd, point2Y_lmd, lambdaAt);

        session.setAttribute("lambdaAt", lambdaAt );
        session.setAttribute("fig1Value", fig1Value);
        session.setAttribute("fig2Value", fig2Value);
        session.setAttribute("fig3Value", fig3Value);
        session.setAttribute("axial_bending_check", (ft_mullion/fig1Value)+(mbyz/fig2Value ));

        double udlWindLoad = CalculatedElements.calculateUDLDueToWindLoad(gridLength, unsupportedLength, windPressure);
        double shearForce = mullionBbyY.calculateShearForceMullion("1", udlWindLoad, unsupportedLength, bendingMoment);
        session.setAttribute("shearForce_mullion",  Utility.roundTo2Decimal(shearForce));
        double hx = mullionBbyY.calculateHx(a, t2);
        session.setAttribute("hx",hx);
        session.setAttribute("fsx_mullion",  Utility.roundTo2Decimal(shearForce/(hx*t1)));

        double aByXandCbyZ = mullionAbyXandCbyZ.calculateAbyXandCbyZ(axialForce, crossSectionalArea, fig1Value, mbyz, fig2Value);

        double bByY = mullionBbyY.calculateBbyY(shearForce, hx, t1);

        Map<String, Boolean> resultMap= new HashMap<>();

        resultMap.put("bendingStress", aByXandCbyZ < 1);
        resultMap.put("shearStress", bByY < 1);

        return resultMap;
    }
}
