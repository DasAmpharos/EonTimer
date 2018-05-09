package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.eontimer.reference.Console;

public class CalibrationHelper {

    public static int convertToMillis(int delays, Console console) {
        return (int) Math.round(delays * console.getFrameRate());
    }

    public static int convertToDelays(int millis, Console console) {
        return (int) Math.round(millis / console.getFrameRate());
    }

    public static int createCalibration(int delay, int second, Console option) {
        int delayCalibration = delay - convertToDelays(second * 1000, option);
        return convertToMillis(delayCalibration, option);
    }
}
