package com.zebrait.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public final class TransformUtil {
    //private static final String dateFormat = "YYYY-MM-dd HH:mm:ss.SSS";
    private static final String dateFormat = "YYYY-MM-dd";
    private final static DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat).withZoneUTC();

    public static long toNumberWithoutChinese(String str) {
        long base = 1;

        if (str.endsWith("亿")) {
            str = str.replace("亿", "");
            base *= 100000000;
        }
        if (str.endsWith("万")) {
            str = str.replace("万", "");
            base *= 10000;
        }
        return (long) (base * Float.parseFloat(str));
    }

    public static Date toDate(String str) {
        return formatter.parseDateTime(str).toDate();
    }
}
