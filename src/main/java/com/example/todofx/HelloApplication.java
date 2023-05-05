package com.example.todofx;

import com.example.todofx.data.AddItem;
import com.example.todofx.data.DetailItem;
import com.example.todofx.data.TaskEntity;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HelloApplication extends Application {
    protected ListView<TaskEntity> listView;

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();

        listView = new ListView<>();
        for (int i = 1; i <= 3; i++) {
            listView.getItems().add(new TaskEntity("Item " + i, "", TaskEntity.RegularRange.None, LocalDate.now()));
        }

        listView.setCellFactory(param -> new ListCell<TaskEntity>() {
            private final CheckBox checkBox = new CheckBox();
            private final Text title = new Text();

            private final Text date = new Text();

            { //todo remove?

                if (getItem() != null) {
                    setContentDisplay(ContentDisplay.RIGHT);
                    title.textProperty().bindBidirectional(new SimpleStringProperty(getItem().getName()));
//                todo
                    date.textProperty().bindBidirectional(new SimpleStringProperty(getItem().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
                    checkBox.selectedProperty().bindBidirectional(new SimpleBooleanProperty(getItem().isReady()));
                }
            }


            @Override
            protected void updateItem(TaskEntity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
//                    VBox root = new VBox(
//                            10,
//                            title,
//                            description,
//                            new HBox(new Text("Срок выполнения:"), datePicker),
//                            new HBox(new Text("Регулярная задача:"), isRegular, spinner),
//                            add
//                    );
//                    root.setPadding(new Insets(10));
                    title.setText(item.getName());
                    checkBox.setSelected(item.isReady());
                    date.setText(item.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
//                    BorderPane borderPane =new BorderPane(checkBox, null, title, null, null);
                    VBox rootDetailTask = new VBox(10, new HBox(10, checkBox, title), date);
                    rootDetailTask.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        Stage detailTaskStage = new Stage();
                        Scene detailTaskScene = new Scene(new DetailItem().getDetailItemRoot(item, taskEntity -> {
                            date.setText(taskEntity.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                            detailTaskStage.close();
                        }), 300, 300);
                        detailTaskStage.setScene(detailTaskScene);
                        detailTaskStage.show();
                    });
                    setGraphic(rootDetailTask);
                }
            }
        });
        Button addTask = new Button("Add Task");
        addTask.setOnAction(actionEvent -> {
            Stage addTaskStage = new Stage();
            Scene addTaskScene = new Scene(new AddItem().addItemRoot(taskEntity -> {
                listView.getItems().add(taskEntity);
                addTaskStage.close();
            }), 300, 300);
            addTaskStage.setScene(addTaskScene);
//            listView.getItems().add(new TaskEntity("Item new","",false,""));
            addTaskStage.show();
        });
        FlowPane root = new FlowPane();
        root.getChildren().add(listView);
        root.getChildren().add(addTask);
        root.setAlignment(Pos.TOP_CENTER);
        root.setOrientation(Orientation.VERTICAL);
        stage.setScene(new Scene(root, 300, 500));
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    public static class MyItem {
        private final String text;
        private final Boolean selected;

        public MyItem(String text) {
            this.text = text;
            this.selected = true;
        }

        public String getText() {
            return text;
        }

        public Boolean isSelected() {
            return selected;
        }

        public BooleanProperty selectedProperty() {
            return new SimpleBooleanProperty(selected);
        }

        public StringProperty textProperty() {
            return new SimpleStringProperty(text);
        }
    }
}