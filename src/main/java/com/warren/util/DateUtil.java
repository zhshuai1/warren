package com.warren.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

public final class DateUtil {
    private static final String dateFormatFull = "YYYY-MM-dd'T'HH:mm:ss.SSS";
    private static final String dateFormat = "YYYY-MM-dd";
    private final static FastDateFormat formatter = FastDateFormat.getInstance(dateFormat);
    private final static FastDateFormat formatterFull = FastDateFormat.getInstance(dateFormatFull);

    public static String toYearMonthDay(Date date) {
        return formatter.format(date);
    }

    public static String format(Date date) {
        return formatterFull.format(date);
    }

    public static Date parseFromYearMonthDay(String yearMonthDay) {
        try {
            return formatter.parse(yearMonthDay);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseFromFull(String time) {
        try {
            return formatterFull.parse(time);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isEarlierThanYesterday(Date date) {
        String today = toYearMonthDay(new Date());
        return toYearMonthDay(date).compareTo(today) < 0;

    }

    public static long daysAgo(Date date) {
        return diffDays(new Date(), date);
    }

    public static long diffDays(Date date1, Date date2) {
        String thisDay = toYearMonthDay(date1);
        String thatDay = toYearMonthDay(date2);
        return (parseFromYearMonthDay(thisDay).getTime() - parseFromYearMonthDay(thatDay).getTime()) / 86400000l;
    }
}
