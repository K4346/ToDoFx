package com.example.todofx.data;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.function.Consumer;


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

        Button addButton = new Button("Добавить");
        addButton.setOnAction(actionEvent -> {
            if (title.getText().isEmpty()) {
                showAlert(true);
            } else if (datePicker.getValue().isBefore(LocalDate.now())) {
                showAlert(false);
            } else {
                addItem(addItemCallback);
            }

        });
        HBox hBox = new HBox(5, new Text("Регулярная задача:"), isRegular, spinner);
        VBox root = new VBox(
                10,
                title,
                description,
                new HBox(new Text("Срок выполнения:"), datePicker),
                hBox,
                addButton
        );
        root.setPadding(new Insets(10));
        VBox.setMargin(addButton, new Insets(0, 0, 0, 100));

        title.setStyle("-fx-font-weight: bold;");

        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        DropShadow shadow = new DropShadow();
        addButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> addButton.setEffect(shadow)
        );
        addButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> addButton.setEffect(null)
        );
        return root;
    }

    private void showAlert(boolean nameError) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.getDialogPane().setStyle("-fx-background-color: #f2f2f2;");
        errorAlert.setTitle("Ошибка");
        errorAlert.setHeaderText(null);
        Label label;
        if (nameError) {
            label = new Label("Название задачи должно быть заполнено");
        } else {
            label = new Label("Выбрана прошлая дата");
        }
        label.setStyle("-fx-font-size: 14;");
        errorAlert.getDialogPane().setContent(label);
        errorAlert.showAndWait();
    }

    void addItem(Consumer<TaskEntity> addItemCallback) {
        LocalDate date = datePicker.getValue();
        TaskEntity.RegularRange currentRegularRange = TaskEntity.RegularRange.None;
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
                        date,
                        ""
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
