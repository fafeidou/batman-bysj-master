package com.batman.bysj.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by dell on 2016/8/30.
 */
public final class ConvertUtils {
    public static long toLong(Object value) {
        if (value == null) return 0;
        if (StringUtils.isEmpty(value.toString())) return 0;
        return Long.parseLong(value.toString());
    }

    public static int toInt(Object value) {
        if (value == null) return 0;
        if (StringUtils.isEmpty(value.toString())) return 0;
        return Integer.parseInt(value.toString());
    }

    public static boolean toBoolean(Object value) {
        if (value == null) return false;
        if (StringUtils.isEmpty(value.toString())) return false;
        return Boolean.parseBoolean(value.toString());
    }

    public static String toString(Object value) {
        if (value == null) return "";
        return value.toString();
    }
}
