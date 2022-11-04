package com.catenax.valueaddedservice.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


public class MethodUtils {


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

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
