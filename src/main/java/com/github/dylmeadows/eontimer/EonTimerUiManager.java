package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.eontimer.config.ApplicationProperties;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.spring.application.FxUiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EonTimerUiManager extends FxUiManager {

    private final ApplicationProperties properties;

    @Autowired
    public EonTimerUiManager(
            final EasyFxml easyFxml,
            final ApplicationProperties properties) {
        super(easyFxml);
        this.properties = properties;
    }

    @Override
    protected String title() {
        return String.format("%s v%s", properties.getName(), properties.getVersion());
    }

    @Override
    protected FxmlNode mainComponent() {
        return EonTimerComponents.TIMER_DISPLAY;
    }
}
