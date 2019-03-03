package io.github.dylmeadows.eontimer.util;

import javafx.scene.control.SpinnerValueFactory;

public final class Spinners {

    public static SpinnerValueFactory<Integer> createValueFactory(int min, int max) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
    }

    public static SpinnerValueFactory<Integer> createValueFactory(int min, int max, int initialValue) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue);
    }

    public static SpinnerValueFactory<Integer> createValueFactory(int min, int max, int initialValue, int amountToStepBy) {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue, amountToStepBy);
    }

    public static SpinnerValueFactory<Double> createValueFactory(double min, double max) {
        return new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max);
    }

    public static SpinnerValueFactory<Double> createValueFactory(double min, double max, double initialValue) {
        return new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue);
    }

    public static SpinnerValueFactory<Double> createValueFactory(double min, double max, double initialValue, double amountToStepBy) {
        return new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue, amountToStepBy);
    }
}
