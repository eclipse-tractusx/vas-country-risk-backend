package com.catenax.valueaddedservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;



public class MethodUtils {

    private static final Logger log = ESAPI.getLogger(MethodUtils.class);

    @Autowired
    ObjectMapper objectMapper;

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
