package com.aution.dapp.server.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * @author hewensheng
 */
public class MyCalendarUtils {

    private static Calendar cal = Calendar.getInstance();

    public static Calendar getNextDayZeroTime(){

        cal.add(Calendar.DATE,1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);

        return cal;
    }
    public static Calendar getToDayZeroTime(){

        cal.add(Calendar.DATE,0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);

        return cal;
    }

    public static String getCornExpression(Date date){
        cal.setTime(date);
        String cronExpression = cal.get(Calendar.SECOND) + " " + cal.get(Calendar.MINUTE) + " " + cal.get(Calendar.HOUR_OF_DAY)
                + " " + cal.get(Calendar.DAY_OF_MONTH) + " " + (cal.get(Calendar.MONTH) + 1) + " ? " + cal.get(Calendar.YEAR);
        return cronExpression;
    }

}
