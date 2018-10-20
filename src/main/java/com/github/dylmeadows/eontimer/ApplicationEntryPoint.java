package com.github.dylmeadows.eontimer;

import javafx.stage.Stage;

public interface ApplicationEntryPoint {

    void init() throws Exception;

    void start(Stage stage) throws Exception;

    void stop() throws Exception;
}
