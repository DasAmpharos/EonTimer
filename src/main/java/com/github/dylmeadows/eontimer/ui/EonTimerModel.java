package com.github.dylmeadows.eontimer.ui;

import com.github.dylmeadows.common.javafx.Model;
import com.github.dylmeadows.eontimer.reference.Console;
import com.github.dylmeadows.eontimer.ui.settings.EonTimerSettingsModel;
import com.github.dylmeadows.eontimer.ui.settings.timer.TimerSettingsModel;
import com.github.dylmeadows.eontimer.ui.timers.custom.CustomTimerModel;
import com.github.dylmeadows.eontimer.ui.timers.gen3.Gen3TimerModel;
import com.github.dylmeadows.eontimer.ui.timers.gen4.Gen4TimerModel;
import com.github.dylmeadows.eontimer.ui.timers.gen5.Gen5TimerModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class EonTimerModel extends Model {

    private final ObjectProperty<Gen5TimerModel> gen5TimerModel;
    private final ObjectProperty<Gen4TimerModel> gen4TimerModel;
    private final ObjectProperty<Gen3TimerModel> gen3TimerModel;
    private final ObjectProperty<CustomTimerModel> customTimerModel;
    private final ObjectProperty<EonTimerSettingsModel> eonTimerSettingsModel;

    public EonTimerModel() {
        gen5TimerModel = new SimpleObjectProperty<>(new Gen5TimerModel());
        gen4TimerModel = new SimpleObjectProperty<>(new Gen4TimerModel());
        gen3TimerModel = new SimpleObjectProperty<>(new Gen3TimerModel());
        customTimerModel = new SimpleObjectProperty<>(new CustomTimerModel());
        eonTimerSettingsModel = new SimpleObjectProperty<>(new EonTimerSettingsModel());

        TimerSettingsModel timerSettings = getEonTimerSettingsModel().getTimerSettingsModel();
        // ===== consoleProperty =====
        ObjectProperty<Console> consoleProperty = timerSettings.consoleProperty();
        getGen5TimerModel().consoleProperty().bind(consoleProperty);
        getGen4TimerModel().consoleProperty().bind(consoleProperty);
        getGen3TimerModel().consoleProperty().bind(consoleProperty);
        getCustomTimerModel().consoleProperty().bind(consoleProperty);
        // ===== precisionCalibrationModeProperty =====
        BooleanProperty precisionCalibrationModeProperty = timerSettings.precisionCalibrationModeProperty();
        getGen5TimerModel().precisionCalibrationModeProperty().bind(precisionCalibrationModeProperty);
        getGen4TimerModel().precisionCalibrationModeProperty().bind(precisionCalibrationModeProperty);
        getGen3TimerModel().precisionCalibrationModeProperty().bind(precisionCalibrationModeProperty);
        getCustomTimerModel().precisionCalibrationModeProperty().bind(precisionCalibrationModeProperty);
        // ===== minimumLengthProperty =====
        IntegerProperty minimumLengthProperty = timerSettings.minimumLengthProperty();
        getGen5TimerModel().minimumLengthProperty().bind(minimumLengthProperty);
        getGen4TimerModel().minimumLengthProperty().bind(minimumLengthProperty);
        getGen3TimerModel().minimumLengthProperty().bind(minimumLengthProperty);
        getCustomTimerModel().minimumLengthProperty().bind(minimumLengthProperty);
    }

    public Gen5TimerModel getGen5TimerModel() {
        return gen5TimerModel.get();
    }

    public ObjectProperty<Gen5TimerModel> gen5TimerModelProperty() {
        return gen5TimerModel;
    }

    public void setGen5TimerModel(Gen5TimerModel gen5TimerModel) {
        this.gen5TimerModel.set(gen5TimerModel);
    }

    public Gen4TimerModel getGen4TimerModel() {
        return gen4TimerModel.get();
    }

    public ObjectProperty<Gen4TimerModel> gen4TimerModelProperty() {
        return gen4TimerModel;
    }

    public void setGen4TimerModel(Gen4TimerModel gen4TimerModel) {
        this.gen4TimerModel.set(gen4TimerModel);
    }

    public Gen3TimerModel getGen3TimerModel() {
        return gen3TimerModel.get();
    }

    public ObjectProperty<Gen3TimerModel> gen3TimerModelProperty() {
        return gen3TimerModel;
    }

    public void setGen3TimerModel(Gen3TimerModel gen3TimerModel) {
        this.gen3TimerModel.set(gen3TimerModel);
    }

    public CustomTimerModel getCustomTimerModel() {
        return customTimerModel.get();
    }

    public ObjectProperty<CustomTimerModel> customTimerModelProperty() {
        return customTimerModel;
    }

    public void setCustomTimerModel(CustomTimerModel customTimerModel) {
        this.customTimerModel.set(customTimerModel);
    }

    public EonTimerSettingsModel getEonTimerSettingsModel() {
        return eonTimerSettingsModel.get();
    }

    public ObjectProperty<EonTimerSettingsModel> eonTimerSettingsModelProperty() {
        return eonTimerSettingsModel;
    }

    public void setEonTimerSettingsModel(EonTimerSettingsModel eonTimerSettingsModel) {
        this.eonTimerSettingsModel.set(eonTimerSettingsModel);
    }
}
