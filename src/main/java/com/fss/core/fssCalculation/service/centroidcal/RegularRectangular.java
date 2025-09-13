package com.fss.core.fssCalculation.service.centroidcal;

public class RegularRectangular {


   public double calculateIxx(double outer_Width, double outer_Height, double inner_Width, double inner_Height) {
        double bo = outer_Width;
        double do_ = outer_Height;

        // Inner dimensions (bi, di)
        double bi = inner_Width;
        double di = inner_Height;

        // Centroid (for symmetric hollow rectangle)
//        double Xc = bo / 2.0;
//        double Yc = do_ / 2.0;

        // Moment of inertia calculations
        double ixx = (bo * Math.pow(do_, 3) - bi * Math.pow(di, 3)) / 12.0;
        //double Iyy = (do_ * Math.pow(bo, 3) - di * Math.pow(bi, 3)) / 12.0;

        //System.out.printf("Centroid (Xc, Yc): (%.2f, %.2f)%n", Xc, Yc);
        System.out.printf("Moment of Inertia Ixx: %.2f%n", ixx);

        return ixx;
    }


   public void calculateOptimalGeometry(double boundingBox, double reQIxx) {
        double outer_Height = boundingBox * 2;// assuming depth we have to take max

        //assuming thickness is fixed in depth and width
        //double thickness = 1 ; // assuming client wants min thickness to reduce costing,
        // if he requires min width then we have to keep it max side
        // so outer width <= outer_depth

        double outer_width = 1;
        double maxThickness = outer_width;
        double minThickness = 1;

        boolean optimalGemoetry = false;
        while (outer_width <= outer_Height) {
            for (double thickness = maxThickness; thickness <= maxThickness; thickness++) {

                double ixx = calculateIxx(outer_width, outer_Height, outer_width - thickness, outer_Height - thickness);

                if (ixx >= reQIxx) {
                    optimalGemoetry = true;
                    System.out.println("optimal depth, width and thickness is = " + outer_Height + ", " + outer_width + ", " + thickness);
                    return;
                }
            }


            outer_width++;
        }

        System.out.println("Optimal geometry is not possible for given geometry");


    }
}
