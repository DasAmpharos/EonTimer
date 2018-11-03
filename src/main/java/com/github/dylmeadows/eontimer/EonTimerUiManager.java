package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.eontimer.component.FxComponents;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.spring.application.FxUiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EonTimerUiManager extends FxUiManager {

    @Autowired
    public EonTimerUiManager(EasyFxml easyFxml) {
        super(easyFxml);
    }

    @Override
    protected String title() {
        return "EonTimerUiManager";
    }

    @Override
    protected FxmlNode mainComponent() {
        return FxComponents.GEN3_TIMER;
    }
}
