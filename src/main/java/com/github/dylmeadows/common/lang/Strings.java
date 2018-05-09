package com.github.dylmeadows.common.lang;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Strings {

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) return s;
        char[] c = s.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    public static String decapitalize(String s) {
        if (s == null || s.length() == 0) return s;
        char[] c = s.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public static String toCamelCase(String s) {
        String[] words = s.split("\\s+");
        return Arrays.stream(words).map(Strings::capitalize)
                .collect(Collectors.joining(" "));
    }
}
