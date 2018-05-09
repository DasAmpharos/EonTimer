package com.github.dylmeadows.eontimer.ui.timers.gen4;

import com.github.dylmeadows.common.javafx.View;
import com.github.dylmeadows.common.javafx.node.IntField;
import com.github.dylmeadows.common.util.ResourceBundles;
import com.github.dylmeadows.eontimer.reference.Gen4TimerMode;
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

public class Gen4TimerView extends GridPane implements View {

    public Gen4TimerView() {
        initComponents();
    }

    public Gen4TimerMode getMode() {
        return modeField.getValue();
    }

    public ObjectProperty<Gen4TimerMode> modeProperty() {
        return modeField.valueProperty();
    }

    public void setMode(Gen4TimerMode mode) {
        modeField.setValue(mode);
    }

    public int getCalibratedDelay() {
        return calibratedDelayField.getValue();
    }

    public IntegerProperty calibratedDelayProperty() {
        return calibratedDelayField.valueProperty();
    }

    public void setCalibratedDelay(int calibratedDelay) {
        calibratedDelayField.setValue(calibratedDelay);
    }

    public int getCalibratedSecond() {
        return calibratedSecondField.getValue();
    }

    public IntegerProperty calibratedSecondProperty() {
        return calibratedSecondField.valueProperty();
    }

    public void setCalibratedSecond(int calibratedSecond) {
        calibratedSecondField.setValue(calibratedSecond);
    }

    public int getTargetDelay() {
        return targetDelayField.getValue();
    }

    public IntegerProperty targetDelayProperty() {
        return targetDelayField.valueProperty();
    }

    public void setTargetDelay(int targetDelay) {
        targetDelayField.setValue(targetDelay);
    }

    public int getTargetSecond() {
        return targetSecondField.getValue();
    }

    public IntegerProperty targetSecondProperty() {
        return targetSecondField.valueProperty();
    }

    public void setTargetSecond(int targetSecond) {
        targetSecondField.setValue(targetSecond);
    }

    public int getDelayHit() {
        return delayHitField.getValue();
    }

    public IntegerProperty delayHitProperty() {
        return delayHitField.valueProperty();
    }

    public void setDelayHit(int delayHit) {
        delayHitField.setValue(delayHit);
    }

    public String getDelayHitText() {
        return delayHitField.getText();
    }

    public StringProperty delayHitTextProperty() {
        return delayHitField.textProperty();
    }

    public void setDelayHitText(String delayHitText) {
        delayHitField.setText(delayHitText);
    }

    public boolean isModeFieldDisabled() {
        return modeField.isDisabled();
    }

    public BooleanProperty modeFieldDisableProperty() {
        return modeField.disableProperty();
    }

    public void setModeFieldDisable(boolean modeFieldDisable) {
        modeField.setDisable(modeFieldDisable);
    }

    public boolean isCalibratedDelayFieldDisabled() {
        return calibratedDelayField.isDisabled();
    }

    public BooleanProperty calibratedDelayFieldDisableProperty() {
        return calibratedDelayField.disableProperty();
    }

    public void setCalibratedDelayFieldDisable(boolean calibratedDelayFieldDisable) {
        calibratedDelayField.setDisable(calibratedDelayFieldDisable);
    }

    public boolean isCalibratedSecondFieldDisabled() {
        return calibratedSecondField.isDisabled();
    }

    public BooleanProperty calibratedSecondFieldDisableProperty() {
        return calibratedSecondField.disableProperty();
    }

    public void setCalibratedSecondFieldDisable(boolean calibratedSecondFieldDisable) {
        calibratedSecondField.setDisable(calibratedSecondFieldDisable);
    }

    public boolean isTargetDelayFieldDisabled() {
        return targetDelayField.isDisabled();
    }

    public BooleanProperty targetDelayFieldDisableProperty() {
        return targetDelayField.disableProperty();
    }

    public void setTargetDelayFieldDisable(boolean targetDelayFieldDisable) {
        targetDelayField.setDisable(targetDelayFieldDisable);
    }

    public boolean isTargetSecondFieldDisabled() {
        return targetSecondField.isDisabled();
    }

    public BooleanProperty targetSecondFieldDisableProperty() {
        return targetSecondField.disableProperty();
    }

    public void setTargetSecondFieldDisable(boolean targetSecondFieldDisable) {
        targetSecondField.setDisable(targetSecondFieldDisable);
    }

    public boolean isDelayHitFieldDisabled() {
        return delayHitField.isDisabled();
    }

    public BooleanProperty delayHitFieldDisableProperty() {
        return delayHitField.disableProperty();
    }

    public void setDelayHitFieldDisable(boolean delayHitFieldDisable) {
        delayHitField.setDisable(delayHitFieldDisable);
    }

