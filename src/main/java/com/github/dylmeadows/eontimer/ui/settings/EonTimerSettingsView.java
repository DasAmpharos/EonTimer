package com.github.dylmeadows.eontimer.ui.settings;

import com.github.dylmeadows.eontimer.ui.settings.action.ActionSettingsView;
import com.github.dylmeadows.eontimer.ui.settings.theme.ThemeSettingsView;
import com.github.dylmeadows.eontimer.ui.settings.timer.TimerSettingsView;
import com.github.dylmeadows.common.util.ResourceBundles;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.ResourceBundle;

public class EonTimerSettingsView extends DialogPane {

    public EonTimerSettingsView() {
        initComponents();
    }

    ActionSettingsView getActionSettingsView() {
        return actionSettingsView;
    }

    TimerSettingsView getTimerSettingsView() {
        return timerSettingsView;
    }

    ThemeSettingsView getThemeSettingsView() {
        return themeSettingsView;
    }

    private void initComponents() {
        ResourceBundle bundle = ResourceBundles.getBundle(EonTimerSettingsView.class);
        tabPane = new TabPane();
        actionTab = new Tab();
        actionSettingsView = new ActionSettingsView();
        timerTab = new Tab();
        timerSettingsView = new TimerSettingsView();
        themeTab = new Tab();
        themeSettingsView = new ThemeSettingsView();

        // ===== this =====
        getButtonTypes().setAll(ButtonType.OK);

        // ===== tabPane =====
        {
            tabPane.setPadding(new Insets(10));
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            tabPane.getStyleClass().add("floating");

            // ===== actionTab =====
            actionTab.setText(bundle.getString("EonTimerSettingsView.actionTab.text"));
            actionTab.setClosable(false);
            actionTab.setContent(actionSettingsView);
            tabPane.getTabs().add(actionTab);

            // ===== timerTab =====
            timerTab.setText(bundle.getString("EonTimerSettingsView.timerTab.text"));
            timerTab.setClosable(false);
            timerTab.setContent(timerSettingsView);
            tabPane.getTabs().add(timerTab);

            // ===== themeTab =====
            themeTab.setText(bundle.getString("EonTimerSettingsView.themeTab.text"));
            themeTab.setClosable(false);
            themeTab.setContent(themeSettingsView);
            tabPane.getTabs().add(themeTab);
        }
        setContent(tabPane);
    }

    // region // EonTimerSettingsView - Variables declaration

    private TabPane tabPane;
    private Tab actionTab;
    private ActionSettingsView actionSettingsView;
    private Tab timerTab;
    private TimerSettingsView timerSettingsView;
    private Tab themeTab;
    private ThemeSettingsView themeSettingsView;

    // endregion
}
