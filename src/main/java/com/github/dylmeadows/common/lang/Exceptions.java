package com.github.dylmeadows.common.lang;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Exceptions {

    private Exceptions() {
    }

    public static String getStackTraceString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
