package io.github.dylmeadows.eontimer.util;

import javafx.concurrent.Task;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FxConcurrency {

    public static Task<Void> asTask(Runnable runnable) {
        return new Task<Void>() {
            @Override
            protected Void call() {
                runnable.run();
                return null;
            }
        };
    }
}
