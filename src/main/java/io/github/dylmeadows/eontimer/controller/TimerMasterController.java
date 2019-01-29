package io.github.dylmeadows.eontimer.controller;

import io.github.dylmeadows.eontimer.core.timer.TimerFactory;
import io.github.dylmeadows.eontimer.model.Timer;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.sources.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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

    private final Timer model;
    private final ApplicationContext context;

    private final Map<Tab, TimerFactory> timerFactoryMap = new HashMap<>();

    public void initialize() {
        timerFactoryMap.put(gen3Tab, context.getBean("gen3TimerFactory", TimerFactory.class));
        timerFactoryMap.put(gen4Tab, context.getBean("gen4TimerFactory", TimerFactory.class));
        timerFactoryMap.put(gen5Tab, context.getBean("gen5TimerFactory", TimerFactory.class));
        timerFactoryMap.put(customTab, context.getBean("customTimerFactory", TimerFactory.class));

        JavaFxObservable.changesOf(timerTabPane.getSelectionModel().selectedItemProperty())
            .map(Change::getNewVal)
            .filter(timerFactoryMap::containsKey)
            .switchIfEmpty(Observable.error(
                new IllegalStateException("selected tab does not have an associated TimerFactory")))
            .map(timerFactoryMap::get)
            .map(TimerFactory::createTimer)
            .subscribe(model::setStages);
    }

    enum TimerType {
        GEN3, GEN4, GEN5, CUSTOM
    }
}
