package com.example.todofx.data;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.function.Consumer;

//todo date
public class AddItem {
    public TextField title;
    public TextField description;
    public DatePicker datePicker;
    public ComboBox<String> spinner;

    public CheckBox isRegular;

    public Pane addItemRoot(Consumer<TaskEntity> addItemCallback) {
        title = new TextField();
        title.setPromptText("Название");
        description = new TextField();
        description.setPrefHeight(50);
        description.setPromptText("Описание");

        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        spinner = new ComboBox<>();
        spinner.getItems().addAll("Ежедневно", "Еженедельно", "Ежемесячно");
        spinner.setVisible(false);

        isRegular = new CheckBox();
        isRegular.setOnAction(actionEvent -> {
            if (isRegular.isSelected()) spinner.setVisible(true);
            else spinner.setVisible(false);
        });

        Button add = new Button("Добавить");
        add.setOnAction(actionEvent -> {
            if (title.getText().isEmpty()) {
                showAlert();
            } else{
                addItem(addItemCallback);
            }

        });
        VBox root = new VBox(
                10,
                title,
                description,
                new HBox(new Text("Срок выполнения:"), datePicker),
                new HBox(new Text("Регулярная задача:"), isRegular, spinner),
                add
        );
        root.setPadding(new Insets(10));
        return root;
    }

    private void showAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Ошибка");
        errorAlert.setContentText("Название задачи должно быть заполнено");
        errorAlert.showAndWait();
    }

    void addItem(Consumer<TaskEntity> addItemCallback){
        LocalDate date = datePicker.getValue();
        TaskEntity.RegularRange currentRegularRange= TaskEntity.RegularRange.None;
        if (isRegular.isSelected() && spinner.getValue() != null) {
            currentRegularRange = switch (spinner.getValue()) {
                case "Ежедневно" -> TaskEntity.RegularRange.Day;
                case "Еженедельно" -> TaskEntity.RegularRange.Week;
                case "Ежемесячно" -> TaskEntity.RegularRange.Month;
                default -> TaskEntity.RegularRange.None;
            };
        }
        currentRegularRange = isRegular.isSelected() ? currentRegularRange : TaskEntity.RegularRange.None;
        TaskEntity newTask = new TaskEntity
                (title.getText(),
                        description.getText(),
                        currentRegularRange,
                        date
                );
        addItemCallback.accept(newTask);
    }

    public TextField getTitle() {
        return title;
    }

    public void setTitle(TextField title) {
        this.title = title;
    }

    public TextField getDescription() {
        return description;
    }

    public void setDescription(TextField description) {
        this.description = description;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public ComboBox<String> getSpinner() {
        return spinner;
    }

    public void setSpinner(ComboBox<String> spinner) {
        this.spinner = spinner;
    }

    public CheckBox getIsRegular() {
        return isRegular;
    }

    public void setIsRegular(CheckBox isRegular) {
        this.isRegular = isRegular;
    }
}
