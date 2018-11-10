package com.github.dylmeadows.eontimer.util;

import javafx.concurrent.Task;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class FxConcurrency {
    public Task<Void> asTask(Runnable runnable) {
        return new Task<Void>() {
            @Override
            protected Void call() {
                runnable.run();
                return null;
            }
        };
    }
}
