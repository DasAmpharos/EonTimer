package com.github.dylmeadows.common.javafx.node;

import com.google.common.primitives.Ints;
import com.sun.javafx.binding.BidirectionalBinding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.Optional;
import java.util.regex.Pattern;

public class IntField extends TextField {

    private IntegerProperty value;
    private BooleanProperty showValueInPrompt;

    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?\\d*$");
    private static final String STYLE_CLASS = "int-field";

    public IntField() {
        this(0);
    }

    public IntField(int value) {
        // initialize properties
        this.value = new SimpleIntegerProperty(value);
        this.showValueInPrompt = new SimpleBooleanProperty(true);
        // setup formatter to only allow integers
        setTextFormatter(new TextFormatter<>(this::validate));
        // add "int-field" style class
        getStyleClass().add(STYLE_CLASS);

        BidirectionalBinding.bind(textProperty(), this.value, new IntFieldStringConverter());
        promptTextProperty().bind(Bindings.createStringBinding(
                this::computePromptText, this.value, this.showValueInPrompt));
    }

    private TextFormatter.Change validate(TextFormatter.Change change) {
         return (INTEGER_PATTERN.matcher(change.getControlNewText()).matches()) ? change : null;
    }

    private String computePromptText() {
        return isShowValueInPrompt() ? Integer.toString(getValue()) : "";
    }

    public int getValue() {
        return value.get();
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    public boolean isShowValueInPrompt() {
        return showValueInPrompt.get();
    }

    public BooleanProperty showValueInPromptProperty() {
        return showValueInPrompt;
    }

    public void setShowValueInPrompt(boolean showValueInPrompt) {
        this.showValueInPrompt.set(showValueInPrompt);
    }

    class IntFieldStringConverter extends StringConverter<Number> {

        @Override
        public String toString(Number object) {
            return object.toString();
        }

        @Override
        public Integer fromString(String string) {
            return Optional.ofNullable(Ints.tryParse(string))
                    .orElse(getValue());
        }
    }
}
