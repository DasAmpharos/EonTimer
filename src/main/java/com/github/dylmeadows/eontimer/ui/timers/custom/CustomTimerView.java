package com.github.dylmeadows.eontimer.ui.timers.custom;

import com.github.dylmeadows.common.javafx.node.IntField;
import com.github.dylmeadows.eontimer.util.ResourceBundles;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.time.Duration;
import java.util.ResourceBundle;

public class CustomTimerView extends GridPane {

    public CustomTimerView() {
        initComponents();
    }

    public ObservableList<Duration> getItems() {
        return list.getItems();
    }

    public void setItems(ObservableList<Duration> items) {
        list.setItems(items);
    }

    public SelectionModel<Duration> getSelectionModel() {
        return list.getSelectionModel();
    }

    public String getValueFieldText() {
        return valueField.getText();
    }

    public StringProperty valueFieldTextProperty() {
        return valueField.textProperty();
    }

    public void setValueFieldText(String text) {
        valueField.setText(text);
    }

    public int getValueFieldValue() {
        return valueField.getValue();
    }

    public void setValueFieldValue(int value) {
        valueField.setValue(value);
    }

    public EventHandler<? super KeyEvent> getOnListKeyPressed() {
        return list.getOnKeyPressed();
    }

    public void setOnListKeyPressed(EventHandler<? super KeyEvent> onListKeyPressed) {
        list.setOnKeyPressed(onListKeyPressed);
    }

    public EventHandler<ActionEvent> getOnValueFieldAction() {
        return valueField.getOnAction();
    }

    public void setOnValueFieldAction(EventHandler<ActionEvent> onValueFieldAction) {
        valueField.setOnAction(onValueFieldAction);
    }

    public EventHandler<ActionEvent> getOnValueAddBtnAction() {
        return valueAddBtn.getOnAction();
    }

    public void setOnValueAddBtnAction(EventHandler<ActionEvent> valueAddBtnOnAction) {
        valueAddBtn.setOnAction(valueAddBtnOnAction);
    }

    public EventHandler<ActionEvent> getOnValueRemoveBtnAction() {
        return valueRemoveBtn.getOnAction();
    }

    public void setOnValueRemoveBtnAction(EventHandler<ActionEvent> onValueRemoveBtnAction) {
        valueRemoveBtn.setOnAction(onValueRemoveBtnAction);
    }

    public EventHandler<ActionEvent> getOnValueMoveUpBtnAction() {
        return valueMoveUpBtn.getOnAction();
    }

    public void setOnValueMoveUpBtnAction(EventHandler<ActionEvent> onValueMoveUpBtnAction) {
        valueMoveUpBtn.setOnAction(onValueMoveUpBtnAction);
    }

    public EventHandler<ActionEvent> getOnValueMoveDownBtnAction() {
        return valueMoveDownBtn.getOnAction();
    }

    public void setOnValueMoveDownBtnAction(EventHandler<ActionEvent> onValueMoveDownBtnAction) {
        valueMoveDownBtn.setOnAction(onValueMoveDownBtnAction);
    }

    public EventHandler<ActionEvent> getOnRemoveMenuItemAction() {
        return removeMenuItem.getOnAction();
    }

    public void setOnRemoveMenuItemAction(EventHandler<ActionEvent> onRemoveMenuItemAction) {
        removeMenuItem.setOnAction(onRemoveMenuItemAction);
    }

    public boolean isListDisabled() {
        return list.isDisabled();
    }

    public BooleanProperty listDisableProperty() {
        return list.disableProperty();
    }

    public void setListDisable(boolean listDisable) {
        list.setDisable(listDisable);
    }

    public boolean isValueFieldDisabled() {
        return valueField.isDisabled();
    }

    public BooleanProperty valueFieldDisableProperty() {
        return valueField.disableProperty();
    }

    public void setValueFieldDisable(boolean valueFieldDisable) {
        valueField.setDisable(valueFieldDisable);
    }

    public boolean isValueAddBtnDisabled() {
        return valueAddBtn.isDisabled();
    }

    public BooleanProperty valueAddBtnDisableProperty() {
        return valueAddBtn.disableProperty();
    }

    public void setValueAddBtnDisable(boolean valueAddBtnDisable) {
        valueAddBtn.setDisable(valueAddBtnDisable);
    }

    public boolean isValueRemoveBtnDisabled() {
        return valueRemoveBtn.isDisabled();
    }

    public BooleanProperty valueRemoveBtnDisableProperty() {
        return valueRemoveBtn.disableProperty();
    }

    public void setValueRemoveBtnDisable(boolean valueRemoveBtnDisable) {
        valueRemoveBtn.setDisable(valueRemoveBtnDisable);
    }

