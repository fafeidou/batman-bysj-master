package com.batman.bysj.common.util;

import java.util.Date;
import java.util.TimeZone;

public final class DateTimeUtilBeijing {

    private static final TimeZone BEIJING_ZONE = TimeZone.getTimeZone("Asia/Shanghai");

    //获取当前北京日期
    public static Date getCurrentBeiJingDate() {
        return toBeiJingDate(new Date());
    }

    //当前时区时间To北京时间
    public static Date toBeiJingDate(Date localDate) {
        return DateTimeUtils.changeTimeZone(localDate, TimeZone.getDefault(), BEIJING_ZONE);
    }

    //北京时间To本地时区时间
    public static Date toLocalDate(Date beiJingDate) {
        return DateTimeUtils.changeTimeZone(beiJingDate, BEIJING_ZONE, TimeZone.getDefault());
    }

    //北京时间To本地时区时间戳
    public static long toLocalTime(Date beiJingDate) {
        return toLocalDate(beiJingDate).getTime();
    }
}
