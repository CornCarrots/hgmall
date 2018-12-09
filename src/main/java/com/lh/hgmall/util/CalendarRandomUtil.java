package com.lh.hgmall.util;

import java.util.Calendar;

public class CalendarRandomUtil {
    public static String getRandom(){
        Calendar calCurrent = Calendar.getInstance();
        int intDay = calCurrent.get(Calendar.DATE);
        int intMonth = calCurrent.get(Calendar.MONTH) + 1;
        int intYear = calCurrent.get(Calendar.YEAR);
        String now = String.valueOf(intYear) + "" + String.valueOf(intMonth) + "" +
                String.valueOf(intDay) + "";
        now +=System.currentTimeMillis();
        return now;
    }
}
