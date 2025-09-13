package com.fss.core.fssCalculation.service.elements.inertia;

import org.springframework.stereotype.Component;

@Component
public class ZxxCal {


    public double calculateZxx(double ixx,double boundingBox_y ) {

        return ixx / boundingBox_y;

    }

}
