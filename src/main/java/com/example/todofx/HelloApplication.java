package com.example.todofx;

import com.example.todofx.data.AddItem;
import com.example.todofx.data.Data;
import com.example.todofx.data.DetailItem;
import com.example.todofx.data.TaskEntity;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HelloApplication extends Application {
    protected ListView<TaskEntity> listView;

    protected Callback<ListView<TaskEntity>, ListCell<TaskEntity>> cellFactory;

    Data data;

    @Override
    public void start(Stage stage) throws IOException {

        data = new Data();
        listView = new ListView<>();
        for (TaskEntity task : data.getTasks()) {
            listView.getItems().add(task);
        }

        cellFactory = param -> new ListCell<TaskEntity>() {
            private final CheckBox checkBox = new CheckBox();
            private final Text title = new Text();

            private final Text date = new Text();
            private final Text reasonDateChangeText = new Text();

            private final Text outOfTimeText = new Text();

            private final Button deleteButton = new Button("удалить");

            @Override
            protected void updateItem(TaskEntity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    title.setText(item.getName());
                    checkBox.setSelected(item.isReady());

                    deleteButton.setOnAction(actionEvent -> {
                        listView.getItems().remove(item);
                        ArrayList<TaskEntity> taskList = new ArrayList<>(listView.getItems());
                        data.setTasks(taskList);
                    });

                    date.setText(item.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    reasonDateChangeText.setText(item.getReasonDateChange());

                    outOfTimeText.setText(item.getComment().getValue());

                    VBox vbox = new VBox(10,
                            title,
                            new HBox(10, date, reasonDateChangeText),
                            new HBox(outOfTimeText));

                    checkBox.setOnAction(actionEvent -> {
                        item.setReady(checkBox.isSelected());
                        item.setOutOfTime(item.isReady() && LocalDate.now().isAfter(item.getDate()));
                        item.setComment(data.checkProgressByItem(item));
                        ArrayList<TaskEntity> taskList = new ArrayList<>(listView.getItems());
                        data.setTasks(taskList);

                        listView.refresh();

                    });
                    HBox rootTask = new HBox(10, checkBox, vbox, deleteButton);
                    rootTask.setManaged(true);
                    rootTask.setMaxWidth(Double.MAX_VALUE);
                    rootTask.setMaxHeight(Double.MAX_VALUE);
                    rootTask.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    rootTask.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    vbox.setMaxWidth(Double.MAX_VALUE);
                    vbox.setMaxHeight(Double.MAX_VALUE);
                    vbox.setPrefWidth(0.0);
                    vbox.setPrefHeight(0.0);
                    vbox.setSpacing(5.0);
                    HBox.setHgrow(vbox, Priority.ALWAYS);

                    title.setStyle("-fx-font-weight: bold;");
                    outOfTimeText.setStyle("-fx-font-style: italic;");

                    rootTask.setStyle("-fx-border-color: gray; -fx-border-radius: 5.0;");
                    rootTask.setPadding(new Insets(10, 10, 10, 10));

                    vbox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        Stage detailTaskStage = new Stage();
                        detailTaskStage.initModality(Modality.APPLICATION_MODAL);
                        detailTaskStage.setResizable(false);
                        Scene detailTaskScene = new Scene(new DetailItem().getDetailItemRoot(item, taskEntity -> {
                            item.setDate(taskEntity.getDate());
                            item.setOutOfTime(taskEntity.getOutOfTime());

                            item.setComment(data.checkProgressByItem(item));

                            item.setReasonDateChange(taskEntity.getReasonDateChange());

                            ArrayList<TaskEntity> taskList = new ArrayList<>(listView.getItems());
                            data.setTasks(taskList);

                            listView.refresh();
                            detailTaskStage.close();
                        }), 300, 300);
                        detailTaskStage.setScene(detailTaskScene);
                        detailTaskStage.setTitle("Задача");
                        detailTaskStage.show();
                    });
                    setGraphic(rootTask);
                }
            }
        };
        listView.setCellFactory(cellFactory);
        Button addTask = initAddTaskButton();

        VBox root = new VBox();
        root.getChildren().add(listView);
        root.getChildren().add(addTask);
        VBox.setVgrow(listView, Priority.ALWAYS);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Список задач");
        stage.show();

    }

    private Button initAddTaskButton() {
        Button addTask = new Button("Add Task");
        addTask.setOnAction(actionEvent -> {
            Stage addTaskStage = new Stage();
            addTaskStage.initModality(Modality.APPLICATION_MODAL);
            addTaskStage.setResizable(false);
            addTaskStage.setTitle("Добавить задачу");
            Scene addTaskScene = new Scene(new AddItem().addItemRoot(taskEntity -> {
                listView.getItems().add(taskEntity);

                ArrayList<TaskEntity> taskList = new ArrayList<>(listView.getItems());
                data.setTasks(taskList);

                addTaskStage.close();
            }), 300, 300);
            addTaskStage.setScene(addTaskScene);
            addTaskStage.show();
        });
        addTask.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        DropShadow shadow = new DropShadow();
        addTask.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> addTask.setEffect(shadow)
        );
        addTask.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> addTask.setEffect(null)
        );
        return addTask;
    }

    public static void main(String[] args) {
        launch();
    }


}