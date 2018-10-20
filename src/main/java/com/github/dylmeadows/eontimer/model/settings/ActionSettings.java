package com.github.dylmeadows.eontimer.model.settings;

import com.github.dylmeadows.eontimer.model.ActionMode;
import com.github.dylmeadows.eontimer.model.resources.SoundResource;
import com.github.dylmeadows.eontimer.reference.settings.ActionSettingsConstants;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class ActionSettings implements ActionSettingsConstants {

    private final ObjectProperty<ActionMode> mode;
    private final ObjectProperty<Color> color;
    private final ObjectProperty<SoundResource> sound;
    private final IntegerProperty interval;
    private final IntegerProperty count;

    public ActionSettings() {
        mode = new SimpleObjectProperty<>(DEFAULT_MODE);
        color = new SimpleObjectProperty<>(DEFAULT_COLOR);
        sound = new SimpleObjectProperty<>(DEFAULT_SOUND);
        interval = new SimpleIntegerProperty(DEFAULT_INTERVAL);
        count = new SimpleIntegerProperty(DEFAULT_COUNT);
    }

    public ActionMode getMode() {
        return mode.get();
    }

    public ObjectProperty<ActionMode> modeProperty() {
        return mode;
    }

    public void setMode(ActionMode mode) {
        this.mode.set(mode);
    }

    public Color getColor() {
        return color.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public SoundResource getSound() {
        return sound.get();
    }

    public ObjectProperty<SoundResource> soundProperty() {
        return sound;
    }

    public void setSound(SoundResource sound) {
        this.sound.set(sound);
    }

    public int getInterval() {
        return interval.get();
    }

    public IntegerProperty intervalProperty() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval.set(interval);
    }

    public int getCount() {
        return count.get();
    }

    public IntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }
}
