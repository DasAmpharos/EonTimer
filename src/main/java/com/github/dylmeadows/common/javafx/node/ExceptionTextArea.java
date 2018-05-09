package com.github.dylmeadows.common.javafx.node;

import com.github.dylmeadows.common.lang.Exceptions;
import javafx.scene.control.TextArea;

public class ExceptionTextArea {

    private ExceptionTextArea() {
    }

    public static TextArea getTextArea(Throwable t) {
        TextArea area = new TextArea(Exceptions.getStackTraceString(t));
        area.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        area.setEditable(false);
        return area;
    }
}
