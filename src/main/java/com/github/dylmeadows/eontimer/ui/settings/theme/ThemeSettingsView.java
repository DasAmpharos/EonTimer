package com.github.dylmeadows.eontimer.ui.settings.theme;

import com.github.dylmeadows.eontimer.model.ThemeBackgroundMode;
import com.github.dylmeadows.eontimer.util.extension.ResourceBundleExtensions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.util.ResourceBundle;

public class ThemeSettingsView extends ScrollPane {

    public ThemeSettingsView() {
        initComponents();
    }

//    private void onBackgroundImageBtnAction() {
//        File file = new File(getModel().getBackgroundImage());
//        ResourceBundle bundle = ResourceBundleExtensions.getBundle(ThemeSettingsView.class);
//
//        FileChooser chooser = new FileChooser();
//        FileChooser.ExtensionFilter imageExtensionFilter = new FileChooser.ExtensionFilter(
//                bundle.getString("BackgroundImageFileChooser.ExtensionFilter.text"),
//                "*.jpg", "*.JPG", "*.jpeg", "*.JPEG", "*.png", "*.PNG"
//        );
//        chooser.getExtensionFilters().add(imageExtensionFilter);
//        if (file.getParentFile() != null)
//            chooser.setInitialDirectory(file.getParentFile());
//        file = chooser.showOpenDialog(null);
//        if (file != null)
//            getModel().setBackgroundImage(file.getPath());
//    }

    public ThemeBackgroundMode getBackgroundMode() {
        return backgroundModeField.getValue();
    }

    public ObjectProperty<ThemeBackgroundMode> backgroundModeProperty() {
        return backgroundModeField.valueProperty();
    }

    public void setBackgroundMode(ThemeBackgroundMode backgroundMode) {
        backgroundModeField.setValue(backgroundMode);
    }

    public Color getBackgroundColor() {
        return backgroundColorField.getValue();
    }

    public ObjectProperty<Color> backgroundColorProperty() {
        return backgroundColorField.valueProperty();
    }

    public void setBackgroundColor(Color backgroundColor) {
        backgroundColorField.setValue(backgroundColor);
    }

    public String getBackgroundImage() {
        return backgroundImageField.getText();
    }

    public StringProperty backgroundImageProperty() {
        return backgroundImageField.textProperty();
    }

    public void setBackgroundImage(String backgroundImage) {
        backgroundImageField.setText(backgroundImage);
    }

    public EventHandler<ActionEvent> getBackgroundImageFieldBtnOnAction() {
        return backgroundImageFieldBtn.getOnAction();
    }

    public ObjectProperty<EventHandler<ActionEvent>> backgroundImageFieldBtnOnActionProperty() {
        return backgroundImageFieldBtn.onActionProperty();
    }

    public void setBackgroundImageFieldBtnOnAction(EventHandler<ActionEvent> onAction) {
        backgroundImageFieldBtn.setOnAction(onAction);
    }

    public Color getPanelBaseColor() {
        return panelBaseColorField.getValue();
    }

    public ObjectProperty<Color> panelBaseColorProperty() {
        return panelBaseColorField.valueProperty();
    }

    public void setPanelBaseColor(Color panelBaseColor) {
        panelBaseColorField.setValue(panelBaseColor);
    }

    public double getPanelTransparency() {
        return panelTransparencyField.getValue();
    }

    public DoubleProperty panelTransparencyProperty() {
        return panelTransparencyField.valueProperty();
    }

    public void setPanelTransparency(double panelTransparency) {
        panelTransparencyField.setValue(panelTransparency);
    }

    public Color getControlBaseColor() {
        return controlBaseColorField.getValue();
    }

    public ObjectProperty<Color> controlBaseColorProperty() {
        return controlBaseColorField.valueProperty();
    }

    public void setControlBaseColor(Color controlBaseColor) {
        controlBaseColorField.setValue(controlBaseColor);
    }

    public Color getLabelTextColor() {
        return labelTextColorField.getValue();
    }

    public ObjectProperty<Color> labelTextColorProperty() {
        return labelTextColorField.valueProperty();
    }

    public void setLabelTextColor(Color labelTextColor) {
        labelTextColorField.setValue(labelTextColor);
    }

    public Color getAccentColor() {
        return accentColorField.getValue();
    }

    public ObjectProperty<Color> accentColorProperty() {
        return accentColorField.valueProperty();
    }

    public void setAccentColor(Color accentColor) {
        accentColorField.setValue(accentColor);
    }

