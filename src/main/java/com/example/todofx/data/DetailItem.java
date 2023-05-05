package com.example.todofx.data;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class DetailItem {
    public DatePicker datePicker;

    public TextField reasonTV;

    public CheckBox changeDateCB;

    public Button changeButton;

    public Pane getDetailItemRoot(TaskEntity task, Consumer<TaskEntity> itemCallback) {
        Text title = new Text(task.getName());
        Text description = new Text(task.getDescription());
        Text textRegular = new Text(task.getRegularRange().getRange() + "Задача");
        datePicker = new DatePicker();
        datePicker.setValue(task.getDate());
        changeDateCB = new CheckBox();

        reasonTV = new TextField();
        reasonTV.setPrefHeight(20);
        reasonTV.setPromptText("Причина изменения даты");
        changeButton = new Button("Изменить дату");
        VBox root = new VBox(10, title);
        if (!task.getDescription().isEmpty()){
            root.getChildren().add(description);
        }
        root.getChildren().addAll(
                textRegular,
                new Text("Дата выполнения: " + task.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),
                new HBox(10, new Text("Изменить срок выполнения:"), changeDateCB)
        );
        setListenners(task,root, itemCallback);

        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        return root;
    }

    private void setListenners(TaskEntity task, VBox root, Consumer<TaskEntity> itemCallback) {
        changeDateCB.setOnAction(actionEvent -> {

            if (changeDateCB.isSelected()) {
                root.getChildren().addAll(
                        datePicker,
                        reasonTV,
                        changeButton
                );
            } else {
                root.getChildren().removeAll(
                        datePicker,
                        reasonTV,
                        changeButton
                );
            }
        });

        changeButton.setOnAction(actionEvent -> {
            if (changeDateCB.isSelected() && datePicker.getValue() == task.date) {
                showAlert();
            } else {
                addItem(task, itemCallback);
            }

        });
    }

    private void addItem(TaskEntity task, Consumer<TaskEntity> itemCallback) {
        TaskEntity newTask = new TaskEntity
                (task.getName(),
                        task.getDescription(),
                        task.getRegularRange(),
                        datePicker.getValue()
                );
        itemCallback.accept(newTask);
    }

    private void showAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Ошибка");
        errorAlert.setContentText("Дата не изменилась и/или не указана причина изменения");
        errorAlert.showAndWait();
    }

}
