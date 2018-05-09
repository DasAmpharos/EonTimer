package com.github.dylmeadows.eontimer.ui.settings.action;

import com.github.dylmeadows.eontimer.reference.ActionMode;
import com.github.dylmeadows.eontimer.reference.settings.ActionSettingsConstants;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;

public class ActionSettingsViewTest extends ApplicationTest implements ActionSettingsConstants {

    private ActionSettingsView view;

    @Override
    public void start(Stage stage) {
        view = new ActionSettingsView();
        view.setId("view");
        stage.setScene(new Scene(view));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setup() {
        setMode(DEFAULT_MODE);
    }

    @After
    public void cleanup() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void test_fieldDisable_setter() {
        setMode(ActionMode.NONE);
        verifyThat("#soundField", isDisabled());
        verifyThat("#colorField", isDisabled());

        setMode(ActionMode.AUDIO);
        verifyThat("#soundField", isEnabled());
        verifyThat("#colorField", isDisabled());

        setMode(ActionMode.VISUAL);
        verifyThat("#soundField", isDisabled());
        verifyThat("#colorField", isEnabled());

        setMode(ActionMode.AV);
        verifyThat("#soundField", isEnabled());
        verifyThat("#colorField", isEnabled());
    }

    private void setMode(ActionMode mode) {
        Platform.runLater(() -> view.setMode(mode));
        WaitForAsyncUtils.waitForFxEvents();
    }

    private ActionMode getMode() {
        return view.getMode();
    }
}
