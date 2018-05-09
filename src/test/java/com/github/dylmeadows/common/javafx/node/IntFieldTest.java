package com.github.dylmeadows.common.javafx.node;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.CoreMatchers.is;

public class IntFieldTest extends ApplicationTest {

    private IntField intField;

    private static final int DEFAULT_VALUE = 0;
    private static final String DEFAULT_STRING = "";

    private static final String VALID_STRING_1 = "1234567890";
    private static final int VALID_VALUE_1 = 1234567890;
    private static final String VALID_STRING_2 = "-1234567890";
    private static final int VALID_VALUE_2 = -1234567890;
    private static final String VALID_STRING_3 = "-";
    private static final int VALID_VALUE_3 = DEFAULT_VALUE;

    private static final String INVALID_STRING_1 = "abcdefghijklmnopqrstuvwxyz";
    private static final String INVALID_STRING_2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String INVALID_STRING_3 = ",./;'[]\\<>?:\"{}|";
    private static final String INVALID_STRING_4 = "`~!@#$%^&*()_+=";

    private static final String MIXED_STRING_1 = "123-456";
    private static final int MIXED_VALUE_1 = 123456;
    private static final String MIXED_STRING_2 = "-123-456";
    private static final int MIXED_VALUE_2 = -123456;

    @Override
    public void start(Stage stage) {
        intField = new IntField();
        BorderPane layout = new BorderPane(intField);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();
    }

    @Before
    public void setup() {
        intField.clear();
    }

    @After
    public void cleanup() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void test_setValue_VALID_VALUE_1() {
        intField.setValue(VALID_VALUE_1);
        assertThat(VALID_STRING_1, VALID_VALUE_1);
    }

    @Test
    public void test_setValue_VALID_VALUE_2() {
        intField.setValue(VALID_VALUE_2);
        assertThat(VALID_STRING_2, VALID_VALUE_2);
    }

    @Test
    public void test_setText_VALID_STRING_1() {
        intField.setText(VALID_STRING_1);
        assertThat(VALID_STRING_1, VALID_VALUE_1);
    }

    @Test
    public void test_setText_VALID_STRING_2() {
        intField.setText(VALID_STRING_2);
        assertThat(VALID_STRING_2, VALID_VALUE_2);
    }

    @Test
    public void test_setText_VALID_STRING_3() {
        intField.setText(VALID_STRING_3);
        assertThat(VALID_STRING_3, VALID_VALUE_3);
    }

    @Test
    public void test_setText_INVALID_STRING_1() {
        intField.setText(INVALID_STRING_1);
        assertDefault();
    }

    @Test
    public void test_setText_INVALID_STRING_2() {
        intField.setText(INVALID_STRING_2);
        assertDefault();
    }

    @Test
    public void test_setText_INVALID_STRING_3() {
        intField.setText(INVALID_STRING_3);
        assertDefault();
    }

    @Test
    public void test_setText_INVALID_STRING_4() {
        intField.setText(INVALID_STRING_4);
        assertDefault();
    }

    @Test
    public void test_setText_MIXED_STRING_1() {
        intField.setText(MIXED_STRING_1);
        assertDefault();
    }

    @Test
    public void test_setText_MIXED_STRING_2() {
        intField.setText(MIXED_STRING_2);
        assertDefault();
    }

    @Test
    public void test_write_VALID_STRING_1() {
        clickOn(intField).write(VALID_STRING_1);
        assertThat(VALID_STRING_1, VALID_VALUE_1);
    }

    @Test
    public void test_write_VALID_STRING_2() {
        clickOn(intField).write(VALID_STRING_2);
        assertThat(VALID_STRING_2, VALID_VALUE_2);
    }

    @Test
    public void test_write_VALID_STRING_3() {
        clickOn(intField).write(VALID_STRING_3);
        assertThat(VALID_STRING_3, VALID_VALUE_3);
    }

    @Test
    public void test_write_INVALID_STRING_1() {
        clickOn(intField).write(INVALID_STRING_1);
        assertDefault();
    }

    @Test
    public void test_write_INVALID_STRING_2() {
        clickOn(intField).write(INVALID_STRING_2);
        assertDefault();
    }

    @Test
    public void test_write_INVALID_STRING_3() {
        clickOn(intField).write(INVALID_STRING_3);
        assertDefault();
    }

    @Test
    public void test_write_INVALID_STRING_4() {
        clickOn(intField).write(INVALID_STRING_4);
        assertDefault();
    }

    @Test
    public void test_write_MIXED_STRING_1() {
        clickOn(intField).write(MIXED_STRING_1);
        String string = Integer.toString(MIXED_VALUE_1);
        assertThat(string, MIXED_VALUE_1);
    }

    @Test
    public void test_write_MIXED_STRING_2() {
        clickOn(intField).write(MIXED_STRING_2);
        String string = Integer.toString(MIXED_VALUE_2);
        assertThat(string, MIXED_VALUE_2);
    }

    @Test
    public void test_showValueInPromptEnabled_setValue() {
        intField.setValue(VALID_VALUE_1);
        Assert.assertThat(intField.getPromptText(), is(VALID_STRING_1));
    }

    @Test
    public void test_showValueInPromptEnabled_setText() {
        intField.setText(VALID_STRING_1);
        Assert.assertThat(intField.getPromptText(), is(VALID_STRING_1));
    }

    @Test
    public void test_showValueInPromptEnabled_write() {
        clickOn(intField).write(VALID_STRING_1);
        Assert.assertThat(intField.getPromptText(), is(VALID_STRING_1));
    }

    @Test
    public void test_showValueInPromptDisabled_setValue() {
        intField.setShowValueInPrompt(false);
        intField.setValue(VALID_VALUE_1);
        Assert.assertThat(intField.getPromptText(), is(""));
    }

    @Test
    public void test_showValueInPromptDisabled_setText() {
        intField.setShowValueInPrompt(false);
        intField.setText(VALID_STRING_1);
        Assert.assertThat(intField.getPromptText(), is(""));
    }

    @Test
    public void test_showValueInPromptDisabled_write() {
        intField.setShowValueInPrompt(false);
        clickOn(intField).write(VALID_STRING_1);
        Assert.assertThat(intField.getPromptText(), is(""));
    }

    private void assertThat(String text, int value) {
        Assert.assertThat(intField.getText(), is(text));
        Assert.assertThat(intField.getValue(), is(value));
    }

    private void assertDefault() {
        assertThat(DEFAULT_STRING, DEFAULT_VALUE);
    }
}