    private void initComponents() {
        ResourceBundle bundle = ResourceBundleExtensions.getBundle(ThemeSettingsView.class);
        gridPane = new GridPane();
        backgroundModeLbl = new Label();
        backgroundModeField = new ChoiceBox<>();
        backgroundColorLbl = new Label();
        backgroundColorField = new ColorPicker();
        backgroundImageLbl = new Label();
        backgroundImageFieldLayout = new HBox();
        backgroundImageField = new TextField();
        backgroundImageFieldBtn = new Button();
        panelBaseColorLbl = new Label();
        panelBaseColorField = new ColorPicker();
        panelTransparencyLbl = new Label();
        panelTransparencyField = new Slider();
        controlBaseColorLbl = new Label();
        controlBaseColorField = new ColorPicker();
        labelTextColorLbl = new Label();
        labelTextColorField = new ColorPicker();
        accentColorLbl = new Label();
        accentColorField = new ColorPicker();


        // ===== this =====
        setFitToWidth(true);
        setMinViewportHeight(185);
        setPrefViewportHeight(185);
        setVbarPolicy(ScrollBarPolicy.ALWAYS);

        // ===== gridPane =====
        {
            gridPane.setPadding(new Insets(10));
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            // ===== ColumnConstraints =====
            {
                // ===== column0 =====
                ColumnConstraints column0 = new ColumnConstraints();
                column0.setHgrow(Priority.NEVER);
                gridPane.getColumnConstraints().add(column0);

                // ===== column1 =====
                ColumnConstraints column1 = new ColumnConstraints();
                column1.setHgrow(Priority.ALWAYS);
                gridPane.getColumnConstraints().add(column1);
            }

            // ===== backgroundModeLbl =====
            backgroundModeLbl.setText(bundle.getString("ThemeSettingsView.backgroundModeLbl.text"));
            backgroundModeLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gridPane.add(backgroundModeLbl, 0, 0);

            // ===== backgroundModeField =====
            backgroundModeField.setItems(FXCollections.observableArrayList(ThemeBackgroundMode.values()));
            backgroundModeField.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(backgroundModeField, 1, 0);

            // ===== backgroundColorLbl =====
            backgroundColorLbl.setText(bundle.getString("ThemeSettingsView.backgroundColorLbl.text"));
            backgroundColorLbl.disableProperty().bind(
                    backgroundModeField.valueProperty().isNotEqualTo(ThemeBackgroundMode.COLOR));
            backgroundColorLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gridPane.add(backgroundColorLbl, 0, 1);

            // ===== backgroundColorField =====
            backgroundColorField.disableProperty().bind(
                    backgroundModeField.valueProperty().isNotEqualTo(ThemeBackgroundMode.COLOR));
            backgroundColorField.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(backgroundColorField, 1, 1);

            // ===== backgroundImageLbl =====
            backgroundImageLbl.setText(bundle.getString("ThemeSettingsView.backgroundImageLbl.text"));
            backgroundImageLbl.disableProperty().bind(
                    backgroundModeField.valueProperty().isNotEqualTo(ThemeBackgroundMode.IMAGE));
            backgroundImageLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gridPane.add(backgroundImageLbl, 0, 2);

            // ===== backgroundImageFieldLayout =====
            {
                backgroundImageFieldLayout.disableProperty().bind(
                        backgroundModeField.valueProperty().isNotEqualTo(ThemeBackgroundMode.IMAGE));

                // ===== backgroundImageField =====
                backgroundImageField.setEditable(false);
                backgroundImageField.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(backgroundImageField, Priority.ALWAYS);
                backgroundImageFieldLayout.getChildren().add(backgroundImageField);

                // ===== backgroundImageFieldBtn =====
                backgroundImageFieldBtn.setText(bundle.getString("ThemeSettingsView.backgroundImageFieldBtn.text"));
                backgroundImageFieldBtn.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(backgroundImageFieldBtn, Priority.NEVER);
                backgroundImageFieldLayout.getChildren().add(backgroundImageFieldBtn);
            }
            gridPane.add(backgroundImageFieldLayout, 1, 2);

            // ===== panelBaseColorLbl =====
            panelBaseColorLbl.setText(bundle.getString("ThemeSettingsView.panelBaseColorLbl.text"));
            panelBaseColorLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gridPane.add(panelBaseColorLbl, 0, 3);

            // ===== panelBaseColorField =====
            panelBaseColorField.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(panelBaseColorField, 1, 3);

            // ===== panelTransparencyLbl =====
            panelTransparencyLbl.setText(bundle.getString("ThemeSettingsView.panelTransparencyLbl.text"));
            panelTransparencyLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gridPane.add(panelTransparencyLbl, 0, 4);

            // ===== panelTransparencyField =====
            panelTransparencyField.setMin(0.0);
            panelTransparencyField.setMax(1.0);
            panelTransparencyField.setBlockIncrement(0.1);
            panelTransparencyField.setShowTickMarks(true);
            panelTransparencyField.setMajorTickUnit(0.25);
            panelTransparencyField.setMinorTickCount(4);
            panelTransparencyField.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(panelTransparencyField, 1, 4);

            // ===== controlBaseColorLbl =====
            controlBaseColorLbl.setText(bundle.getString("ThemeSettingsView.controlBaseColorLbl.text"));
            controlBaseColorLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gridPane.add(controlBaseColorLbl, 0, 5);

            // ===== controlBaseColorField =====
            controlBaseColorField.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(controlBaseColorField, 1, 5);

            // ===== labelTextColorLbl =====
            labelTextColorLbl.setText(bundle.getString("ThemeSettingsView.labelTextColorLbl.text"));
            labelTextColorLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gridPane.add(labelTextColorLbl, 0, 6);

            // ===== labelTextColorField =====
            labelTextColorField.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(labelTextColorField, 1, 6);

            // ===== accentColorLbl =====
            accentColorLbl.setText(bundle.getString("ThemeSettingsView.accentColorLbl.text"));
            accentColorLbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gridPane.add(accentColorLbl, 0, 7);

            // ===== accentColorField =====
            accentColorField.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(accentColorField, 1, 7);
        }
        setContent(gridPane);
    }

    // region // ThemeSettingsView - Variables declaration

    private GridPane gridPane;
    private Label backgroundModeLbl;
    private ChoiceBox<ThemeBackgroundMode> backgroundModeField;
    private Label backgroundColorLbl;
    private ColorPicker backgroundColorField;
    private Label backgroundImageLbl;
    private HBox backgroundImageFieldLayout;
    private TextField backgroundImageField;
    private Button backgroundImageFieldBtn;
    private Label panelBaseColorLbl;
    private ColorPicker panelBaseColorField;
    private Label panelTransparencyLbl;
    private Slider panelTransparencyField;
    private Label controlBaseColorLbl;
    private ColorPicker controlBaseColorField;
    private Label labelTextColorLbl;
    private ColorPicker labelTextColorField;
    private Label accentColorLbl;
    private ColorPicker accentColorField;

    // endregion
}