    private void initComponents() {
        ResourceBundle bundle = ResourceBundles.getBundle(Gen4TimerView.class);
        modeLbl = new Label();
        modeField = new ChoiceBox<>();
        scrollPane = new ScrollPane();
        timerPropertyPane = new GridPane();
        calibratedDelayLbl = new Label();
        calibratedDelayField = new IntField();
        calibratedSecondLbl = new Label();
        calibratedSecondField = new IntField();
        targetDelayLbl = new Label();
        targetDelayField = new IntField();
        targetSecondLbl = new Label();
        targetSecondField = new IntField();
        delayHitLbl = new Label();
        delayHitField = new IntField();

        // ===== Gen4TimerView =====
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
        modeLbl.setText(bundle.getString("Gen4TimerView.modeLbl.text"));
        modeLbl.getStyleClass().add("themeable");
        modeLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(modeLbl, 0, 0, 1, 1);

        // ===== modeField =====
        modeField.setItems(FXCollections.observableArrayList(Gen4TimerMode.values()));
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

                // ===== calibratedDelayLbl =====
                calibratedDelayLbl.setText(bundle.getString("Gen4TimerView.calibratedDelayLbl.text"));
                calibratedDelayLbl.getStyleClass().add("themeable");
                calibratedDelayLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                timerPropertyPane.add(calibratedDelayLbl, 0, 0, 1, 1);

                // ===== calibratedDelayField =====
                calibratedDelayField.getStyleClass().add("themeable");
                calibratedDelayField.setMaxWidth(Double.MAX_VALUE);
                timerPropertyPane.add(calibratedDelayField, 1, 0, 1, 1);

                // ===== calibratedSecondLbl =====
                calibratedSecondLbl.setText(bundle.getString("Gen4TimerView.calibratedSecondLbl.text"));
                calibratedSecondLbl.getStyleClass().add("themeable");
                calibratedSecondLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                timerPropertyPane.add(calibratedSecondLbl, 0, 1, 1, 1);

                // ===== calibratedSecondField =====
                calibratedSecondField.getStyleClass().add("themeable");
                calibratedSecondField.setMaxWidth(Double.MAX_VALUE);
                timerPropertyPane.add(calibratedSecondField, 1, 1, 1, 1);

                // ===== targetDelayLbl =====
                targetDelayLbl.setText(bundle.getString("Gen4TimerView.targetDelayLbl.text"));
                targetDelayLbl.getStyleClass().add("themeable");
                targetDelayLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                timerPropertyPane.add(targetDelayLbl, 0, 2, 1, 1);

                // ===== targetDelayField =====
                targetDelayField.getStyleClass().add("themeable");
                targetDelayField.setMaxWidth(Double.MAX_VALUE);
                timerPropertyPane.add(targetDelayField, 1, 2, 1, 1);

                // ===== targetSecondLbl =====
                targetSecondLbl.setText(bundle.getString("Gen4TimerView.targetSecondLbl.text"));
                targetSecondLbl.getStyleClass().add("themeable");
                targetSecondLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                timerPropertyPane.add(targetSecondLbl, 0, 3, 1, 1);

                // ===== targetSecondField =====
                targetSecondField.getStyleClass().add("themeable");
                targetSecondField.setMaxWidth(Double.MAX_VALUE);
                timerPropertyPane.add(targetSecondField, 1, 3, 1, 1);

            }
            scrollPane.setContent(timerPropertyPane);
        }
        add(scrollPane, 0, 1, 4, 1);

        // ===== delayHitLbl =====
        delayHitLbl.setText(bundle.getString("Gen4TimerView.delayHitLbl.text"));
        delayHitLbl.getStyleClass().add("themeable");
        delayHitLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(delayHitLbl, 0, 2, 2, 1);

        // ===== delayHitField =====
        delayHitField.setText("");
        delayHitField.setShowValueInPrompt(false);
        delayHitField.getStyleClass().add("themeable");
        delayHitField.setMaxWidth(Double.MAX_VALUE);
        add(delayHitField, 2, 2, 2, 1);
        // endregion
    }

    // region Variable Declaration

    private Label modeLbl;
    private ChoiceBox<Gen4TimerMode> modeField;
    private ScrollPane scrollPane;
    private GridPane timerPropertyPane;
    private Label calibratedDelayLbl;
    private IntField calibratedDelayField;
    private Label calibratedSecondLbl;
    private IntField calibratedSecondField;
    private Label targetDelayLbl;
    private IntField targetDelayField;
    private Label targetSecondLbl;
    private IntField targetSecondField;
    private Label delayHitLbl;
    private IntField delayHitField;

    // endregion
}
