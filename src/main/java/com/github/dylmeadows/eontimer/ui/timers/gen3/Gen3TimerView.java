package com.github.dylmeadows.eontimer.ui.timers.gen3;

import com.github.dylmeadows.common.javafx.node.GridPaneLayout;
import com.github.dylmeadows.common.javafx.node.IntField;
import com.github.dylmeadows.eontimer.util.extension.ResourceBundleExtensions;
import com.github.dylmeadows.eontimer.model.Gen3TimerMode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.ResourceBundle;

public class Gen3TimerView extends GridPane {

    public Gen3TimerView() {
        initComponents();
    }

    public Gen3TimerMode getMode() {
        return modeField.getValue();
    }

    public ObjectProperty<Gen3TimerMode> modeProperty() {
        return modeField.valueProperty();
    }

    public void setMode(Gen3TimerMode mode) {
        modeField.setValue(mode);
    }

    public int getCalibration() {
        return calibrationField.getValue();
    }

    public IntegerProperty calibrationProperty() {
        return calibrationField.valueProperty();
    }

    public void setCalibration(int calibration) {
        calibrationField.setValue(calibration);
    }

    public int getPreTimer() {
        return preTimerField.getValue();
    }

    public IntegerProperty preTimerProperty() {
        return preTimerField.valueProperty();
    }

    public void setPreTimer(int preTimer) {
        preTimerField.setValue(preTimer);
    }

    public int getTargetFrame() {
        return targetFrameField.getValue();
    }

    public IntegerProperty targetFrameProperty() {
        return targetFrameField.valueProperty();
    }

    public void setTargetFrame(int targetFrame) {
        targetFrameField.setValue(targetFrame);
    }

    public int getFrameHit() {
        return frameHitField.getValue();
    }

    public IntegerProperty frameHitProperty() {
        return frameHitField.valueProperty();
    }

    public void setFrameHit(int frameHit) {
        frameHitField.setValue(frameHit);
    }

    public String getFrameHitText() {
        return frameHitField.getText();
    }

    public StringProperty frameHitTextProperty() {
        return frameHitField.textProperty();
    }

    public void setFrameHitText(String frameHitText) {
        frameHitField.setText(frameHitText);
    }

    public boolean isModeFieldDisable() {
        return modeField.isDisabled();
    }

    public BooleanProperty modeFieldDisableProperty() {
        return modeField.disableProperty();
    }

    public void setModeFieldDisable(boolean modeFieldDisable) {
        modeField.setDisable(modeFieldDisable);
    }

    public boolean isCalibrationFieldDisabled() {
        return calibrationField.isDisabled();
    }

    public BooleanProperty calibrationFieldDisableProperty() {
        return calibrationField.disableProperty();
    }

    public void setCalibrationFieldDisable(boolean calibrationFieldDisable) {
        calibrationField.setDisable(calibrationFieldDisable);
    }

    public boolean isPreTimerFieldDisabled() {
        return preTimerField.isDisabled();
    }

    public BooleanProperty preTimerFieldDisableProperty() {
        return preTimerField.disableProperty();
    }

    public void setPreTimerFieldDisable(boolean preTimerFieldDisable) {
        preTimerField.setDisable(preTimerFieldDisable);
    }

    public boolean isTargetFrameFieldDisabled() {
        return targetFrameField.isDisabled();
    }

    public BooleanProperty targetFrameFieldDisableProperty() {
        return targetFrameField.disableProperty();
    }

    public void setTargetFrameFieldDisable(boolean targetFrameFieldDisable) {
        targetFrameField.setDisable(targetFrameFieldDisable);
    }

    public boolean isFrameHitFieldDisabled() {
        return frameHitField.isDisabled();
    }

    public BooleanProperty frameHitFieldDisableProperty() {
        return frameHitField.disableProperty();
    }

