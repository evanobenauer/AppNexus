package com.ejo.util.time;

import java.time.LocalDateTime;

public class TimeUtil {

    public static DateTime getCurrentDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new DateTime(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());
    }

    public static double getDateTimePercent(DateTime current, DateTime start, DateTime end) {
        return (double) getSecondDifference(current, start) / (getSecondDifference(end,start));
    }

    public static long getSecondDifference(DateTime timeFinal, DateTime timeInitial) {
        return (timeFinal.getCalendar().getTimeInMillis() / 1000) - (timeInitial.getCalendar().getTimeInMillis() / 1000);
    }

}
