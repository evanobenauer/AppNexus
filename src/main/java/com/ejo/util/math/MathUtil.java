package com.ejo.util.math;

import java.util.ArrayList;

public class MathUtil {

    public static double roundDouble(double number, int sigFigs) {
        String num = String.format("%." + sigFigs + "f", number);
        return Double.parseDouble(num);
    }

    public static <T extends Number> double calculateAverage(ArrayList<T> values) {
        double avg = 0;
        for (T val : values) {
            avg += val.doubleValue();
        }
        avg /= values.size();
        return avg;
    }

    public static boolean isEven(int num) {
        return (num % 2 == 0);
    }

    public static boolean isNumberInteger(Number number) {
        try {
            return number.doubleValue() == (int) number;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isNumberLong(Number number) {
        try {
            return number.doubleValue() == (long) number;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isNumberShort(Number number) {
        try {
            return number.doubleValue() == (short) number;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isNumberFloat(Number number) {
        try {
            return number.doubleValue() == (float) number;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isNumberDouble(Number number) {
        try {
            return number.doubleValue() == (double) number;
        } catch (Exception exception) {
            return false;
        }
    }


}