    public void setFrameHitFieldDisable(boolean frameHitFieldDisable) {
        frameHitField.setDisable(frameHitFieldDisable);
    }

    private void initComponents() {
        ResourceBundle bundle = ResourceBundleExtensions.getBundle(Gen3TimerView.class);
        modeLbl = new Label();
        modeField = new ChoiceBox<>();
        scrollPane = new ScrollPane();
        timerPropertyPane = new GridPane();
        calibrationLbl = new Label();
        calibrationField = new IntField();
        preTimerLbl = new Label();
        preTimerField = new IntField();
        targetFrameLbl = new Label();
        targetFrameField = new IntField();
        frameHitLbl = new Label();
        frameHitField = new IntField();

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
            column0.setPercentWidth(25);
            getColumnConstraints().add(column0);

            // ===== column1 =====
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setHalignment(HPos.LEFT);
            column1.setHgrow(Priority.ALWAYS);
            column1.setPercentWidth(25);
            getColumnConstraints().add(column1);

            // ===== column2 =====
            ColumnConstraints column2 = new ColumnConstraints();
            column2.setHalignment(HPos.LEFT);
            column2.setHgrow(Priority.ALWAYS);
            column2.setPercentWidth(25);
            getColumnConstraints().add(column2);

            // ===== column3 =====
            ColumnConstraints column3 = new ColumnConstraints();
            column3.setHalignment(HPos.LEFT);
            column3.setHgrow(Priority.ALWAYS);
            column3.setPercentWidth(25);
            getColumnConstraints().add(column3);
        }
        // ===== RowConstraints =====
        {
            // ===== row0 =====
            RowConstraints row0 = new RowConstraints();
            row0.setValignment(VPos.CENTER);
            row0.setVgrow(Priority.NEVER);
            getRowConstraints().add(row0);

            // ===== row1 =====
            RowConstraints row1 = new RowConstraints();
            row1.setValignment(VPos.CENTER);
            row1.setVgrow(Priority.ALWAYS);
            getRowConstraints().add(row1);

            // ===== row2 =====
            RowConstraints row2 = new RowConstraints();
            row2.setValignment(VPos.CENTER);
            row2.setVgrow(Priority.NEVER);
            getRowConstraints().add(row2);
        }

        // ===== modeLbl =====
        modeLbl.setText(bundle.getString("Gen3TimerView.modeLbl.text"));
        modeLbl.getStyleClass().add("themeable");
        modeLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(modeLbl, 0, 0, 1, 1);

        // ===== modeField =====
        // TODO: Add variable timer
        modeField.setItems(FXCollections.observableArrayList(Gen3TimerMode.STANDARD));
        modeField.getStyleClass().add("themeable");
        modeField.setMaxWidth(Double.MAX_VALUE);
        add(modeField, 1, 0, 3, 1);

