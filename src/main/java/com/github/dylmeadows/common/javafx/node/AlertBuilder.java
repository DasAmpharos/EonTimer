package com.github.dylmeadows.common.javafx.node;

import com.github.dylmeadows.eontimer.util.Arrays;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Accessors(chain = true)
@Setter(value = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class AlertBuilder {

    private final Alert.AlertType messageType;

    private String title;

    private Modality modality = Modality.APPLICATION_MODAL;

    private String headerText;

    @Setter(AccessLevel.NONE)
    private Node contentNode;

    @Setter(AccessLevel.NONE)
    private String contentText;

    private Node expandableContent;

    private ButtonType[] options = Arrays.of(ButtonType.OK);

    private ButtonType defaultOption = ButtonType.OK;
    
    static AlertBuilder newBuilder(Alert.AlertType messageType) {
        return new AlertBuilder(messageType);
    }

    AlertBuilder setContent(Node content) {
        this.contentNode = content;
        this.contentText = null;
        return this;
    }

    AlertBuilder setContent(String content) {
        this.contentText = content;
        this.contentNode = null;
        return this;
    }

    Alert build() {
        Alert alert = new Alert(messageType);
        DialogPane dialogPane = alert.getDialogPane();

        alert.setTitle(title);
        alert.initModality(modality);
        alert.setHeaderText(headerText);

        Optional.ofNullable(contentText)
                .ifPresent(dialogPane::setContentText);
        Optional.ofNullable(contentNode)
                .ifPresent(dialogPane::setContent);
        Optional.ofNullable(expandableContent)
                .ifPresent(dialogPane::setExpandableContent);

        dialogPane.getButtonTypes().setAll(options);
        dialogPane.getButtonTypes()
                .stream()
                .collect(Collectors.toMap(identity(), dialogPane::lookupButton))
                .forEach((key, value) -> {
                    boolean isDefaultOption = key.equals(defaultOption);
                    Optional.of(value)
                            .filter(Button.class::isInstance)
                            .map(Button.class::cast)
                            .ifPresent(button -> button.setDefaultButton(isDefaultOption));
                });

        return alert;
    }
}
