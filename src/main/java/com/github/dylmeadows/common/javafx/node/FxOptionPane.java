package com.github.dylmeadows.common.javafx.node;

import com.github.dylmeadows.common.util.ResourceBundles;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;

import java.util.concurrent.CountDownLatch;

public class FxOptionPane {

    public static final ButtonType EXIT = new ButtonType("EXIT");

    private static final ButtonType[] DEFAULT_OPTIONS = new ButtonType[]{ButtonType.OK};

    private static final ButtonType DEFAULT_DEFAULT_OPTION = ButtonType.OK;

    private static final Alert.AlertType DEFAULT_CONFIRM_MESSAGE_TYPE = Alert.AlertType.CONFIRMATION;

    private static final Alert.AlertType DEFAULT_EXCEPTION_MESSAGE_TYPE = Alert.AlertType.ERROR;

    public static ButtonType showConfirmDialog(Object message) {
        return showConfirmDialog(message, null);
    }

    public static ButtonType showConfirmDialog(Object message, String title) {
        return showConfirmDialog(message, title, null);
    }

    public static ButtonType showConfirmDialog(Object message, String title, ButtonType[] options) {
        return showConfirmDialog(message, title, options, null);
    }

    public static ButtonType showConfirmDialog(Object message, String title, ButtonType[] options, ButtonType defaultOption) {
        return showConfirmDialog(message, title, options, defaultOption, DEFAULT_CONFIRM_MESSAGE_TYPE);
    }

    public static ButtonType showConfirmDialog(Object message, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        return showConfirmDialogImpl(message, title, options, defaultOption, messageType);
    }

    private static ButtonType showConfirmDialogImpl(Object message, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        Alert alert = new Alert(messageType);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
        if (message instanceof Node) {
            alert.getDialogPane().setContent((Node) message);
        } else {
            alert.setContentText(message.toString());
        }
        if (title == null && messageType != null)
            title = ResourceBundles.getBundle(FxOptionPane.class).getString(messageType.name());
        alert.setTitle(title);
        if (options == null && defaultOption == null) {
            options = DEFAULT_OPTIONS;
            defaultOption = DEFAULT_DEFAULT_OPTION;
        }
        alert.getDialogPane().getButtonTypes().setAll(options);
        if (options != null) {
            if (defaultOption != null && alert.getDialogPane().getButtonTypes().contains(defaultOption)) {
                setDefaultOption(alert, defaultOption);
            }
        }
        return showAndWait(alert, EXIT);
    }

    public static ButtonType showExceptionDialog(Throwable t) {
        return showExceptionDialog(t, null);
    }

    public static ButtonType showExceptionDialog(Throwable t, String title) {
        return showExceptionDialog(t, title, null);
    }

    public static ButtonType showExceptionDialog(Throwable t, String title, ButtonType[] options) {
        return showExceptionDialog(t, title, options, null);
    }

    public static ButtonType showExceptionDialog(Throwable t, String title, ButtonType[] options, ButtonType defaultOption) {
        return showExceptionDialog(t, title, options, defaultOption, DEFAULT_EXCEPTION_MESSAGE_TYPE);
    }

    public static ButtonType showExceptionDialog(Throwable t, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        return showExceptionDialogImpl(t, title, options, defaultOption, messageType);
    }

    private static ButtonType showExceptionDialogImpl(Throwable t, String title, ButtonType[] options, ButtonType defaultOption, Alert.AlertType messageType) {
        Alert alert = new Alert(messageType);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(t.getLocalizedMessage());
        alert.getDialogPane().setExpandableContent(ExceptionTextArea.getTextArea(t));
        if (title == null && messageType != null)
            title = ResourceBundles.getBundle(FxOptionPane.class).getString(messageType.name());
        alert.setTitle(title);
        if (options == null && defaultOption == null) {
            options = DEFAULT_OPTIONS;
            defaultOption = DEFAULT_DEFAULT_OPTION;
        }
        alert.getDialogPane().getButtonTypes().setAll(options);
        if (options != null) {
            if (defaultOption != null && alert.getDialogPane().getButtonTypes().contains(defaultOption)) {
                setDefaultOption(alert, defaultOption);
            }
        }
        return showAndWait(alert, EXIT);
    }

    private static void setDefaultOption(Dialog dialog, ButtonType defaultOption) {
        for (ButtonType option : dialog.getDialogPane().getButtonTypes()) {
            Button button = (Button) dialog.getDialogPane().lookupButton(option);
            button.setDefaultButton(option.equals(defaultOption));
        }
    }

    private static void show(Dialog dialog) {
        if (Platform.isFxApplicationThread()) {
            dialog.show();
        } else {
            Platform.runLater(dialog::show);
        }
    }

    private static ButtonType showAndWait(Dialog<ButtonType> dialog, ButtonType defaultValue) {
        if (Platform.isFxApplicationThread()) {
            return dialog.showAndWait().orElse(defaultValue);
        } else {
            final CountDownLatch latch = new CountDownLatch(1);
            Task<ButtonType> task = new Task<ButtonType>() {
                @Override
                protected ButtonType call() throws Exception {
                    try {
                        return dialog.showAndWait().orElse(defaultValue);
                    } finally {
                        latch.countDown();
                    }
                }
            };
            Platform.runLater(task);

            try {
                latch.await();
            } catch (InterruptedException e) {
            }

            return task.getValue();
        }
    }
}
