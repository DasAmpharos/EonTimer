package com.github.dylmeadows.eontimer.ui;

import com.github.dylmeadows.eontimer.util.ResourceBundles;
import com.github.dylmeadows.eontimer.ui.settings.EonTimerSettingsView;
import com.github.dylmeadows.eontimer.ui.timers.custom.CustomTimerView;
import com.github.dylmeadows.eontimer.ui.timers.gen3.Gen3TimerView;
import com.github.dylmeadows.eontimer.ui.timers.gen4.Gen4TimerView;
import com.github.dylmeadows.eontimer.ui.timers.gen5.Gen5TimerView;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class EonTimerView extends GridPane {

    private final BooleanProperty tabDisable;

    private final ReadOnlyObjectWrapper<TimerType> timerType;

    public EonTimerView() {
        initComponents();

        tabDisable = new SimpleBooleanProperty(false);
        ReadOnlyObjectProperty<Tab> selectedTab = timerTabPane.getSelectionModel().selectedItemProperty();
        gen5Tab.disableProperty().bind(tabDisable.and(selectedTab.isNotEqualTo(gen5Tab)));
        gen4Tab.disableProperty().bind(tabDisable.and(selectedTab.isNotEqualTo(gen4Tab)));
        gen3Tab.disableProperty().bind(tabDisable.and(selectedTab.isNotEqualTo(gen3Tab)));
        customTab.disableProperty().bind(tabDisable.and(selectedTab.isNotEqualTo(customTab)));

        timerType = new ReadOnlyObjectWrapper<>();
        timerType.bind(Bindings.createObjectBinding(this::computeTimerType,
                timerTabPane.getSelectionModel().selectedItemProperty()));
    }

    private TimerType computeTimerType() {
        Tab selected = timerTabPane.getSelectionModel().getSelectedItem();
        if (selected.equals(gen5Tab)) return TimerType.GEN5;
        if (selected.equals(gen4Tab)) return TimerType.GEN4;
        if (selected.equals(gen3Tab)) return TimerType.GEN3;
        if (selected.equals(customTab)) return TimerType.CUSTOM;
        return null;
    }

    public String getCurrentStageDisplayText() {
        return currentStageDisplayLbl.getText();
    }

    public StringProperty currentStageDisplayTextProperty() {
        return currentStageDisplayLbl.textProperty();
    }

    public void setCurrentStageDisplayText(String currentStageDisplayText) {
        currentStageDisplayLbl.setText(currentStageDisplayText);
    }

    public Paint getCurrentStageDisplayLblBackgroundColor() {
        return currentStageDisplayLblBackground.getFill();
    }

    public ObjectProperty<Paint> currentStageDisplayLblBackgroundColorProperty() {
        return currentStageDisplayLblBackground.fillProperty();
    }

    public void setCurrentStageDisplayLblBackgroundColor(Paint currentStageDisplayLblBackgroundColor) {
        currentStageDisplayLblBackground.setFill(currentStageDisplayLblBackgroundColor);
    }

    public String getMinutesBeforeTargetValueText() {
        return minutesBeforeTargetValueLbl.getText();
    }

    public StringProperty minutesBeforeTargetValueTextProperty() {
        return minutesBeforeTargetValueLbl.textProperty();
    }

    public void setMinutesBeforeTargetValueText(String minutesBeforeTargetValueText) {
        minutesBeforeTargetValueLbl.setText(minutesBeforeTargetValueText);
    }

    public String getNextStageDisplayText() {
        return nextStageDisplayLbl.getText();
    }

    public StringProperty nextStageDisplayTextProperty() {
        return nextStageDisplayLbl.textProperty();
    }

    public void setNextStageDisplayText(String text) {
        nextStageDisplayLbl.setText(text);
    }

    public boolean isTabDisable() {
        return tabDisable.get();
    }

    public BooleanProperty tabDisableProperty() {
        return tabDisable;
    }

    public void setTabDisable(boolean tabDisable) {
        this.tabDisable.set(tabDisable);
    }

    public TimerType getTimerType() {
        return timerType.get();
    }

    public ReadOnlyObjectProperty<TimerType> timerTypeProperty() {
        return timerType.getReadOnlyProperty();
    }

    public boolean isSettingsBtnDisabled() {
        return settingsBtn.isDisabled();
    }

    public BooleanProperty settingsBtnDisableProperty() {
        return settingsBtn.disableProperty();
    }

    public void setSettingsBtnDisable(boolean disable) {
        settingsBtn.setDisable(disable);
    }

    public boolean isUpdateBtnDisabled() {
        return updateBtn.isDisabled();
    }

    public BooleanProperty updateBtnDisableProperty() {
        return updateBtn.disableProperty();
    }

    public void setUpdateBtnDisable(boolean disable) {
        updateBtn.setDisable(disable);
    }

    public String getTimerBtnText() {
        return timerBtn.getText();
    }

    public StringProperty timerBtnTextProperty() {
        return timerBtn.textProperty();
    }

    public void setTimerBtnText(String timerBtnText) {
        timerBtn.setText(timerBtnText);
    }

    public void setSettingsBtnOnAction(EventHandler<ActionEvent> settingsBtnOnAction) {
        settingsBtn.setOnAction(settingsBtnOnAction);
    }

    public void setUpdateBtnOnAction(EventHandler<ActionEvent> updateBtnOnAction) {
        updateBtn.setOnAction(updateBtnOnAction);
    }

    public void setTimerBtnOnAction(EventHandler<ActionEvent> timerBtnOnAction) {
        timerBtn.setOnAction(timerBtnOnAction);
    }

    public Gen5TimerView getGen5TimerView() {
        return gen5TimerView;
    }

    public Gen4TimerView getGen4TimerView() {
        return gen4TimerView;
    }

    public Gen3TimerView getGen3TimerView() {
        return gen3TimerView;
    }

    public CustomTimerView getCustomTimerView() {
        return customTimerView;
    }

    public EonTimerSettingsView getEonTimerSettingsView() {
        return eonTimerSettingsView;
    }

    private void initComponents() {
        ResourceBundle bundle = ResourceBundles.getBundle(EonTimerView.class);
        displayPane = new GridPane();
        currentDisplayPane = new AnchorPane();
        currentDisplayStackPane = new StackPane();
        currentStageDisplayLbl = new Label();
        currentStageDisplayLblBackground = new Rectangle();
        minutesBeforeTargetLbl = new Label();
        minutesBeforeTargetValueLbl = new Label();
        nextStageDisplayLbl = new Label();
        timerTabPane = new TabPane();
        gen5Tab = new Tab();
        gen5TimerView = new Gen5TimerView();
        gen4Tab = new Tab();
        gen4TimerView = new Gen4TimerView();
        gen3Tab = new Tab();
        gen3TimerView = new Gen3TimerView();
        customTab = new Tab();
        customTimerView = new CustomTimerView();
        settingsBtn = new Button();
        buttonControlPane = new HBox();
        updateBtn = new Button();
        timerBtn = new Button();
        eonTimerSettingsView = new EonTimerSettingsView();

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
            column0.setMaxWidth(Double.MAX_VALUE);
            column0.setPrefWidth(200);
            getColumnConstraints().add(column0);

            // ===== column1 =====
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setHalignment(HPos.LEFT);
            column1.setHgrow(Priority.ALWAYS);
            column1.setMaxWidth(Double.MAX_VALUE);
            column1.setPrefWidth(250);
            getColumnConstraints().add(column1);
        }
        // ===== RowConstraints =====
        {
            // ===== row0 =====
            RowConstraints row0 = new RowConstraints();
            row0.setVgrow(Priority.ALWAYS);
            row0.setValignment(VPos.TOP);
            getRowConstraints().add(row0);

            // ===== row1 =====
            RowConstraints row1 = new RowConstraints();
            row1.setVgrow(Priority.NEVER);
            getRowConstraints().add(row1);
        }

        // ===== displayPane =====
        {
            displayPane.setHgap(5);
            displayPane.getStyleClass().add("panel");
            displayPane.getStyleClass().add("themeable");
            displayPane.setMaxHeight(Double.NEGATIVE_INFINITY);
            displayPane.setPadding(new Insets(10));

            // ===== ColumnConstraints =====
            {
                // ===== column0 =====
                ColumnConstraints column0 = new ColumnConstraints();
                column0.setHalignment(HPos.LEFT);
                column0.setHgrow(Priority.NEVER);
                displayPane.getColumnConstraints().add(column0);

                // ===== column1 =====
                ColumnConstraints column1 = new ColumnConstraints();
                column1.setHalignment(HPos.LEFT);
                column1.setHgrow(Priority.ALWAYS);
                displayPane.getColumnConstraints().add(column1);
            }
            // ===== RowConstraints =====
            {
                // ===== row0 =====
                RowConstraints row0 = new RowConstraints();
                row0.setVgrow(Priority.NEVER);
                displayPane.getRowConstraints().add(row0);

                // ===== row1 =====
                RowConstraints row1 = new RowConstraints();
                row1.setVgrow(Priority.NEVER);
                displayPane.getRowConstraints().add(row1);

                // ===== row2 =====
                RowConstraints row2 = new RowConstraints();
                row2.setVgrow(Priority.NEVER);
                displayPane.getRowConstraints().add(row2);
            }

            // ===== currentDisplayPane =====
            {
                // ===== currentDisplayStackPane =====
                {
                    AnchorPane.setTopAnchor(currentDisplayStackPane, 0.0);
                    AnchorPane.setBottomAnchor(currentDisplayStackPane, 0.0);
                    AnchorPane.setLeftAnchor(currentDisplayStackPane, 0.0);
                    AnchorPane.setRightAnchor(currentDisplayStackPane, 0.0);

                    // ===== currentStageDisplayLblBackground =====
                    currentStageDisplayLblBackground.setFill(Color.TRANSPARENT);
                    currentStageDisplayLblBackground.widthProperty().bind(currentDisplayStackPane.widthProperty());
                    currentStageDisplayLblBackground.heightProperty().bind(currentDisplayStackPane.heightProperty());
                    currentDisplayStackPane.getChildren().add(currentStageDisplayLblBackground);

                    // ===== currentStageDisplayLbl =====
                    currentStageDisplayLbl.setText("0:00");
                    currentStageDisplayLbl.setFont(Font.font(62));
                    currentStageDisplayLbl.getStyleClass().add("themeable");
                    currentStageDisplayLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    currentStageDisplayLbl.setPadding(new Insets(-10, 0, -10, 0));
                    currentDisplayStackPane.getChildren().add(currentStageDisplayLbl);
                }
                currentDisplayPane.getChildren().add(currentDisplayStackPane);
            }
            displayPane.add(currentDisplayPane, 0, 0, 2, 1);

            // ===== minutesBeforeTargetLbl =====
            minutesBeforeTargetLbl.setText(bundle.getString("EonTimerView.minutesBeforeTargetLbl.text"));
            minutesBeforeTargetLbl.getStyleClass().add("themeable");
            minutesBeforeTargetLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            displayPane.add(minutesBeforeTargetLbl, 0, 1, 1, 1);

            // ===== minutesBeforeTargetValueLbl =====
            minutesBeforeTargetValueLbl.setText("0");
            minutesBeforeTargetValueLbl.getStyleClass().add("themeable");
            minutesBeforeTargetValueLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            displayPane.add(minutesBeforeTargetValueLbl, 1, 1, 1, 1);

            // ===== nextStageDisplayLbl =====
            nextStageDisplayLbl.setText("0:00");
            nextStageDisplayLbl.getStyleClass().add("themeable");
            nextStageDisplayLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            displayPane.add(nextStageDisplayLbl, 0, 2, 2, 1);
        }
        add(displayPane, 0, 0, 1, 1);

        // ===== timerTabPane =====
        {
            timerTabPane.getStyleClass().add("floating");
            timerTabPane.getStyleClass().add("themeable");
            timerTabPane.getStyleClass().add("large-tabs");
            timerTabPane.getStyleClass().add("bold-labels");
            timerTabPane.setPadding(new Insets(-5, 0, 0, 0));
            timerTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            // ===== gen5Tab =====
            gen5Tab.setText(bundle.getString("EonTimerView.gen5Tab.text"));
            gen5Tab.setClosable(false);
            gen5Tab.setContent(gen5TimerView);
            timerTabPane.getTabs().add(gen5Tab);

            // ===== gen4Tab =====
            gen4Tab.setText(bundle.getString("EonTimerView.gen4Tab.text"));
            gen4Tab.setClosable(false);
            gen4Tab.setContent(gen4TimerView);
            timerTabPane.getTabs().add(gen4Tab);

            // ===== gen3Tab =====
            gen3Tab.setText(bundle.getString("EonTimerView.gen3Tab.text"));
            gen3Tab.setClosable(false);
            gen3Tab.setContent(gen3TimerView);
            timerTabPane.getTabs().add(gen3Tab);

            // ===== customTab =====
            customTab.setText(bundle.getString("EonTimerView.customTab.text"));
            customTab.setClosable(false);
            customTab.setContent(customTimerView);
            timerTabPane.getTabs().add(customTab);
        }
        add(timerTabPane, 1, 0, 1, 1);

        // ===== settingsBtn =====
        settingsBtn.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.GEAR));
        settingsBtn.getStyleClass().add("themeable");
        add(settingsBtn, 0, 1, 1, 1);

        // ===== buttonControlPane =====
        {
            buttonControlPane.setSpacing(10);

            // ===== updateBtn =====
            updateBtn.setText(bundle.getString("EonTimerView.updateBtn.text"));
            updateBtn.getStyleClass().add("themeable");
            HBox.setHgrow(updateBtn, Priority.ALWAYS);
            updateBtn.setMaxWidth(Double.MAX_VALUE);
            buttonControlPane.getChildren().add(updateBtn);

            // ===== timerBtn =====
            timerBtn.setDefaultButton(true);
            timerBtn.setText(bundle.getString("EonTimerView.timerBtn.text.start"));
            timerBtn.getStyleClass().add("themeable");
            HBox.setHgrow(timerBtn, Priority.ALWAYS);
            timerBtn.setMaxWidth(Double.MAX_VALUE);
            buttonControlPane.getChildren().add(timerBtn);
        }
        add(buttonControlPane, 1, 1, 1, 1);
    }

    // region // EonTimerView - Variables declaration

    private GridPane displayPane;
    private AnchorPane currentDisplayPane;
    private StackPane currentDisplayStackPane;
    private Label currentStageDisplayLbl;
    private Rectangle currentStageDisplayLblBackground;
    private Label minutesBeforeTargetLbl;
    private Label minutesBeforeTargetValueLbl;
    private Label nextStageDisplayLbl;
    private TabPane timerTabPane;
    private Tab gen5Tab;
    private Gen5TimerView gen5TimerView;
    private Tab gen4Tab;
    private Gen4TimerView gen4TimerView;
    private Tab gen3Tab;
    private Gen3TimerView gen3TimerView;
    private Tab customTab;
    private CustomTimerView customTimerView;
    private Button settingsBtn;
    private HBox buttonControlPane;
    private Button updateBtn;
    private Button timerBtn;
    private EonTimerSettingsView eonTimerSettingsView;

    // endregion

    public enum TimerType {
        GEN5, GEN4, GEN3, CUSTOM
    }
}
