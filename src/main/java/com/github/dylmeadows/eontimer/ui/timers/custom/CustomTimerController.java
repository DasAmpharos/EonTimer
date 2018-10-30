package com.github.dylmeadows.eontimer.ui.timers.custom;

public class CustomTimerController {

    /*private BooleanBinding valueAddBtnDisableBinding;
    private BooleanBinding valueRemoveBtnDisableBinding;
    private BooleanBinding valueMoveUpBtnDisableBinding;
    private BooleanBinding valueMoveDownBtnDisableBinding;

    public CustomTimerController(CustomTimerModel model, CustomTimerView view) {
        super(model, view);
    }

    @Override
    public void calibrate() {
    }

    @Override
    protected ObjectBinding<Timer> createTimerBinding(CustomTimerModel model) {
        return Bindings.createObjectBinding(this::createTimer,
                model.getStages());
    }

    public Timer createTimer() {
        CustomTimer timer = new CustomTimer();
        // TODO: refactor
        *//*if (getModel().getStages().isEmpty())
            timer.getStages().add(TimerConstants.NULL_TIME_SPAN);
        else
            timer.getStages().addAll(getModel().getStages());*//*
        return timer;
    }

    @Override
    protected void bind(CustomTimerModel model, CustomTimerView view) {
        super.bind(model, view);

        valueAddBtnDisableBinding = view.valueFieldTextProperty().isEmpty()
                .or(allFieldsDisableProperty());
        valueRemoveBtnDisableBinding = view.getSelectionModel().selectedIndexProperty().isEqualTo(-1)
                .or(allFieldsDisableProperty());
        valueMoveUpBtnDisableBinding = Bindings.createBooleanBinding(this::isSelectedFirstIndex,
                view.getSelectionModel().selectedIndexProperty(),
                view.getItems())
                .or(allFieldsDisableProperty());
        valueMoveDownBtnDisableBinding = Bindings.createBooleanBinding(this::isSelectedLastIndex,
                view.getSelectionModel().selectedIndexProperty(),
                view.getItems())
                .or(allFieldsDisableProperty());

        view.setItems(model.getStages());
        view.setOnListKeyPressed(this::onListKeyPressed);
        view.setOnValueFieldAction(this::onValueFieldAction);
        view.setOnValueAddBtnAction(this::onValueAddBtnAction);
        view.setOnValueRemoveBtnAction(this::onValueRemoveBtnAction);
        view.setOnValueMoveUpBtnAction(this::onValueMoveUpBtnAction);
        view.setOnValueMoveDownBtnAction(this::onValueMoveDownBtnAction);
        view.setOnRemoveMenuItemAction(this::onRemoveMenuItemAction);

        view.listDisableProperty().bind(allFieldsDisableProperty());
        view.valueFieldDisableProperty().bind(allFieldsDisableProperty());
        view.valueAddBtnDisableProperty().bind(valueAddBtnDisableBinding);
        view.valueRemoveBtnDisableProperty().bind(valueRemoveBtnDisableBinding);
        view.valueMoveUpBtnDisableProperty().bind(valueMoveUpBtnDisableBinding);
        view.valueMoveDownBtnDisableProperty().bind(valueMoveDownBtnDisableBinding);
    }

    @Override
    protected void unbind(CustomTimerModel model, CustomTimerView view) {
        super.unbind(model, view);

        valueAddBtnDisableBinding = null;
        valueRemoveBtnDisableBinding = null;
        valueMoveUpBtnDisableBinding = null;
        valueMoveDownBtnDisableBinding = null;

        view.setItems(null);
        view.setOnListKeyPressed(null);
        view.setOnValueFieldAction(null);
        view.setOnValueAddBtnAction(null);
        view.setOnValueRemoveBtnAction(null);
        view.setOnValueMoveUpBtnAction(null);
        view.setOnValueMoveDownBtnAction(null);
        view.setOnRemoveMenuItemAction(null);

        view.listDisableProperty().unbind();
        view.valueFieldDisableProperty().unbind();
        view.valueAddBtnDisableProperty().unbind();
        view.valueRemoveBtnDisableProperty().unbind();
        view.valueMoveUpBtnDisableProperty().unbind();
        view.valueMoveDownBtnDisableProperty().unbind();
    }

    private void onListKeyPressed(KeyEvent e) {
        if ((e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.DELETE))
            onDelete();
    }

    private void onValueFieldAction(ActionEvent e) {
        onAdd();
    }

    private void onValueAddBtnAction(ActionEvent e) {
        onAdd();
    }

    private void onValueRemoveBtnAction(ActionEvent e) {
        onDelete();
    }

    private void onValueMoveUpBtnAction(ActionEvent e) {
        onMoveUp();
    }

    private void onValueMoveDownBtnAction(ActionEvent e) {
        onMoveDown();
    }

    private void onRemoveMenuItemAction(ActionEvent e) {
        onDelete();
    }

    private void onAdd() {
        *//*if (!getView().getValueFieldText().isEmpty()) {
            int value = getView().getValueFieldValue();
            getView().getItems().add(Duration.ofMillis(value));
            getView().setValueFieldText("");
        }*//*
    }

    private void onDelete() {
        *//*SelectionModel<Duration> selectionModel = getView().getSelectionModel();
        if (selectionModel.getSelectedIndex() != -1) {
            ObservableList<Duration> items = getView().getItems();
            Duration item = selectionModel.getSelectedItem();
            items.remove(item);
        }*//*
    }

    private void onMoveUp() {
        *//*SelectionModel<Duration> selectionModel = getView().getSelectionModel();
        if (selectionModel.getSelectedIndex() != -1) {
            ObservableList<Duration> items = getView().getItems();
            int selectedIndex = selectionModel.getSelectedIndex();
            Duration item = items.get(selectedIndex);
            items.set(selectedIndex, items.get(selectedIndex - 1));
            items.set(selectedIndex - 1, item);
            selectionModel.select(selectedIndex - 1);
        }*//*
    }

    private void onMoveDown() {
        *//*SelectionModel<Duration> selectionModel = getView().getSelectionModel();
        if (selectionModel.getSelectedIndex() != -1) {
            ObservableList<Duration> items = getView().getItems();
            int selectedIndex = selectionModel.getSelectedIndex();
            Duration item = items.get(selectedIndex);
            items.set(selectedIndex, items.get(selectedIndex + 1));
            items.set(selectedIndex + 1, item);
            selectionModel.select(selectedIndex + 1);
        }*//*
    }

    private boolean hasSelection() {
//        return !getView().getSelectionModel().isEmpty();
        return false;
    }

    private boolean isSelectedFirstIndex() {
//        return hasSelection() && getView().getSelectionModel().getSelectedIndex() == 0;
        return false;
    }

    private boolean isSelectedLastIndex() {
//        return hasSelection() && getView().getSelectionModel().getSelectedIndex() == getView().getItems().size() - 1;
        return false;
    }*/
}
