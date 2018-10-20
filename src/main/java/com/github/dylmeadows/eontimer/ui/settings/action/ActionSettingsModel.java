package com.github.dylmeadows.eontimer.ui.settings.action;

import com.github.dylmeadows.eontimer.handlers.ActionHandler;
import com.github.dylmeadows.eontimer.model.ActionMode;
import com.github.dylmeadows.eontimer.model.resources.SoundResource;
import com.github.dylmeadows.eontimer.reference.settings.ActionSettingsConstants;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

/**
 * Settings related to timer action specific configuration. Binds to
 * {@link ActionSettingsView}.
 */
public class ActionSettingsModel implements ActionSettingsConstants {

    /**
     * See {@link com.github.dylmeadows.eontimer.ui.EonTimerController#onActionModeChange(ObservableValue, ActionMode, ActionMode)}
     * Binds to {@link ActionSettingsView#modeField}.
     */
    private final ObjectProperty<ActionMode> mode;

    /**
     * See {@link com.github.dylmeadows.eontimer.handlers.actions.VisualAction#activeFill}.
     * Binds to {@link ActionSettingsView#colorField}.
     */
    private final ObjectProperty<Color> color;

    /**
     * See {@link com.github.dylmeadows.eontimer.handlers.actions.SoundAction#sound}.
     * Binds to {@link ActionSettingsView#soundField}.
     */
    private final ObjectProperty<SoundResource> sound;

    /**
     * See {@link ActionHandler#interval}.
     * Binds to {@link ActionSettingsView#intervalField}.
     */
    private final IntegerProperty interval;

    /**
     * See {@link ActionHandler#count}.
     * Binds to {@link ActionSettingsView#countField}.
     */
    private final IntegerProperty count;

    /**
     * Initializes all properties to their default values.
     */
    public ActionSettingsModel() {
        mode = new SimpleObjectProperty<>(DEFAULT_MODE);
        color = new SimpleObjectProperty<>(DEFAULT_COLOR);
        sound = new SimpleObjectProperty<>(DEFAULT_SOUND);
        interval = new SimpleIntegerProperty(DEFAULT_INTERVAL);
        count = new SimpleIntegerProperty(DEFAULT_COUNT);
    }

    /**
     * @return see {@link #mode}
     */
    public ActionMode getMode() {
        return mode.get();
    }

    /**
     * @return see {@link #mode}
     */
    public ObjectProperty<ActionMode> modeProperty() {
        return mode;
    }

    /**
     * @param mode see {@link #mode}
     */
    public void setMode(ActionMode mode) {
        this.mode.set(mode);
    }

    /**
     * @return see {@link #color}
     */
    public Color getColor() {
        return color.get();
    }

    /**
     * @return see {@link #color}
     */
    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    /**
     * @param color see {@link #color}
     */
    public void setColor(Color color) {
        this.color.set(color);
    }

    /**
     * @return see {@link #sound}
     */
    public SoundResource getSound() {
        return sound.get();
    }

    /**
     * @return see {@link #sound}
     */
    public ObjectProperty<SoundResource> soundProperty() {
        return sound;
    }

    /**
     * @param sound see {@link #sound}
     */
    public void setSound(SoundResource sound) {
        this.sound.set(sound);
    }

    /**
     * @return see {@link #interval}
     */
    public int getInterval() {
        return interval.get();
    }

    /**
     * @return see {@link #interval}
     */
    public IntegerProperty intervalProperty() {
        return interval;
    }

    /**
     * @param interval see {@link #interval}
     */
    public void setInterval(int interval) {
        this.interval.set(interval);
    }

    /**
     * @return see {@link #count}
     */
    public int getCount() {
        return count.get();
    }

    /**
     * @return see {@link #count}
     */
    public IntegerProperty countProperty() {
        return count;
    }

    /**
     * @param count see {@link #count}
     */
    public void setCount(int count) {
        this.count.set(count);
    }
}
