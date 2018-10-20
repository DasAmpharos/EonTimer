package com.github.dylmeadows.eontimer.ui.settings.timer;

import com.github.dylmeadows.eontimer.model.Console;
import com.github.dylmeadows.common.javafx.node.IntField;
import com.github.dylmeadows.eontimer.util.ResourceBundles;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.ResourceBundle;

public class TimerSettingsView extends GridPane {

    public TimerSettingsView() {
        initComponents();
    }

    private void initComponents() {
        ResourceBundle bundle = ResourceBundles.getBundle(TimerSettingsView.class);
        consoleLbl = new Label();
        consoleField = new ChoiceBox<>();
        refreshIntervalLbl = new Label();
        refreshIntervalField = new IntField();
        precisionCalibrationModeField = new CheckBox();

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

        // ===== consoleLbl =====
        consoleLbl.setText(bundle.getString("TimerSettingsView.consoleLbl.text"));
        consoleLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(consoleLbl, 0, 0);

        // ===== consoleField =====
        consoleField.setItems(FXCollections.observableArrayList(Console.values()));
        consoleField.setMaxWidth(Double.MAX_VALUE);
        add(consoleField, 1, 0);

        // ===== refreshIntervalLbl =====
        refreshIntervalLbl.setText(bundle.getString("TimerSettingsView.refreshIntervalLbl.text"));
        refreshIntervalLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(refreshIntervalLbl, 0, 1);

        // ===== refreshIntervalField =====
        refreshIntervalField.setMaxWidth(Double.MAX_VALUE);
        add(refreshIntervalField, 1, 1);

        // ===== precisionCalibrationField =====
        precisionCalibrationModeField.setText(bundle.getString("TimerSettingsView.precisionCalibrationModeField.text"));
        precisionCalibrationModeField.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        precisionCalibrationModeField.setAlignment(Pos.CENTER_RIGHT);
        add(precisionCalibrationModeField, 0, 2, 2, 1);
    }

    public Console getConsole() {
        return consoleField.getValue();
    }

    public ObjectProperty<Console> consoleProperty() {
        return consoleField.valueProperty();
    }

    public void setConsole(Console console) {
        consoleField.setValue(console);
    }

    public int getRefreshInterval() {
        return refreshIntervalField.getValue();
    }

    public IntegerProperty refreshIntervalProperty() {
        return refreshIntervalField.valueProperty();
    }

    public void setRefreshInterval(int refreshInterval) {
        refreshIntervalField.setValue(refreshInterval);
    }

    public boolean isPrecisionCalibrationMode() {
        return precisionCalibrationModeField.isSelected();
    }

    public BooleanProperty precisionCalibrationModeProperty() {
        return precisionCalibrationModeField.selectedProperty();
    }

    public void setPrecisionCalibrationMode(boolean precisionCalibrationMode) {
        precisionCalibrationModeField.setSelected(precisionCalibrationMode);
    }

    // region // TimerSettingsView - Variables declaration

    private Label consoleLbl;
    private ChoiceBox<Console> consoleField;
    private Label refreshIntervalLbl;
    private IntField refreshIntervalField;
    private CheckBox precisionCalibrationModeField;

    // endregion
}
