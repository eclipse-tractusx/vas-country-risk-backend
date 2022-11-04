package com.catenax.valueaddedservice.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodUtilsTest {

    @Test
    @DisplayName("Should return the number with decimals when the number is not an integer")
    void removeDecimalsZeroWhenNumberIsNotIntegerThenReturnNumberWithDecimals() {
        Float number = 1.23f;
        String result = MethodUtils.removeDecimalsZero(number);
        assertEquals("1.23", result);
    }

    @Test
    @DisplayName("Should return the number without decimals when the number is an integer")
    void removeDecimalsZeroWhenNumberIsIntegerThenReturnNumberWithoutDecimals() {
        Float number = 1.0f;

        String result = MethodUtils.removeDecimalsZero(number);

        assertEquals("1", result);
    }
}