    public boolean isValueMoveUpBtnDisabled() {
        return valueMoveUpBtn.isDisabled();
    }

    public BooleanProperty valueMoveUpBtnDisableProperty() {
        return valueMoveUpBtn.disableProperty();
    }

    public void setValueMoveUpBtnDisable(boolean valueMoveUpBtnDisable) {
        valueMoveUpBtn.setDisable(valueMoveUpBtnDisable);
    }

    public boolean isValueMoveDownBtnDisabled() {
        return valueMoveDownBtn.isDisabled();
    }

    public BooleanProperty valueMoveDownBtnDisableProperty() {
        return valueMoveDownBtn.disableProperty();
    }

    public void setValueMoveDownBtnDisabled(boolean valueMoveDownBtnDisabled) {
        valueMoveDownBtn.setDisable(valueMoveDownBtnDisabled);
    }

    private void initComponents() {
        ResourceBundle bundle = ResourceBundles.getBundle(CustomTimerView.class);
        list = new ListView<>();
        listContextMenu = new ContextMenu();
        removeMenuItem = new MenuItem();
        controlPnl = new HBox();
        valuePnl = new HBox();
        valueField = new IntField();
        valueAddBtn = new Button();
        valueRemoveBtn = new Button();
        valueMovePnl = new VBox();
        valueMoveUpBtn = new Button();
        valueMoveDownBtn = new Button();


        // ===== this =====
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        // ===== ColumnConstraints =====
        {
            // ===== column0 =====
            ColumnConstraints column0 = new ColumnConstraints();
            column0.setMaxWidth(Double.MAX_VALUE);
            column0.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(column0);
        }
        // ===== RowConstraints =====
        {
            // ===== row0 =====
            RowConstraints row0 = new RowConstraints();
            row0.setMaxHeight(Double.MAX_VALUE);
            row0.setVgrow(Priority.ALWAYS);
            getRowConstraints().add(row0);

            // ===== row1 =====
            RowConstraints row1 = new RowConstraints();
            row1.setMaxHeight(Double.MAX_VALUE);
            row1.setVgrow(Priority.NEVER);
            getRowConstraints().add(row1);
        }

        // ===== list =====
        list.setEditable(true);
        list.setCellFactory(TextFieldListCell.forListView(new CustomTimerStringConverter()));
        add(list, 0, 0, 1, 1);

        // ===== hasSelection =====
        BooleanBinding hasSelection = list.getSelectionModel().selectedIndexProperty().isNotEqualTo(-1);

        // ===== listContextMenu =====
        {
            // ===== removeMenuItem =====
            removeMenuItem.setText(bundle.getString("CustomTimerView.removeMenuItem.text"));
            listContextMenu.getItems().add(removeMenuItem);
        }
        list.setContextMenu(listContextMenu);

        // ===== valueMovePnl =====
        {
            valueMovePnl.setSpacing(5);
            valueMovePnl.setAlignment(Pos.CENTER);
            valueMovePnl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            // ===== valueMoveUpBtn =====
            valueMoveUpBtn.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.CHEVRON_UP));
            valueMovePnl.getChildren().add(valueMoveUpBtn);

            // ===== valueMoveDownBtn =====
            valueMoveDownBtn.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.CHEVRON_DOWN));
            valueMovePnl.getChildren().add(valueMoveDownBtn);
        }
        add(valueMovePnl, 1, 0, 1, 1);

        // ===== controlPnl =====
        {
            controlPnl.setSpacing(10);

            // ===== valuePnl =====
            {
                HBox.setHgrow(valuePnl, Priority.ALWAYS);

                // ===== valueField =====
                valueField.setText("");
                valueField.setShowValueInPrompt(false);
                HBox.setHgrow(valueField, Priority.ALWAYS);
                valuePnl.getChildren().add(valueField);

                // ===== valueAddBtn =====
                valueAddBtn.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.PLUS));
                HBox.setHgrow(valueAddBtn, Priority.NEVER);
                valuePnl.getChildren().add(valueAddBtn);
            }
            controlPnl.getChildren().add(valuePnl);

            // ===== valueRemoveBtn =====
            valueRemoveBtn.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.MINUS));
            controlPnl.getChildren().add(valueRemoveBtn);
        }
        add(controlPnl, 0, 1, 1, 1);
    }

    // region // CustomTimerView - Variables declaration

    private ListView<Duration> list;
    private ContextMenu listContextMenu;
    private MenuItem removeMenuItem;
    private HBox controlPnl;
    private HBox valuePnl;
    private IntField valueField;
    private Button valueAddBtn;
    private Button valueRemoveBtn;
    private VBox valueMovePnl;
    private Button valueMoveUpBtn;
    private Button valueMoveDownBtn;

    // endregion
}
