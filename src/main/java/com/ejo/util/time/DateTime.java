package com.ejo.util.time;

import java.util.Calendar;

/**
 * The DateTime class is an easy-to-use way to store a date and time. The date may be created and formatted.
 * It uses the java Calendar class to store its data. To modify data within this DateTime, it must be done by using
 * the Calendar.
 */
public class DateTime {

    public static final DateTime NULL_TIME = new DateTime(0, 0, 0);

    private final Calendar calendar;

    public DateTime(int year, int month, int day, int hour, int min, int sec) {
        calendar = (Calendar) Calendar.getInstance().clone();
        calendar.set(year, month - 1, day, hour, min, sec);
    }

    public DateTime(int year, int month, int day, int hour, int min) {
        this(year, month, day, hour, min, 0);
    }

    public DateTime(int year, int month, int day, int hour) {
        this(year, month, day, hour, 0, 0);
    }

    public DateTime(int year, int month, int day) {
        this(year, month, day, 0, 0, 0);
    }


    public DateTime getAdded(int seconds) {
        return new DateTime(getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond() + seconds);
    }

    public long getDateTimeID() {
        try {
            return Long.parseLong(getYearString() + getMonthString() + getDayString() + getHourString() + getMinuteString() + getSecondString());
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public boolean isWeekend() {
        int dayOfWeek = getCalendar().get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    // =================================================

    // NUMERICAL GETTERS

    // =================================================

    public int getYear() {
        return Integer.parseInt(getYearString());
    }

    public int getMonth() {
        return Integer.parseInt(getMonthString());
    }

    public int getDay() {
        return Integer.parseInt(getDayString());
    }

    public int getHour() {
        return Integer.parseInt(getHourString());
    }

    public int getMinute() {
        return Integer.parseInt(getMinuteString());
    }

    public int getSecond() {
        return Integer.parseInt(getSecondString());
    }

    // =================================================

    // STRING GETTERS

    // =================================================

    public String getYearString() {
        String timeString = getCalendar().getTime().toString();
        return timeString.substring(timeString.length() - 4);
    }

    public String getMonthString() {
        String month = getCalendar().getTime().toString().substring(4, 19).substring(0, 3);
        //This is a switch statement for a reason, because the java calendar bugs and ends up
        // disagreeing on the numerical month sometimes from the string? weird right!
        return switch (month) {
            case "Jan" -> "01";
            case "Feb" -> "02";
            case "Mar" -> "03";
            case "Apr" -> "04";
            case "May" -> "05";
            case "Jun" -> "06";
            case "Jul" -> "07";
            case "Aug" -> "08";
            case "Sep" -> "09";
            case "Oct" -> "10";
            case "Nov" -> "11";
            case "Dec" -> "12";
            default -> null;
        };
    }

    private String getDayString() {
        return getCalendar().getTime().toString().substring(4, 19).substring(4, 6);
    }

    private String getHourString() {
        return getCalendar().getTime().toString().substring(4, 19).substring(7, 9);
    }

    private String getMinuteString() {
        return getCalendar().getTime().toString().substring(4, 19).substring(10, 12);
    }

    private String getSecondString() {
        return getCalendar().getTime().toString().substring(4, 19).substring(13, 15);
    }


    public Calendar getCalendar() {
        return calendar;
    }

    // =================================================

    // OBJECT METHODS

    // =================================================

    @Override
    public String toString() {
        return getYearString() + "-" + getMonthString() + "-" + getDayString() + " " + getHourString() + ":" + getMinuteString() + ":" + getSecondString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DateTime dateTime)) return false;
        return dateTime.getYearString().equals(getYearString())
                && dateTime.getMonthString().equals(getMonthString())
                && dateTime.getDayString().equals(getDayString())
                && dateTime.getHourString().equals(getHourString())
                && dateTime.getMinuteString().equals(getMinuteString())
                && dateTime.getSecondString().equals(getSecondString());
    }

    @Override
    protected DateTime clone() {
        return new DateTime(getYear(),getMonth(),getDay(),getHour(),getMinute(),getSecond());
    }

    // =================================================

    // STATIC METHODS

    // =================================================

    //DateTimeIDs should be used when ordering dates
    //They are presented as this: YYYYMMDDHHMMSS
    // Example: 20011127052500 (November 27, 2001 @ 5:25am)
    public static DateTime getById(long id) {
        String idString = String.valueOf(id);
        int year = Integer.parseInt(idString.substring(0, 4));
        int month = Integer.parseInt(idString.substring(4, 6));
        int day = Integer.parseInt(idString.substring(6, 8));
        int hour = Integer.parseInt(idString.substring(8, 10));
        int min = Integer.parseInt(idString.substring(10, 12));
        int sec = Integer.parseInt(idString.substring(12, 14));
        return new DateTime(year, month, day, hour, min, sec);
    }

    //The calendar returns this formatted string for a date
    // This function will convert that formatted string into a DateTime
    public static DateTime getByFormattedString(String formattedDateTime) {
        String[] split = formattedDateTime.split(" ");
        int year = Integer.parseInt(split[0].split("-")[0]);
        int month = Integer.parseInt(split[0].split("-")[1]);
        int day = Integer.parseInt(split[0].split("-")[2]);
        int hour = Integer.parseInt(split[1].split(":")[0]);
        int min = Integer.parseInt(split[1].split(":")[1]);
        int sec = Integer.parseInt(split[1].split(":")[2]);
        return new DateTime(year, month, day, hour, min, sec);
    }

}