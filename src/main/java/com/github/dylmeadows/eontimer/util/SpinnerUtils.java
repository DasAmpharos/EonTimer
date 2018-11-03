package com.github.dylmeadows.eontimer.util;

import javafx.scene.control.SpinnerValueFactory;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class SpinnerUtils {

    public SpinnerValueFactory<Integer> createValueFactory(int min, int max) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
    }

    public SpinnerValueFactory<Integer> createValueFactory(int min, int max, int initialValue) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue);
    }

    public SpinnerValueFactory<Integer> createValueFactory(int min, int max, int initialValue, int amountToStepBy) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue, amountToStepBy);
    }
}
