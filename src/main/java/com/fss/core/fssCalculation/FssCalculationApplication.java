package com.fss.core.fssCalculation;

import com.fss.core.fssCalculation.service.BendingMomentCal;
import com.fss.core.fssCalculation.service.CentroidCal.RegularRectangular;
import com.fss.core.fssCalculation.service.DeflectionCal;
import com.fss.core.fssCalculation.service.IxxCal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootApplication
public class FssCalculationApplication {

	public static void main(String[] args) {


		SpringApplication.run(FssCalculationApplication.class, args);
		//System.out.println("Your Application started successfully");

//		String typeOfGlazing= "SLIDING WINDOW";
//		double unsupportedLength= 3200;
//		double gridLength= 1200;
//		double windPressure= 2.35;
//		double stackBracket= 300;
////		double  Ixx= IxxCal.calculateRequiredIxx(typeOfGlazing,unsupportedLength,gridLength,windPressure,stackBracket);
////		System.out.println("Calculated,Ixx ="+ Ixx);
////		System.out.println("Allowable deflection is "+ DeflectionCal.calculateDeflection(typeOfGlazing,unsupportedLength,gridLength,windPressure,stackBracket, Ixx));
////		System.out.println("Calculated,Deflection ="+ DeflectionCal.calculateDeflection(typeOfGlazing,unsupportedLength,gridLength,windPressure,stackBracket, 200));
//
//		double bendingMoment = BendingMomentCal.calculateBendingMoment(typeOfGlazing, unsupportedLength, gridLength, windPressure, stackBracket);
//
//		BigDecimal roundedMoment = new BigDecimal(bendingMoment).setScale(2, RoundingMode.HALF_UP);
//
//		System.out.println("Bending Moment is = "+roundedMoment);
////		RegularRectangular regularRectangular = new RegularRectangular();
////		regularRectangular.calculateOptimalGeometry(5, 200);

		System.out.println("Your Application started successfully.");

	}

}
