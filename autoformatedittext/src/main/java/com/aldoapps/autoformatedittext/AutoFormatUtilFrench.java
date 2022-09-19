package com.aldoapps.autoformatedittext;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by aldo on 21/08/16.
 */
public class AutoFormatUtilFrench {


    public static final String FORMAT_NO_DECIMAL_FRENCH = "###.###";


    public static final String FORMAT_WITH_DECIMAL_FRENCH = "###.###,##";
    public static final Locale l = new Locale("fr", "BE");
    public static  DecimalFormatSymbols symbols = new DecimalFormatSymbols(l);

    public static  DecimalFormatSymbols symbolsfrench = new DecimalFormatSymbols(Locale.FRENCH);



    public static DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(Locale.FRANCE);
    public static NumberFormat goodNumberFormat = new DecimalFormat("#,##0.00#", dfs);
    public static int getCharOccurance(String input, char c) {
        int occurrence = 0;
        char[] chars = input.toCharArray();
        for (char thisChar : chars) {
            if (thisChar == c) {
                occurrence++;
            }
        }
        return occurrence;
    }

    public static int getCommaOccurrence(String input) {
        return getCharOccurance(input, '.');
    }

    public static String extractDigits(String input) {
        return input.replaceAll("\\D+", "");
    }

    public static String formatToStringWithoutDecimal(double value) {

       // NumberFormat formatter = new DecimalFormat(FORMAT_NO_DECIMAL_FRENCH,symbols);

        DecimalFormatSymbols symbols1 = new DecimalFormatSymbols();
        symbols1.setDecimalSeparator(',');
        symbols1.setGroupingSeparator('.');
        NumberFormat goodNumberFormat = new DecimalFormat("#,##0.00#", symbols1);
        return goodNumberFormat.format(value);
    }

    public static String formatToStringWithoutDecimal(String value) {
        return formatToStringWithoutDecimal(Double.parseDouble(value));
    }

    public static String formatWithDecimal(String price) {
        return formatWithDecimal(Double.parseDouble(price));
    }

    public static String formatWithDecimal(double price) {
       // NumberFormat formatter = new DecimalFormat(FORMAT_WITH_DECIMAL_FRENCH,symbols);
        DecimalFormatSymbols symbols1 = new DecimalFormatSymbols();
        symbols1.setDecimalSeparator(',');
        symbols1.setGroupingSeparator('.');
        NumberFormat goodNumberFormat = new DecimalFormat("#,##0.00#", symbols1);

        return goodNumberFormat.format(price);
    }
}
