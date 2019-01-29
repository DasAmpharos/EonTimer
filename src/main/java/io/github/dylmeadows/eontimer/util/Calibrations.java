package io.github.dylmeadows.eontimer.util;

import io.github.dylmeadows.eontimer.model.config.Console;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Calibrations {

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
