package com.fss.core.fssCalculation.service.estimation.Interface;

public interface IEstimation {

    double profileEstimation(double endShutterCrossSectionalArea, double innerLockACrossSectionalArea, double innerLockBCrossSectionalArea, double outerCrossSectionalArea, double gl, double ul, double rate);
}


