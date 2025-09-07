package com.fss.core.fssCalculation.service.utility;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Component
public class Utility {

    public static String formatAmount(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("en", "IN"));
        DecimalFormat formatter = new DecimalFormat("##,##,###.##", symbols);
        return formatter.format(amount);
    }

    public static double roundTo2Decimal(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public  void populateManualCalculatedValues(HttpSession session)
    {
         double iyy = (session.getAttribute("iyy") instanceof Double)
                 ? (Double) session.getAttribute("iyy") : 0.0;
         double bxx= (session.getAttribute("boundingboxx") instanceof Double)
                 ? (Double) session.getAttribute("boundingboxx") : 0.0;
         if(bxx>=1.0) {
             session.setAttribute("zyy", iyy/bxx);
         }

         double deflection = (session.getAttribute("df") instanceof Double)
                 ? (Double) session.getAttribute("df") : 0.0;

         session.setAttribute("allowableDefLesser",deflection<=19.0?deflection:19.0);

         double gt= (session.getAttribute("glassThickness") instanceof Double)
                 ? (Double) session.getAttribute("glassThickness") : 0.0;

         double dlintensity = ((gt*25.0)*110.0)/100000.0; //110%

        session.setAttribute("dlintensity", dlintensity);


        double bendingMoment= (session.getAttribute("bm") instanceof Double)
                ? (Double) session.getAttribute("bm") : 0.0;
        double zxx = (session.getAttribute("zxx_mullion") instanceof Double)
                ? (Double) session.getAttribute("zxx_mullion") : 0.0;

        session.setAttribute("fx", bendingMoment/zxx);

        double fsx_mullion=(session.getAttribute("fsx_mullion") instanceof Double)
                ? (Double) session.getAttribute("fsx_mullion") : 0.0;

        session.setAttribute("combined_shearEq_Mullion", Utility.roundTo2Decimal(fsx_mullion/41.0));


    }
}
