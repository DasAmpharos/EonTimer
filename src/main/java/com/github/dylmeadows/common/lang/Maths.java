package com.github.dylmeadows.common.lang;

public class Maths {

    private Maths() {
    }

    public static int max(int... values) {
        int i = 0;
        int max = values[i];
        while (++i < values.length)
            if (values[i] > max)
                max = values[i];
        return max;
    }

    public static double max(double... values) {
        int i = 0;
        double max = values[i];
        while (++i < values.length)
            if (values[i] > max)
                max = values[i];
        return max;
    }

    public static int min(int... values) {
        int i = 0;
        int min = values[i];
        while (++i < values.length)
            if (values[i] < min)
                min = values[i];
        return min;
    }

    public static double min(double... values) {
        int i = 0;
        double min = values[i];
        while (++i < values.length)
            if (values[i] < min)
                min = values[i];
        return min;
    }

    public static boolean isBound(int value, int min, int max) {
        return (min <= max) && (min <= value && value <= max);
    }

    public static boolean isByte(String s) {
        return parseNumber(s, Byte::parseByte) != null;
    }

    public static boolean isShort(String s) {
        return parseNumber(s, Short::parseShort) != null;
    }

    public static boolean isInteger(String s) {
        return parseNumber(s, Integer::parseInt) != null;
    }

    public static boolean isLong(String s) {
        return parseNumber(s, Long::parseLong) != null;
    }

    public static boolean isFloat(String s) {
        return parseNumber(s, Float::parseFloat) != null;
    }

    public static boolean isDouble(String s) {
        return parseNumber(s, Double::parseDouble) != null;
    }

    private static <T extends Number> T parseNumber(String s, Parser<T> parser) {
        try {
            return parser.parse(s);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }
}
