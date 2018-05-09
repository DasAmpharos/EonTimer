package com.github.dylmeadows.eontimer.ui.timers.gen5;

import com.github.dylmeadows.common.javafx.View;
import com.github.dylmeadows.common.javafx.node.GridPaneLayout;
import com.github.dylmeadows.common.javafx.node.IntField;
import com.github.dylmeadows.common.util.ResourceBundles;
import com.github.dylmeadows.eontimer.reference.Gen5TimerMode;
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

public class Gen5TimerView extends GridPane implements View {

    public Gen5TimerView() {
        initComponents();
    }

    public Gen5TimerMode getMode() {
        return modeField.getValue();
    }

    public ObjectProperty<Gen5TimerMode> modeProperty() {
        return modeField.valueProperty();
    }

    public void setMode(Gen5TimerMode mode) {
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

    public int getEntralinkCalibration() {
        return entralinkCalibrationField.getValue();
    }

    public IntegerProperty entralinkCalibrationProperty() {
        return entralinkCalibrationField.valueProperty();
    }

    public void setEntralinkCalibration(int entralinkCalibration) {
        entralinkCalibrationField.setValue(entralinkCalibration);
    }

    public int getFrameCalibration() {
        return frameCalibrationField.getValue();
    }

    public IntegerProperty frameCalibrationProperty() {
        return frameCalibrationField.valueProperty();
    }

    public void setFrameCalibration(int frameCalibration) {
        frameCalibrationField.setValue(frameCalibration);
    }

    public int getTargetAdvances() {
        return targetAdvancesField.getValue();
    }

    public IntegerProperty targetAdvancesProperty() {
        return targetAdvancesField.valueProperty();
    }

    public void setTargetAdvances(int targetAdvances) {
        targetAdvancesField.setValue(targetAdvances);
    }

    public int getSecondHit() {
        return secondHitField.getValue();
    }

    public IntegerProperty secondHitProperty() {
        return secondHitField.valueProperty();
    }

    public void setSecondHit(int secondHit) {
        secondHitField.setValue(secondHit);
    }

    public String getSecondHitText() {
        return secondHitField.getText();
    }

    public StringProperty secondHitTextProperty() {
        return secondHitField.textProperty();
    }

    public void setSecondHitText(String secondHitText) {
        secondHitField.setText(secondHitText);
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

    public int getActualAdvances() {
        return actualAdvancesField.getValue();
    }

    public IntegerProperty actualAdvancesProperty() {
        return actualAdvancesField.valueProperty();
    }

    public void setActualAdvances(int actualAdvances) {
        actualAdvancesField.setValue(actualAdvances);
    }

    public String getActualAdvancesText() {
        return actualAdvancesField.getText();
    }

    public StringProperty actualAdvancesTextProperty() {
        return actualAdvancesField.textProperty();
    }

    public void setActualAdvancesText(String actualAdvancesText) {
        actualAdvancesField.setText(actualAdvancesText);
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

    public boolean isCalibrationFieldDisabled() {
        return calibrationField.isDisabled();
    }

    public BooleanProperty calibrationFieldDisableProperty() {
        return calibrationField.disableProperty();
    }

    public void setCalibrationFieldDisable(boolean calibrationFieldDisable) {
        calibrationField.setDisable(calibrationFieldDisable);
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

    public boolean isEntralinkCalibrationFieldDisabled() {
        return entralinkCalibrationField.isDisabled();
    }

    public BooleanProperty entralinkCalibrationFieldDisableProperty() {
        return entralinkCalibrationField.disableProperty();
    }

    public void setEntralinkCalibrationFieldDisable(boolean entralinkCalibrationFieldDisable) {
        entralinkCalibrationField.setDisable(entralinkCalibrationFieldDisable);
    }

    public boolean isFrameCalibrationFieldDisabled() {
        return frameCalibrationField.isDisabled();
    }

    public BooleanProperty frameCalibrationFieldDisableProperty() {
        return frameCalibrationField.disableProperty();
    }

    public void setFrameCalibrationFieldDisable(boolean frameCalibrationFieldDisable) {
        frameCalibrationField.setDisable(frameCalibrationFieldDisable);
    }

    public boolean isTargetAdvancesFieldDisabled() {
        return targetAdvancesField.isDisabled();
    }

    public BooleanProperty targetAdvancesFieldDisableProperty() {
        return targetAdvancesField.disableProperty();
    }

    public void setTargetAdvancesFieldDisable(boolean targetAdvancesFieldDisable) {
        targetAdvancesField.setDisable(targetAdvancesFieldDisable);
    }

    public boolean isSecondHitFieldDisabled() {
        return secondHitField.isDisabled();
    }

    public BooleanProperty secondHitFieldDisableProperty() {
        return secondHitField.disableProperty();
    }

    public void setSecondHitFieldDisable(boolean secondHitFieldDisable) {
        secondHitField.setDisable(secondHitFieldDisable);
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

    public boolean isActualAdvancesFieldDisabled() {
        return actualAdvancesField.isDisabled();
    }

    public BooleanProperty actualAdvancesFieldDisableProperty() {
        return actualAdvancesField.disableProperty();
    }

    public void setActualAdvancesFieldDisable(boolean actualAdvancesFieldDisable) {
        actualAdvancesField.setDisable(actualAdvancesFieldDisable);
    }

    private void initComponents() {
        ResourceBundle bundle = ResourceBundles.getBundle(Gen5TimerView.class);
        modeLbl = new Label();
        modeField = new ChoiceBox<>();
        scrollPane = new ScrollPane();
        timerPropertyPane = new GridPane();
        calibrationLbl = new Label();
        calibrationField = new IntField();
        targetDelayLbl = new Label();
        targetDelayField = new IntField();
        targetSecondLbl = new Label();
        targetSecondField = new IntField();
        entralinkCalibrationLbl = new Label();
        entralinkCalibrationField = new IntField();
        frameCalibrationLbl = new Label();
        frameCalibrationField = new IntField();
        targetAdvancesLbl = new Label();
        targetAdvancesField = new IntField();
        timerUpdatePane = new GridPane();
        secondHitLbl = new Label();
        secondHitField = new IntField();
        delayHitLbl = new Label();
        delayHitField = new IntField();
        actualAdvancesLbl = new Label();
        actualAdvancesField = new IntField();

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
        modeLbl.setText(bundle.getString("Gen5TimerView.modeLbl.text"));
        modeLbl.getStyleClass().add("themeable");
        modeLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(modeLbl, 0, 0, 1, 1);

        // ===== modeField =====
        modeField.setItems(FXCollections.observableArrayList(Gen5TimerMode.values()));
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

                // ===== Timer Property Layouts =====
                GridPaneLayout cGearLayout = new GridPaneLayout(2, 3);
                GridPaneLayout standardLayout = new GridPaneLayout(2, 2);
                GridPaneLayout entralinkLayout = new GridPaneLayout(2, 4);
                GridPaneLayout entralinkPlusLayout = new GridPaneLayout(2, 6);

                // ===== calibrationLbl =====
                calibrationLbl.setText(bundle.getString("Gen5TimerView.calibrationLbl.text"));
                calibrationLbl.getStyleClass().add("themeable");
                calibrationLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                // add to layouts
                cGearLayout.add(calibrationLbl, 0, 0);
                standardLayout.add(calibrationLbl, 0, 0);
                entralinkLayout.add(calibrationLbl, 0, 0);
                entralinkPlusLayout.add(calibrationLbl, 0, 0);

                // ===== calibrationField =====
                calibrationField.getStyleClass().add("themeable");
                calibrationField.setMaxWidth(Double.MAX_VALUE);
                // add to layouts
                cGearLayout.add(calibrationField, 1, 0);
                standardLayout.add(calibrationField, 1, 0);
                entralinkLayout.add(calibrationField, 1, 0);
                entralinkPlusLayout.add(calibrationField, 1, 0);

                // ===== targetDelayLbl =====
                targetDelayLbl.setText(bundle.getString("Gen5TimerView.targetDelayLbl.text"));
                targetDelayLbl.getStyleClass().add("themeable");
                targetDelayLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                // add to layouts
                cGearLayout.add(targetDelayLbl, 0, 1);
                entralinkLayout.add(targetDelayLbl, 0, 1);
                entralinkPlusLayout.add(targetDelayLbl, 0, 1);

                // ===== targetDelayField =====
                targetDelayField.getStyleClass().add("themeable");
                targetDelayField.setMaxWidth(Double.MAX_VALUE);
                // add to layouts
                cGearLayout.add(targetDelayField, 1, 1);
                entralinkLayout.add(targetDelayField, 1, 1);
                entralinkPlusLayout.add(targetDelayField, 1, 1);

                // ===== targetSecondLbl =====
                targetSecondLbl.setText(bundle.getString("Gen5TimerView.targetSecondLbl.text"));
                targetSecondLbl.getStyleClass().add("themeable");
                targetSecondLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                // add to layouts
                cGearLayout.add(targetSecondLbl, 0, 2);
                standardLayout.add(targetSecondLbl, 0, 1);
                entralinkLayout.add(targetSecondLbl, 0, 2);
                entralinkPlusLayout.add(targetSecondLbl, 0, 2);

                // ===== targetSecondField =====
                targetSecondField.getStyleClass().add("themeable");
                targetSecondField.setMaxWidth(Double.MAX_VALUE);
                // add to layouts
                cGearLayout.add(targetSecondField, 1, 2);
                standardLayout.add(targetSecondField, 1, 1);
                entralinkLayout.add(targetSecondField, 1, 2);
                entralinkPlusLayout.add(targetSecondField, 1, 2);

                // ===== entralinkCalibrationLbl =====
                entralinkCalibrationLbl.setText(bundle.getString("Gen5TimerView.entralinkCalibrationLbl.text"));
                entralinkCalibrationLbl.getStyleClass().add("themeable");
                entralinkCalibrationLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                // add to layouts
                entralinkLayout.add(entralinkCalibrationLbl, 0, 3);
                entralinkPlusLayout.add(entralinkCalibrationLbl, 0, 3);

                // ===== entralinkCalibrationField =====
                entralinkCalibrationField.getStyleClass().add("themeable");
                entralinkCalibrationField.setMaxWidth(Double.MAX_VALUE);
                // add to layouts
                entralinkLayout.add(entralinkCalibrationField, 1, 3);
                entralinkPlusLayout.add(entralinkCalibrationField, 1, 3);

                // ===== frameCalibrationLbl =====
                frameCalibrationLbl.setText(bundle.getString("Gen5TimerView.frameCalibrationLbl.text"));
                frameCalibrationLbl.getStyleClass().add("themeable");
                frameCalibrationLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                // add to layouts
                entralinkPlusLayout.add(frameCalibrationLbl, 0, 4);

                // ===== frameCalibrationField =====
                frameCalibrationField.getStyleClass().add("themeable");
                frameCalibrationField.setMaxWidth(Double.MAX_VALUE);
                // add to layouts
                entralinkPlusLayout.add(frameCalibrationField, 1, 4);

                // ===== targetAdvancesLbl =====
                targetAdvancesLbl.setText(bundle.getString("Gen5TimerView.targetAdvancesLbl.text"));
                targetAdvancesLbl.getStyleClass().add("themeable");
                targetAdvancesLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                // add to layouts
                entralinkPlusLayout.add(targetAdvancesLbl, 0, 5);

                // ===== targetAdvancesField =====
                targetAdvancesField.getStyleClass().add("themeable");
                targetAdvancesField.setMaxWidth(Double.MAX_VALUE);
                // add to layouts
                entralinkPlusLayout.add(targetAdvancesField, 1, 5);

                // change timerPropertyPaneLayout as mode changes
                modeField.valueProperty().addListener((observable, oldValue, newValue) -> {
                    switch (newValue) {
                        case C_GEAR:
                            cGearLayout.apply(timerPropertyPane);
                            break;
                        case STANDARD:
                            standardLayout.apply(timerPropertyPane);
                            break;
                        case ENTRALINK:
                            entralinkLayout.apply(timerPropertyPane);
                            break;
                        case ENTRALINK_PLUS:
                            entralinkPlusLayout.apply(timerPropertyPane);
                            break;
                    }
                });
                standardLayout.apply(timerPropertyPane);
            }
            scrollPane.setContent(timerPropertyPane);
        }
        add(scrollPane, 0, 1, 4, 1);

        // ===== timerUpdatePane =====
        {
            timerUpdatePane.setHgap(10);
            timerUpdatePane.setVgap(10);

            // ===== ColumnConstraints =====
            {
                // ===== column0 =====
                ColumnConstraints column0 = new ColumnConstraints();
                column0.setHgrow(Priority.NEVER);
                column0.setPercentWidth(50);
                timerUpdatePane.getColumnConstraints().add(column0);

                // ===== column1 =====
                ColumnConstraints column1 = new ColumnConstraints();
                column1.setHgrow(Priority.ALWAYS);
                column1.setPercentWidth(50);
                timerUpdatePane.getColumnConstraints().add(column1);
            }

            // ===== Timer Update Layouts =====
            GridPaneLayout cGearLayout = new GridPaneLayout(2, 1);
            GridPaneLayout standardLayout = new GridPaneLayout(2, 1);
            GridPaneLayout entralinkLayout = new GridPaneLayout(2, 2);
            GridPaneLayout entralinkPlusLayout = new GridPaneLayout(2, 3);

            // ===== secondHitLbl =====
            secondHitLbl.setText(bundle.getString("Gen5TimerView.secondHitLbl.text"));
            secondHitLbl.getStyleClass().add("themeable");
            secondHitLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            // add to layouts
            standardLayout.add(secondHitLbl, 0, 0);
            entralinkLayout.add(secondHitLbl, 0, 0);
            entralinkPlusLayout.add(secondHitLbl, 0, 0);

            // ===== secondHitField =====
            secondHitField.setText("");
            secondHitField.setShowValueInPrompt(false);
            secondHitField.getStyleClass().add("themeable");
            secondHitField.setMaxWidth(Double.MAX_VALUE);
            // add to layouts
            standardLayout.add(secondHitField, 1, 0);
            entralinkLayout.add(secondHitField, 1, 0);
            entralinkPlusLayout.add(secondHitField, 1, 0);

            // ===== delayHitLbl =====
            delayHitLbl.setText(bundle.getString("Gen5TimerView.delayHitLbl.text"));
            delayHitLbl.getStyleClass().add("themeable");
            delayHitLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            // add to layouts
            cGearLayout.add(delayHitLbl, 0, 0);
            entralinkLayout.add(delayHitLbl, 0, 1);
            entralinkPlusLayout.add(delayHitLbl, 0, 1);

            // ===== delayHitField =====
            delayHitField.setText("");
            delayHitField.setShowValueInPrompt(false);
            delayHitField.getStyleClass().add("themeable");
            delayHitField.setMaxWidth(Double.MAX_VALUE);
            // add to layouts
            cGearLayout.add(delayHitField, 1, 0);
            entralinkLayout.add(delayHitField, 1, 1);
            entralinkPlusLayout.add(delayHitField, 1, 1);

            // ===== actualAdvancesLbl =====
            actualAdvancesLbl.setText(bundle.getString("Gen5TimerView.actualAdvancesLbl.text"));
            actualAdvancesLbl.getStyleClass().add("themeable");
            actualAdvancesLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            // add to layouts
            entralinkPlusLayout.add(actualAdvancesLbl, 0, 2);

            // ===== actualAdvancesField =====
            actualAdvancesField.setText("");
            actualAdvancesField.setShowValueInPrompt(false);
            actualAdvancesField.getStyleClass().add("themeable");
            actualAdvancesField.setMaxWidth(Double.MAX_VALUE);
            // add to layouts
            entralinkPlusLayout.add(actualAdvancesField, 1, 2);

            // change timerUpdatePaneLayout as mode changes
            modeField.valueProperty().addListener((observable, oldValue, newValue) -> {
                switch (newValue) {
                    case C_GEAR:
                        cGearLayout.apply(timerUpdatePane);
                        break;
                    case STANDARD:
                        standardLayout.apply(timerUpdatePane);
                        break;
                    case ENTRALINK:
                        entralinkLayout.apply(timerUpdatePane);
                        break;
                    case ENTRALINK_PLUS:
                        entralinkPlusLayout.apply(timerUpdatePane);
                        break;
                }
            });
            standardLayout.apply(timerUpdatePane);
        }
        add(timerUpdatePane, 0, 2, 4, 1);
    }

    // region Variables Declaration

    private Label modeLbl;
    private ChoiceBox<Gen5TimerMode> modeField;
    private ScrollPane scrollPane;
    private GridPane timerPropertyPane;
    private Label calibrationLbl;
    private IntField calibrationField;
    private Label targetDelayLbl;
    private IntField targetDelayField;
    private Label targetSecondLbl;
    private IntField targetSecondField;
    private Label entralinkCalibrationLbl;
    private IntField entralinkCalibrationField;
    private Label frameCalibrationLbl;
    private IntField frameCalibrationField;
    private Label targetAdvancesLbl;
    private IntField targetAdvancesField;
    private GridPane timerUpdatePane;
    private Label secondHitLbl;
    private IntField secondHitField;
    private Label delayHitLbl;
    private IntField delayHitField;
    private Label actualAdvancesLbl;
    private IntField actualAdvancesField;

    // endregion
}
