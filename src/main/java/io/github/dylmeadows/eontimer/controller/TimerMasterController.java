package io.github.dylmeadows.eontimer.controller;

import io.github.dylmeadows.eontimer.model.TimerModel;
import io.github.dylmeadows.eontimer.service.TimerService;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TimerMasterController {

    @FXML
    private Tab gen3Tab;
    @FXML
    private Tab gen4Tab;
    @FXML
    private Tab gen5Tab;
    @FXML
    private Tab customTab;
    @FXML
    private TabPane timerTabPane;

    private final TimerModel model;
    private final TimerFactory gen3TimerFactory;
    private final TimerFactory gen4TimerFactory;
    private final TimerFactory gen5TimerFactory;
    private final TimerFactory customTimerFactory;
    private final TimerService timerService;

    public void initialize() {
        /*JavaFxObservable.changesOf(timerTabPane.getSelectionModel().selectedItemProperty())
            .map(Change::getNewVal)
            .map(this::getTimerFactory)
            .map(TimerFactory::createTimer)
            .subscribe(model::setStages, error -> {
                log.error(error.getMessage(), error);
                System.exit(-1);
            });*/
        timerService.start();
    }

    private TimerFactory getTimerFactory(Tab tab) {
        if (tab.equals(gen3Tab)) {
            return gen3TimerFactory;
        } else if (tab.equals(gen4Tab)) {
            return gen4TimerFactory;
        } else if (tab.equals(gen5Tab)) {
            return gen5TimerFactory;
        } else if (tab.equals(customTab)) {
            return customTimerFactory;
        } else {
            throw new IllegalStateException("unable to find timer factory for selected tab");
        }
    }
}
