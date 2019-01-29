package io.github.dylmeadows.eontimer.model.config;

import io.github.dylmeadows.eontimer.model.resource.SoundResource;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ActionConfigurationModel {

    private final ObjectProperty<ActionMode> mode = new SimpleObjectProperty<>(ActionConfigurationConstants.DEFAULT_MODE);
    private final ObjectProperty<Color> color = new SimpleObjectProperty<>(ActionConfigurationConstants.DEFAULT_COLOR);
    private final ObjectProperty<SoundResource> sound = new SimpleObjectProperty<>(ActionConfigurationConstants.DEFAULT_SOUND);
    private final IntegerProperty interval = new SimpleIntegerProperty(ActionConfigurationConstants.DEFAULT_INTERVAL);
    private final IntegerProperty count = new SimpleIntegerProperty(ActionConfigurationConstants.DEFAULT_COUNT);

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
