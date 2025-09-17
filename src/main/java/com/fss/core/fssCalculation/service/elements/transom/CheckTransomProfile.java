package com.fss.core.fssCalculation.service.elements.transom;


import com.fss.core.fssCalculation.modal.input.TransomInput;
import com.fss.core.fssCalculation.modal.output.TransomOutput;
import com.fss.core.fssCalculation.service.elements.transom.figures.Figure1Transom;
import com.fss.core.fssCalculation.service.elements.transom.figures.Figure2Transom;
import com.fss.core.fssCalculation.service.elements.transom.figures.Figure3Transom;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CheckTransomProfile {

    private final  TransomBbyY transomBbyY;
    private  final Figure1Transom figure1Transom;
    private  final Figure2Transom figure2Transom;
    private  final Figure3Transom figure3Transom;
    private  final TransomAbyXandCbyZ transomAbyXandCbyZ;

    public CheckTransomProfile(TransomBbyY transomBbyY, Figure1Transom figure1Transom, Figure2Transom figure2Transom, Figure3Transom figure3Transom, TransomAbyXandCbyZ transomAbyXandCbyZ) {
        this.transomBbyY = transomBbyY;
        this.figure1Transom = figure1Transom;
        this.figure2Transom = figure2Transom;
        this.figure3Transom = figure3Transom;
        this.transomAbyXandCbyZ = transomAbyXandCbyZ;
    }

    public TransomOutput checkForTransomProfile(TransomInput transomInput) {
        TransomOutput transomOutput = new TransomOutput();

        Map<String, Boolean> resultMap = new HashMap<>();
        transomOutput.setResultMap(resultMap);

        double gridLength = transomInput.getGridLength();
        double windPressure = transomInput.getWindPressure();
        double glassThickness = transomInput.getGlassThickness();
        double transomTopPanelHeight = transomInput.getTransomTopPanelHeight();
        double transomBottomPanelHeight = transomInput.getTransomBottomPanelHeight();
        double depthOfSectionTransom = transomInput.getDepthOfSectionTransom();
        double t2Transom = transomInput.getT2Transom();
        double thicknessTaTransom_t1 = transomInput.getThicknessTaTransom_t1();
        double transomIxx = transomInput.getTransomIxx();
        double transomIyy = transomInput.getTransomIyy();
        double transomBoundingBoxY = transomInput.getTransomBoundingBoxY();
        double transomCrossSectionalArea = transomInput.getTransomCrossSectionalArea();
        double transomUnsupportedLength = transomInput.getTransomUnsupportedLength();
        double sectionWidthB = transomInput.getSectionWidthB();


        double abyB = depthOfSectionTransom / sectionWidthB;



        double fig1Value = figure1Transom.calculateFig1ValueTransom(transomIxx, transomIyy, transomCrossSectionalArea, gridLength);
        double fig3Value = figure3Transom.calculateFig3Value(abyB, t2Transom, thicknessTaTransom_t1);
        double fig2Value = figure2Transom.calculateFigure2Value(fig3Value, transomUnsupportedLength, sectionWidthB);

        double aByXandCbyZ = transomAbyXandCbyZ.calculateAbyXandCbyZ(windPressure, transomTopPanelHeight, transomBottomPanelHeight, transomIxx, transomBoundingBoxY, transomCrossSectionalArea, transomUnsupportedLength, glassThickness, fig1Value, fig2Value);
        double bByY = transomBbyY.calculateBbyY(windPressure, transomTopPanelHeight, transomBottomPanelHeight, depthOfSectionTransom, gridLength, t2Transom, thicknessTaTransom_t1);

        transomOutput.setAbyB(abyB);
        transomOutput.setFig1Value(fig1Value);
        transomOutput.setFig2Value(fig2Value);
        transomOutput.setFig3Value(fig3Value);

        transomOutput.setAByXandCbyZ(aByXandCbyZ);
        transomOutput.setBByY(bByY);

        resultMap.put("bendingStress", aByXandCbyZ < 1);
        resultMap.put("shearStress", bByY < 1);

        return transomOutput;
    }
}
