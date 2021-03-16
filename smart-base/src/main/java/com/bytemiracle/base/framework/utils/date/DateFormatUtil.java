package com.bytemiracle.base.framework.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 转换时间格式
 */
public class DateFormatUtil {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String dateConverString(Date date) {
        return sdf.format(date);
    }

    public static String dateConverString(Date date,String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.format(date);
    }

    public static Date stringConverDate(String date) {
        Date d = null;
        try {
            if (date.length() == 19) {
                //形如：2020-06-16 14:44:14
                d = sdf.parse(date);
            } else {
                //形如Tue Jun 16 21:44:29 GMT+08:00 2020
                d = new Date(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * @param timeMillis 时间戳
     * @return 00-00 00:00
     */
    public static String buildTimeInDay(long timeMillis) {
        if (timeMillis == 0L) {
            return "00-00 00:00";
        }
        String timeString = DateFormatUtil.dateConverString(new Date(timeMillis));
        String[] s = timeString.split(" ");
        String[] dates = s[0].split("-");
        String[] times = s[1].split(":");
        return dates[1] + "-" + dates[2] + " " + times[0] + ":" + times[1];
    }

    /**
     * @param timeMillis 时间戳
     * @return 2021-01-30 01:45:38
     */
    public static String buildDateTime(long timeMillis) {
        if (timeMillis == 0L) {
            return "0000-00-00 00:00:00";
        }
        return DateFormatUtil.dateConverString(new Date(timeMillis));
    }



    public static String milliToTime(long milliseconds){
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1));
        return hms;
    }



}
