package com.fss.core.fssCalculation.service;

import org.springframework.stereotype.Component;

@Component
public class ZxxCal {


    public double calculateZxx(double ixx,double boundingBox_y ) {

        return ixx / boundingBox_y;

    }

}
