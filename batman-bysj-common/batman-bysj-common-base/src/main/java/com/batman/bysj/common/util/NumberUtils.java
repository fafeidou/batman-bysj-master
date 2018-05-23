package com.batman.bysj.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 原始代码是合并自 LongUtils 和 BigDecimalUtils
 */
public final class NumberUtils {

    public static long parseLong(Object value) {
        if (value == null) return 0;
        if (StringUtils.isEmpty(value.toString())) return 0;
        return Long.parseLong(value.toString());
    }

    public static String toString(Long value) {
        return toString(value, "");
    }

    public static String toString(Long value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return Long.toString(value);
    }

    public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale) {
        if (b2.doubleValue() == 0) {
            return BigDecimal.ZERO;
        }
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal getValue(Double value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value);
    }
}
