package sample;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.*;

public class DynamicTableView extends Application {
    private static final int N_COLS = 5;
    private static final int N_ROWS = 10;

    private static class TestDataGenerator {
        private static final String[] LOREM = ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc tempus cursus diam ac blandit. Ut ultrices lacus et mattis laoreet. Morbi vehicula tincidunt eros lobortis varius. Nam quis tortor commodo, vehicula ante vitae, sagittis enim. Vivamus mollis placerat leo non pellentesque. Nam blandit, odio quis facilisis posuere, mauris elit tincidunt ante, ut eleifend augue neque dictum diam. Curabitur sed lacus eget dolor laoreet cursus ut cursus elit. Phasellus quis interdum lorem, eget efficitur enim. Curabitur commodo, est ut scelerisque aliquet, urna velit tincidunt massa, tristique varius mi neque et velit. In condimentum quis nisi et ultricies. Nunc posuere felis a velit dictum suscipit ac non nisl. Pellentesque eleifend, purus vel consequat facilisis, sapien lacus rutrum eros, quis finibus lacus magna eget est. Nullam eros nisl, sodales et luctus at" +
                ", lobortis at sem.").split(" ");

        private int curWord = 0;

        public List<String> getNext(int nWords) {
            List<String> words = new ArrayList<>();

            for (int i = 0; i < nWords; i++) {
                if (curWord == Integer.MAX_VALUE) {
                    curWord = 0;
                }

                words.add(LOREM[curWord % LOREM.length]);
                curWord++;
            }
            System.out.println("\t"+words);
            return words;
        }
    }


    public void start(Stage stage) throws Exception {
        TestDataGenerator dataGenerator = new TestDataGenerator();

        TableView<ObservableList<String>> tableView = new TableView<>();

        // add columns
        List<String> columnNames = dataGenerator.getNext(N_COLS);
        for (int i = 0; i < columnNames.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames.get(i));
            System.out.print("\t"+columnNames.get(i));
            column.setCellValueFactory(param ->
                    new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            tableView.getColumns().add(column);
        }
        System.out.println("\n\t----------------------------------------");
        // add data
        for (int i = 0; i < N_ROWS; i++) {
            System.out.println(i);
            tableView.getItems().add(
                    FXCollections.observableArrayList(
                            dataGenerator.getNext(N_COLS)
                    )
            );
        }

        tableView.setPrefHeight(200);

        Scene scene = new Scene(tableView);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}