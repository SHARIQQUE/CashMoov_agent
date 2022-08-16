package com.aldoapps.autoformatedittext;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by aldo on 21/08/16.
 */
public class AutoFormatUtil {

    public static final String FORMAT_NO_DECIMAL = "###,###";

    public static final String FORMAT_WITH_DECIMAL = "###,###.##";

    public static  DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);

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
        return getCharOccurance(input, ',');
    }

    public static String extractDigits(String input) {
        return input.replaceAll("\\D+", "");
    }

    public static String formatToStringWithoutDecimal(double value) {
        NumberFormat formatter = new DecimalFormat(FORMAT_NO_DECIMAL,symbols);
        return formatter.format(value);
    }

    public static String formatToStringWithoutDecimal(String value) {
        return formatToStringWithoutDecimal(Double.parseDouble(value));
    }

    public static String formatWithDecimal(String price) {
        return formatWithDecimal(Double.parseDouble(price));
    }

    public static String formatWithDecimal(double price) {
        NumberFormat formatter = new DecimalFormat(FORMAT_WITH_DECIMAL,symbols);
        return formatter.format(price);
    }
}
