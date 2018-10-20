package com.github.dylmeadows.eontimer.ui.settings.action;

import com.github.dylmeadows.eontimer.model.ActionMode;
import com.github.dylmeadows.eontimer.model.resources.SoundResource;
import com.github.dylmeadows.common.javafx.node.IntField;
import com.github.dylmeadows.eontimer.util.ResourceBundles;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.util.ResourceBundle;

public class ActionSettingsView extends GridPane {

    public ActionSettingsView() {
        initComponents();
    }

    public ActionMode getMode() {
        return modeField.getValue();
    }

    public ObjectProperty<ActionMode> modeProperty() {
        return modeField.valueProperty();
    }

    public void setMode(ActionMode mode) {
        modeField.setValue(mode);
    }

    public Color getColor() {
        return colorField.getValue();
    }

    public ObjectProperty<Color> colorProperty() {
        return colorField.valueProperty();
    }

    public void setColor(Color color) {
        colorField.setValue(color);
    }

    public SoundResource getSound() {
        return soundField.getValue();
    }

    public ObjectProperty<SoundResource> soundProperty() {
        return soundField.valueProperty();
    }

    public void setSound(SoundResource sound) {
        soundField.setValue(sound);
    }

    public int getInterval() {
        return intervalField.getValue();
    }

    public IntegerProperty intervalProperty() {
        return intervalField.valueProperty();
    }

    public void setInterval(int interval) {
        intervalField.setValue(interval);
    }

    public int getCount() {
        return countField.getValue();
    }

    public IntegerProperty countProperty() {
        return countField.valueProperty();
    }

    public void setCount(int count) {
        countField.setValue(count);
    }

    private void initComponents() {
        ResourceBundle bundle = ResourceBundles.getBundle(ActionSettingsView.class);
        modeLbl = new Label();
        modeField = new ChoiceBox<>();
        colorLbl = new Label();
        colorField = new ColorPicker();
        soundLbl = new Label();
        soundField = new ChoiceBox<>();
        intervalLbl = new Label();
        intervalField = new IntField();
        countLbl = new Label();
        countField = new IntField();

        // ===== this =====
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        // ===== ColumnConstraints =====
        {
            // ===== column0 =====
            ColumnConstraints column0 = new ColumnConstraints();
            column0.setHalignment(HPos.LEFT);
            column0.setHgrow(Priority.NEVER);
            getColumnConstraints().add(column0);

            // ===== column1 =====
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setHalignment(HPos.LEFT);
            column1.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(column1);
        }

        // ===== modeLbl =====
        modeLbl.setText(bundle.getString("ActionSettingsView.modeLbl.text"));
        modeLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(modeLbl, 0, 0);

        // ===== modeField =====
        modeField.setItems(FXCollections.observableArrayList(ActionMode.values()));
        modeField.setMaxWidth(Double.MAX_VALUE);
        add(modeField, 1, 0);

        // ===== colorLbl =====
        colorLbl.setText(bundle.getString("ActionSettingsView.colorLbl.text"));
        colorLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        colorLbl.disableProperty().bind(
                modeField.valueProperty().isNotEqualTo(ActionMode.VISUAL)
                        .and(modeField.valueProperty().isNotEqualTo(ActionMode.AV)));
        add(colorLbl, 0, 1);

        // ===== colorField =====
        colorField.setMaxWidth(Double.MAX_VALUE);
        colorField.disableProperty().bind(
                modeField.valueProperty().isNotEqualTo(ActionMode.VISUAL)
                        .and(modeField.valueProperty().isNotEqualTo(ActionMode.AV)));
        add(colorField, 1, 1);

        // ===== soundLbl =====
        soundLbl.setText(bundle.getString("ActionSettingsView.soundLbl.text"));
        soundLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        soundLbl.disableProperty().bind(
                modeField.valueProperty().isNotEqualTo(ActionMode.AUDIO)
                        .and(modeField.valueProperty().isNotEqualTo(ActionMode.AV)));
        add(soundLbl, 0, 2);

        // ===== soundField =====
        soundField.setItems(FXCollections.observableArrayList(SoundResource.values()));
        soundField.setMaxWidth(Double.MAX_VALUE);
        soundField.disableProperty().bind(
                modeField.valueProperty().isNotEqualTo(ActionMode.AUDIO)
                        .and(modeField.valueProperty().isNotEqualTo(ActionMode.AV)));
        add(soundField, 1, 2);

        // ===== intervalLbl =====
        intervalLbl.setText(bundle.getString("ActionSettingsView.intervalLbl.text"));
        intervalLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(intervalLbl, 0, 3);

        // ===== intervalField =====
        intervalField.setMaxWidth(Double.MAX_VALUE);
        add(intervalField, 1, 3);

        // ===== countLbl =====
        countLbl.setText(bundle.getString("ActionSettingsView.countLbl.text"));
        countLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(countLbl, 0, 4);

        // ===== countField =====
        countField.setMaxWidth(Double.MAX_VALUE);
        add(countField, 1, 4);
    }

    // region // ActionSettingsView - Variables declaration

    private Label modeLbl;
    private ChoiceBox<ActionMode> modeField;
    private Label colorLbl;
    private ColorPicker colorField;
    private Label soundLbl;
    private ChoiceBox<SoundResource> soundField;
    private Label intervalLbl;
    private IntField intervalField;
    private Label countLbl;
    private IntField countField;

    // endregion
}
