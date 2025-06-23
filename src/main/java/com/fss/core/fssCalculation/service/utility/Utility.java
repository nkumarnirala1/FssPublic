package com.fss.core.fssCalculation.service.utility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utility {

    public static String formatAmount(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("en", "IN"));
        DecimalFormat formatter = new DecimalFormat("##,##,###.##", symbols);
        return formatter.format(amount);
    }
}
