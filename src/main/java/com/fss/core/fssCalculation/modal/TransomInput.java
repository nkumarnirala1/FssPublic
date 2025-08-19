package com.fss.core.fssCalculation.modal;


import lombok.Data;

@Data
public class TransomInput {

    private double gridLength = 1200.0;
    private double windPressure = 2.35;
    private double glassThickness = 12.0;
    private double transomTopPanelHeight = 1700.0;
    private double transomBottomPanelHeight = 1500.0;
    private double depthOfSectionTransom = 101.0;
    private double t2Transom = 2.6;
    private double thicknessTaTransom_t1 = 2.6;
    private double transomIxx = 46.24;
    private double transomIyy = 150.9;
    private double transomBoundingBoxY = 2.85;
    private double transomCrossSectionalArea = 9.6;
    private double transomUnsupportedLength = 1200.0;
    private double sectionWidthB = 10.0;
}

