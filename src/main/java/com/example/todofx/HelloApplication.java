package com.example.todofx;

import com.almasb.fxgl.logging.ConsoleOutput;
import com.example.todofx.data.TaskEntity;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.Console;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();

        ListView<TaskEntity> listView = new ListView<>();
        for (int i = 1; i <= 3; i++) {
            listView.getItems().add(new TaskEntity("Item " + i,"",false,""));
        }

        listView.setCellFactory(param -> new ListCell<TaskEntity>() {
            private final CheckBox checkBox = new CheckBox();
            private final TextField textField = new TextField();

            { //todo remove?
                textField.setEditable(false);


                if (getItem()!=null){
                setContentDisplay(ContentDisplay.RIGHT);
                textField.textProperty().bindBidirectional(new SimpleStringProperty(getItem().getName()));
                checkBox.selectedProperty().bindBidirectional(new SimpleBooleanProperty(getItem().isReady()));
            }}


            @Override
            protected void updateItem(TaskEntity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    textField.setText(item.getName());

                    checkBox.setSelected(item.isReady());
                    BorderPane borderPane =new BorderPane(checkBox, null, textField, null, null);
                    borderPane.addEventFilter(MouseEvent.MOUSE_CLICKED,event->{
                        System.out.println("sdfdsfsd");
                        Stage secondaryStage = new Stage();
                        StackPane root2 = new StackPane();
                        Scene secondaryScene = new Scene(root2, 200, 200);
                        secondaryStage.setScene(secondaryScene);
                        secondaryStage.show();
                    });
                    setGraphic(borderPane);
                }
            }
        });

        stage.setScene(new Scene(listView, 500, 500));
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