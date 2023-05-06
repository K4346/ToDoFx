package com.example.todofx.data;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
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
        Text textRegular = new Text(task.getRegularRange().getRange() + " Задача");
        datePicker = new DatePicker();
        datePicker.setValue(task.getDate());
        changeDateCB = new CheckBox();

        reasonTV = new TextField();
        reasonTV.setPrefHeight(20);
        reasonTV.setPromptText("Причина изменения даты");
        changeButton = new Button("Изменить дату");
        VBox root = new VBox(10, title);
        if (!task.getDescription().isEmpty()) {
            root.getChildren().add(description);
        }
        HBox hBox = new HBox(10, new Text("Изменить срок выполнения:"), changeDateCB);
        root.getChildren().addAll(
                textRegular,
                new Text("Дата выполнения: " + task.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),
                hBox

        );
        VBox.setMargin(hBox, new Insets(0, 0, 0, 50));
        setListenners(task, root, itemCallback);

        root.setPadding(new Insets(10));
        root.setAlignment(Pos.BASELINE_CENTER);
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        description.setStyle("-fx-font-size: 16;");
        textRegular.setStyle("-fx-font-size: 14; -fx-font-style: italic;");

        changeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        DropShadow shadow = new DropShadow();
        changeButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> changeButton.setEffect(shadow)
        );
        changeButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> changeButton.setEffect(null)
        );

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
            if (changeDateCB.isSelected() && (datePicker.getValue() == task.date || datePicker.getValue().isBefore(LocalDate.now()))) {
                showAlert(true);
            } else if (reasonTV.getText().isEmpty()){
                showAlert(false);
            } else{
                addItem(task, itemCallback);
            }

        });
    }

    private void addItem(TaskEntity task, Consumer<TaskEntity> itemCallback) {
        TaskEntity newTask = new TaskEntity
                (task.getName(),
                        task.getDescription(),
                        task.getRegularRange(),
                        datePicker.getValue(),
                        changeDateCB.isSelected() ? reasonTV.getText() : ""
                );
        itemCallback.accept(newTask);
    }

    private void showAlert(boolean dateError) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.getDialogPane().setStyle("-fx-background-color: #f2f2f2;");
        errorAlert.setTitle("Ошибка");
        errorAlert.setHeaderText(null);
        Label label;
        if (dateError) {
            label = new Label("Дата не изменилась или указана прошедшая дата");
        } else {
            label = new Label("Не указана причина изменения даты");
        }
        label.setStyle("-fx-font-size: 14;");
        errorAlert.getDialogPane().setContent(label);
        errorAlert.showAndWait();
    }

}
