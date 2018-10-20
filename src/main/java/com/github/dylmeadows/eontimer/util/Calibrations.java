package com.github.dylmeadows.eontimer.util;

import com.github.dylmeadows.eontimer.model.Console;

public class Calibrations {

    public static int convertToMillis(int delays, Console console) {
        return (int) Math.round(delays * console.getFrameRate());
    }

    public static int convertToDelays(int millis, Console console) {
        return (int) Math.round(millis / console.getFrameRate());
    }

    public static int createCalibration(int delay, int second, Console console) {
        int delayCalibration = delay - convertToDelays(second * 1000, console);
        return convertToMillis(delayCalibration, console);
    }
}
