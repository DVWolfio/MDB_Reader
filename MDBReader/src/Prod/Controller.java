package Prod;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private TreeView<String> TableList;
    @FXML
    private TableView<ObservableList<String>> tableRows;
    @FXML
    private Button OpenFile;
    private Stage primaryStage;
    private MDBRead readerObj = new MDBRead();
    private ArrayList<TableColumn<ObservableList<String>, String>> headOfCurTable = new ArrayList<>();
    private List<String> bodyOfCurTable = new ArrayList<>();
    private ObservableList<String> rows = FXCollections.observableArrayList();
    private File selectedFile;



    public void SelectFile() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new java.io.File("C:\\"));
        chooser.setTitle("Выберите файл");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("mdb (MS Access 2000)", "*.mdb"));
        selectedFile = chooser.showOpenDialog(TableList.getScene().getWindow());
        readerObj.setFileName(selectedFile.getAbsolutePath());
        primaryStage.setTitle(selectedFile.getAbsolutePath());
        loadTreeItems();
    }



    public TreeView<String> getTableList() {
        return TableList;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



    public void loadTreeItems() {
        // создать узлы дерева, начиная с корневого узла
        TreeItem<String> treeItemRoot = new TreeItem<String>("Tables of .mdb");
        treeItemRoot.setExpanded(true);

        try {
            for (String tableName : readerObj.getTableList()) {
                treeItemRoot.getChildren().add(new TreeItem<String>(tableName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        TableList.setRoot(treeItemRoot);
    }

    public StringProperty cellValue(int idx) {
        return new SimpleStringProperty(bodyOfCurTable.get(idx));
    }

    public void loadCurrentTable(String selectedTable) throws IOException {
//        System.out.print("me: "+selectedTable);
//        tableRows.
        tableRows.getColumns().clear();
        tableRows.getItems().clear();
        headOfCurTable.clear();
        bodyOfCurTable.clear();

        headOfCurTable = readerObj.getHeadTable(selectedTable);
        bodyOfCurTable = readerObj.getBodyTable(selectedTable, headOfCurTable.size());

        ArrayList<String> oneRow = new ArrayList<>();

        for (int i = 0; i < headOfCurTable.size(); i++) {
            final int final_i = i;
            if (bodyOfCurTable.size() > 0) {
                headOfCurTable.get(i).setCellValueFactory(param ->
                        new ReadOnlyObjectWrapper<>(param.getValue().get(final_i))
                );
            }
            tableRows.getColumns().add(headOfCurTable.get(i));
            // FIXME: 10/10/2017 нагуглить как исправить ширину по умолчанию
        }

        int lineSprtr = 0;
        for (int j = 0; j < bodyOfCurTable.size(); j++) {
            lineSprtr++;

            oneRow.add((String) bodyOfCurTable.get(j));
            if (lineSprtr % headOfCurTable.size() == 0 && j != 0) {
                tableRows.getItems().add(FXCollections.observableArrayList(oneRow));
                oneRow.clear();
            }
        }
    }




}
