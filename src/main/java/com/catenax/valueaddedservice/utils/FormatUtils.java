package com.catenax.valueaddedservice.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormatUtils {

    public static Float formatFloatTwoDecimals(Float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sym);
        return Float.valueOf(df.format(number));
    }

    public static String removeDecimalsZero(Float number) {
        if (number == number.longValue()) {
            return new DecimalFormat("#").format(number);
        }
        return String.valueOf(number);
    }
}
