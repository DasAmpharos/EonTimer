package com.github.dylmeadows.common.javafx.node;

import com.github.dylmeadows.eontimer.util.extension.ResourceBundleExtensions;
import com.google.common.base.Throwables;
import io.reactivex.Observable;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class FxOptionPane {

    public final ButtonType EXIT = new ButtonType("EXIT");

    private final ButtonType DEFAULT_OPTION = ButtonType.OK;
    private final ButtonType[] DEFAULT_OPTIONS = new ButtonType[]{ButtonType.OK};
    private final Alert.AlertType DEFAULT_CONFIRM_MESSAGE_TYPE = Alert.AlertType.CONFIRMATION;
    private final Alert.AlertType DEFAULT_EXCEPTION_MESSAGE_TYPE = Alert.AlertType.ERROR;

    public Observable<ButtonType> showConfirmDialog(Node content) {
        return buildAndShow(content, null, DEFAULT_OPTIONS, DEFAULT_OPTION, DEFAULT_CONFIRM_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showConfirmDialog(Node content, String title) {
        return buildAndShow(content, title, DEFAULT_OPTIONS, DEFAULT_OPTION, DEFAULT_CONFIRM_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showConfirmDialog(Node content, String title, ButtonType[] options) {
        return buildAndShow(content, title, options, null, DEFAULT_CONFIRM_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showConfirmDialog(Node content, String title, ButtonType[] options, ButtonType defaultOption) {
        return buildAndShow(content, title, options, defaultOption, DEFAULT_CONFIRM_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showConfirmDialog(Node content, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        return buildAndShow(content, title, options, defaultOption, messageType);
    }

    public Observable<ButtonType> showConfirmDialog(String content) {
        return buildAndShow(content, null, DEFAULT_OPTIONS, DEFAULT_OPTION, DEFAULT_CONFIRM_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showConfirmDialog(String content, String title) {
        return buildAndShow(content, title, DEFAULT_OPTIONS, DEFAULT_OPTION, DEFAULT_CONFIRM_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showConfirmDialog(String content, String title, ButtonType[] options) {
        return buildAndShow(content, title, options, null, DEFAULT_CONFIRM_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showConfirmDialog(String content, String title, ButtonType[] options, ButtonType defaultOption) {
        return buildAndShow(content, title, options, defaultOption, DEFAULT_CONFIRM_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showConfirmDialog(String content, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        return buildAndShow(content, title, options, defaultOption, messageType);
    }

    public Observable<ButtonType> showExceptionDialog(Throwable t) {
        return buildAndShow(null, t, null, DEFAULT_OPTIONS, DEFAULT_OPTION, DEFAULT_EXCEPTION_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showExceptionDialog(Throwable t, String title) {
        return buildAndShow(null, t, title, DEFAULT_OPTIONS, DEFAULT_OPTION, DEFAULT_EXCEPTION_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showExceptionDialog(Throwable t, String title, ButtonType[] options) {
        return buildAndShow(null, t, title, options, null, DEFAULT_EXCEPTION_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showExceptionDialog(Throwable t, String title, ButtonType[] options, ButtonType defaultOption) {
        return buildAndShow(null, t, title, options, defaultOption, DEFAULT_EXCEPTION_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showExceptionDialog(Throwable t, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        return buildAndShow(null, t, title, options, defaultOption, messageType);
    }

    public Observable<ButtonType> showExceptionDialog(String message, Throwable t) {
        return buildAndShow(message, t, null, DEFAULT_OPTIONS, DEFAULT_OPTION, DEFAULT_EXCEPTION_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showExceptionDialog(String message, Throwable t, String title) {
        return buildAndShow(message, t, title, DEFAULT_OPTIONS, DEFAULT_OPTION, DEFAULT_EXCEPTION_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showExceptionDialog(String message, Throwable t, String title, ButtonType[] options) {
        return buildAndShow(message, t, title, options, null, DEFAULT_EXCEPTION_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showExceptionDialog(String message, Throwable t, String title, ButtonType[] options, ButtonType defaultOption) {
        return buildAndShow(message, t, title, options, defaultOption, DEFAULT_EXCEPTION_MESSAGE_TYPE);
    }

    public Observable<ButtonType> showExceptionDialog(String message, Throwable t, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        return buildAndShow(message, t, title, options, defaultOption, messageType);
    }

    private Observable<ButtonType> buildAndShow(Node content, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        return show(buildAlert(messageType, title, options, defaultOption)
                .setContent(content)
                .build());
    }

    private Observable<ButtonType> buildAndShow(String content, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        return show(buildAlert(messageType, title, options, defaultOption)
                .setContent(content)
                .build());
    }

    private Observable<ButtonType> buildAndShow(String content, Throwable t, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        return show(buildAlert(messageType, title, options, defaultOption)
                .setContent(Optional.ofNullable(content)
                        .orElse(t.getLocalizedMessage()))
                .setExpandableContent(Optional.ofNullable(t)
                        .map(FxOptionPane::getExceptionTextArea)
                        .orElse(null))
                .build());
    }

    private Observable<ButtonType> show(Dialog<ButtonType> dialog) {
        return Observable.create(emitter -> Platform.runLater(() -> {
            emitter.onNext(dialog.showAndWait().orElse(EXIT));
            emitter.onComplete();
        }));
    }

    private AlertBuilder buildAlert(Alert.AlertType messageType, String title, ButtonType[] options, ButtonType defaultOption) {
        return AlertBuilder.newBuilder(messageType)
                .setTitle(Optional.ofNullable(title)
                        .orElse(getTitle(messageType)))
                .setOptions(Optional.ofNullable(options)
                        .orElse(DEFAULT_OPTIONS))
                .setDefaultOption(Optional.ofNullable(defaultOption)
                        .orElse(DEFAULT_OPTION));
    }

    private String getTitle(Alert.AlertType messageType) {
        return Optional.ofNullable(messageType)
                .map(Alert.AlertType::name)
                .map(FxOptionPane::getResourceString)
                .orElse(null);
    }

    private String getResourceString(String key) {
        return ResourceBundleExtensions.getBundle(FxOptionPane.class).getString(key);
    }

    private TextArea getExceptionTextArea(Throwable t) {
        TextArea area = new TextArea(Throwables.getStackTraceAsString(t));
        area.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        area.setEditable(false);
        return area;
    }
}
