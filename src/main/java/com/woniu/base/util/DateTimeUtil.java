package com.woniu.base.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public final class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static String format(DateTime dt, String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(dt);
    }

    public static DateTime parse(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.parseDateTime(date);
    }

    public static String formatDate(DateTime dt) {
        return format(dt, "yyyy-MM-dd");
    }

    public static DateTime parseDate(String date) {
        return parse(date, "yyyy-MM-dd");
    }

    public static String formatCompactDate(DateTime dt) {
        return format(dt, "yyyyMMdd");
    }

    public static DateTime parseCompactDate(String date) {
        return parse(date, "yyyyMMdd");
    }

    public static String formatTime(DateTime dt) {
        return format(dt, "HH:mm:ss");
    }

    public static String formatCompactTime(DateTime dt) {
        return format(dt, "HHmmss");
    }

    public static String formatDateTime(DateTime dt) {
        return format(dt, "yyyy-MM-dd HH:mm:ss");
    }

    public static DateTime parseDateTime(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatCompactDateTime(DateTime dt) {
        return format(dt, "yyyyMMddHHmmss");
    }

    public static DateTime parseCompactDateTime(String date) {
        return parse(date, "yyyyMMddHHmmss");
    }

    public static String formatHumanReadable(Date date) {
        if (date == null) {
            return "";
        }

        return formatHumanReadable(new DateTime(date));
    }

    public static String formatHumanReadable(DateTime date) {
        if (date == null) {
            return "";
        }

        long currentDate = System.currentTimeMillis();
        long createDate = date.getMillis();
        long sub = currentDate - createDate;
        String val = "刚刚";
        sub = sub / 1000;
        if (sub > 0) {
            if (sub < 60) {
                val = sub + "秒前";
            } else if (sub >= 60 && sub < 60 * 60) {
                val = sub / 60 + "分钟前";
            } else if (sub >= 60 * 60 && sub < 60 * 60 * 24) {
                val = (sub / (60 * 60)) + "小时前";
            } else if (sub >= 60 * 60 * 24 && sub < 60 * 60 * 24 * 365) {
                val = (sub / (60 * 60 * 24)) + "天前";
            } else {
                val = "1年前";
            }
        }

        return val;
    }
}
