package com.github.dylmeadows.eontimer.controller;

import com.github.dylmeadows.eontimer.core.timer.TimerFactory;
import com.github.dylmeadows.eontimer.model.Timer;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.sources.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class TimerMasterController implements FxmlController {

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

    private final Timer model;

    private final Map<Tab, TimerType> tabTimerTypeMap = new HashMap<>();
    private final Map<TimerType, TimerFactory> timerFactoryMap = new HashMap<>();

    @Autowired
    public TimerMasterController(
        Timer model,
        @Qualifier("gen3TimerFactory") TimerFactory gen3TimerFactory,
        @Qualifier("gen4TimerFactory") TimerFactory gen4TimerFactory,
        @Qualifier("gen5TimerFactory") TimerFactory gen5TimerFactory,
        @Qualifier("customTimerFactory") TimerFactory customTimerFactory) {
        this.model = model;
        timerFactoryMap.put(TimerType.GEN3, gen3TimerFactory);
        timerFactoryMap.put(TimerType.GEN4, gen4TimerFactory);
        timerFactoryMap.put(TimerType.GEN5, gen5TimerFactory);
        timerFactoryMap.put(TimerType.CUSTOM, customTimerFactory);
    }

    @Override
    public void initialize() {
        tabTimerTypeMap.put(gen3Tab, TimerType.GEN3);
        tabTimerTypeMap.put(gen4Tab, TimerType.GEN4);
        tabTimerTypeMap.put(gen5Tab, TimerType.GEN5);
        tabTimerTypeMap.put(customTab, TimerType.CUSTOM);

        JavaFxObservable.changesOf(timerTabPane.getSelectionModel().selectedItemProperty())
            .map(Change::getNewVal)
            .filter(tabTimerTypeMap::containsKey)
            .switchIfEmpty(Observable.error(
                new IllegalStateException("selected tab does not have an associated TimerType")))
            .map(tabTimerTypeMap::get)
            .map(timerFactoryMap::get)
            .map(TimerFactory::createTimer)
            .doOnNext(model::setStages)
            .subscribe();
    }

    enum TimerType {
        GEN3, GEN4, GEN5, CUSTOM
    }
}
