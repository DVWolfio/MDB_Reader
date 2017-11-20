package Prod;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class Main extends Application {


    private void initialize() {



    }


    public void start(Stage stage) throws Exception {
        // load the scene fxml UI.
        // grabs the UI scenegraph view from the loader.
        // grabs the UI controller for the view from the loader.

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("MainF.fxml"));
        final Parent root = (Parent) loader.load();
        final Controller controller = loader.<Controller>getController();
        controller.setPrimaryStage(stage);





        //заполнение дерева (пример)
//        controller.loadTreeItems();

        MultipleSelectionModel<TreeItem<String>> MTreeSelModel = controller.getTableList().getSelectionModel();

        MTreeSelModel.selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            public void changed(ObservableValue<? extends TreeItem<String>> changed, TreeItem<String> oldVal, TreeItem<String> newVal) {
                // отобразить выбранный элемент и полный путь от него к корневому узлу
                if (newVal != null) {
                    String path = newVal.getValue();
                    TreeItem<String> tmp = newVal.getParent();
                    while (tmp != null) {
                        path = tmp.getValue() + "->" + path;
                        tmp = tmp.getParent();
                    }
//                    System.out.println("Selection is " + newVal.getValue() +"\nComplete path is " + path);
                    try {
                        controller.loadCurrentTable(newVal.getValue().split("[ ]")[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        // initialize the stage.
        stage.setScene(new Scene(root));
//        stage.initStyle(StageStyle.TRANSPARENT); --рамка
        stage.getIcons().add(new Image(getClass().getResourceAsStream("584782.png")));
        stage.show();
    }

  /*  */

    /**
     * small helper class for handling tree loading events.
     *//*
    private class TreeLoadingEventHandler implements EventHandler<ActionEvent> {
        private Controller controller;
        private int idx = 0;

        TreeLoadingEventHandler(Controller controller) {
            this.controller = controller;
        }

        @Override public void handle(ActionEvent t) {
            controller.loadTreeItems("Loaded " + idx, "Loaded " + (idx + 1), "Loaded " + (idx + 2));
            idx += 3;
        }
    }*/
    public static void main(String[] args) {
        launch(args);
    }
}