        // ===== scrollPane =====
        {
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("themeable");

            // ===== timerPropertyPane =====
            {
                timerPropertyPane.setPadding(new Insets(10));
                timerPropertyPane.setHgap(10);
                timerPropertyPane.setVgap(10);

                // ===== ColumnConstraints =====
                {
                    // ===== column0 =====
                    ColumnConstraints column0 = new ColumnConstraints();
                    column0.setHgrow(Priority.NEVER);
                    column0.setPercentWidth(50);
                    timerPropertyPane.getColumnConstraints().add(column0);

                    // ===== column1 =====
                    ColumnConstraints column1 = new ColumnConstraints();
                    column1.setHgrow(Priority.ALWAYS);
                    column1.setPercentWidth(50);
                    timerPropertyPane.getColumnConstraints().add(column1);
                }
                // ===== RowConstraints =====
                {
                    // ===== row0 =====
                    RowConstraints row0 = new RowConstraints();
                    row0.setVgrow(Priority.NEVER);
                    timerPropertyPane.getRowConstraints().add(row0);

                    // ===== row1 =====
                    RowConstraints row1 = new RowConstraints();
                    row1.setVgrow(Priority.NEVER);
                    timerPropertyPane.getRowConstraints().add(row1);

                    // ===== row2 =====
                    RowConstraints row2 = new RowConstraints();
                    row2.setVgrow(Priority.NEVER);
                    timerPropertyPane.getRowConstraints().add(row2);
                }

                // ===== Timer Property Layout =====
                GridPaneLayout standardLayout = new GridPaneLayout();
                GridPaneLayout variableTargetLayout = new GridPaneLayout();

                // ===== calibrationLbl =====
                calibrationLbl.setText(bundle.getString("Gen3TimerView.calibrationLbl.text"));
                calibrationLbl.getStyleClass().add("themeable");
                calibrationLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                standardLayout.add(calibrationLbl, 0, 0, 1, 1);

                // ===== calibrationField =====
                calibrationField.getStyleClass().add("themeable");
                calibrationField.setMaxWidth(Double.MAX_VALUE);
                standardLayout.add(calibrationField, 1, 0, 1, 1);

                // ===== preTimerLbl =====
                preTimerLbl.setText(bundle.getString("Gen3TimerView.preTimerLbl.text"));
                preTimerLbl.getStyleClass().add("themeable");
                preTimerLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                standardLayout.add(preTimerLbl, 0, 1, 1, 1);

                // ===== preTimerField =====
                preTimerField.getStyleClass().add("themeable");
                preTimerField.setMaxWidth(Double.MAX_VALUE);
                standardLayout.add(preTimerField, 1, 1, 1, 1);

                // ===== targetFrameLbl =====
                targetFrameLbl.setText(bundle.getString("Gen3TimerView.targetFrameLbl.text"));
                targetFrameLbl.getStyleClass().add("themeable");
                targetFrameLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                standardLayout.add(targetFrameLbl, 0, 2, 1, 1);
                variableTargetLayout.add(targetFrameLbl, 0, 0, 1, 1);

                // ===== targetFrameField =====
                targetFrameField.getStyleClass().add("themeable");
                targetFrameField.setMaxWidth(Double.MAX_VALUE);
                standardLayout.add(targetFrameField, 1, 2, 1, 1);
                variableTargetLayout.add(targetFrameField, 1, 0, 1, 1);

                // change timerPropertyPane layout as mode changes
                modeField.valueProperty().addListener((observable, oldValue, newValue) -> {
                    switch (newValue) {
                        case STANDARD:
                            standardLayout.apply(timerPropertyPane);
                            break;
                        case VARIABLE_TARGET:
                            variableTargetLayout.apply(timerPropertyPane);
                            break;
                    }
                });
                standardLayout.apply(timerPropertyPane);
            }
            scrollPane.setContent(timerPropertyPane);
        }
        add(scrollPane, 0, 1, 4, 1);

        // ===== frameHitLbl =====
        frameHitLbl.setText(bundle.getString("Gen3TimerView.frameHitLbl.text"));
        frameHitLbl.getStyleClass().add("themeable");
        frameHitLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(frameHitLbl, 0, 2, 2, 1);

        // ===== frameHitField =====
        frameHitField.setText("");
        frameHitField.setShowValueInPrompt(false);
        frameHitField.getStyleClass().add("themeable");
        frameHitField.setMaxWidth(Double.MAX_VALUE);
        add(frameHitField, 2, 2, 2, 1);
    }

    // region // Gen3TimerView - Variables declaration

    private Label modeLbl;
    private ChoiceBox<Gen3TimerMode> modeField;
    private ScrollPane scrollPane;
    private GridPane timerPropertyPane;
    private Label calibrationLbl;
    private IntField calibrationField;
    private Label preTimerLbl;
    private IntField preTimerField;
    private Label targetFrameLbl;
    private IntField targetFrameField;
    private Label frameHitLbl;
    private IntField frameHitField;

    // endregion
}
