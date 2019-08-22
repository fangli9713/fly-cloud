package com.fly.common.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtil {


    /**
     * 日期格式yyyy-MM-dd
     */
    public static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     */
    public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获得当前的日期毫秒
     */
    public static long nowTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获得当前的时间戳
     */
    public static Timestamp nowTimeStamp() {
        return new Timestamp(nowTimeMillis());
    }

    /**
     * 把String 日期转换成long型日期(秒)；
     *
     * @param date   String 型日期；
     * @param format 日期格式；
     * @return long
     */
    public static long stringToLong(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = sdf.parse(date);
        return d.getTime() / 1000;

    }

    /**
     * 传入时间字符串及时间格式，返回对应的Date对象
     *
     * @param date   时间字符串
     * @param format 时间格式
     * @return Date
     */
    public static Date stringToDate(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(date);
    }


    /**
     * date转指定格式string
     *
     * @param date
     * @param format
     * @return String
     */
    public static String dateToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if (date == null) {
            return null;
        }
        return simpleDateFormat.format(date);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
        if (date == null) {
            return null;
        }
        return simpleDateFormat.format(date);
    }

    /**
     * 时间戳数型日期转转指定格式String
     *
     * @param date
     * @param format
     * @return String
     */
    public static String longToString(long date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = new Date(date * 1000L);
        return sdf.format(d);
    }

    public static long getAppointTimes(int hours) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (long) (cal.getTimeInMillis() / 1000);
    }

    public static boolean currentDate(String date, String pattern) {
        boolean flag = false;
        long zero = getAppointTimes(0);
        long twentyFour = getAppointTimes(24);
        long now = 0;
        try {
            now = stringToLong(date, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (now >= zero && now < twentyFour) {
            flag = true;
        }
        return flag;

    }

    /**
     * Date转LocalDateTime
     *
     * @param date Date对象
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 格式化时间-默认yyyy-MM-dd HH:mm:ss格式
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, DATE_TIME_PATTERN);
    }

    /**
     * 按pattern格式化时间-默认yyyy-MM-dd HH:mm:ss格式
     *
     * @param dateTime LocalDateTime对象
     * @param pattern  要格式化的字符串
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = DATE_TIME_PATTERN;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }


    /**
     * long转 LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * String 转 LocalDateTime
     *
     * @param time
     * @return
     */

    public static LocalDateTime parseStringToDateTime(String time) {
        return parseStringToDateTime(time, DateUtil.DATE_TIME_PATTERN);
    }

    /**
     * String 转 LocalDateTime
     *
     * @param time
     * @return
     */

    public static LocalDateTime parseStringToDateTime(String time, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(time, df);
    }


}
