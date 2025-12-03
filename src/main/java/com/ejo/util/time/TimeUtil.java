package com.ejo.util.time;

import java.time.LocalDateTime;

public class TimeUtil {

    public static DateTime getCurrentDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new DateTime(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());
    }

    public static DateTime getCurrentDateTimeAdjusted(int secondAdjust) {
        return getCurrentDateTime().getAdded(secondAdjust);
    }

    public static double getDateTimePercent(DateTime start, DateTime current, DateTime end) {
        return (double) getSecondDifference(current, start) / (getSecondDifference(end,start));
    }

    public static long getSecondDifference(DateTime timeFinal, DateTime timeInitial) {
        return (timeFinal.getCalendar().getTimeInMillis() / 1000) - (timeInitial.getCalendar().getTimeInMillis() / 1000);
    }

}
