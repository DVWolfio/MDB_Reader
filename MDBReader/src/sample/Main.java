package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sun.reflect.generics.tree.Tree;

public class Main extends Application {

    Label response;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();


        // присвоить з а головок подмосткам
        primaryStage.setTitle("DemonstrateаTreeView");

        // Продемонстрировать элемент управления TreeView
        // Использовать панель поточной компоновки FlowPane
        // в качестве корневого узла. В данном случае с
        // промежут ками 10 по вертикали и по горизонтали
        FlowPane rootNode = new FlowPane(10, 10);
        // выровнять элементы управления по центру сцены
        rootNode.setAlignment(Pos.CENTER);
        // создать сцену
        Scene myScene = new Scene(rootNode, 310, 460);
        // установить сцену на подмостках
        primaryStage.setScene(myScene);
        // созда т ь ме т ку , изве щающую о состоянии элемента ,
        // выбранно го из дерева
        response = new Label("NoSelection");
        // Ничего не выбрано

        // создать узлы дерева, начиная с корневого узла
        TreeItem<String> tiRoot = new TreeItem<String>(" Food");

        // ввести поддеревья, начиная с узла фруктов
        TreeItem<String> tiFruit = new TreeItem<String>("Fruit ");
        // построить узел яблок
        TreeItem<String> tiApples = new TreeItem<String>("Apples ");
        // ввести порожденные узлы сортов яблок в узел яблок
        tiApples.getChildren().add(new TreeItem<String>("Fuji"));
        tiApples.getChildren().add(new TreeItem<String>("Winesap"));
        tiApples.getChildren().add(new TreeItem<String>("Jonathan"));
        // ввести порожденные узлы видов фруктов в узел фруктов
        tiFruit.getChildren().add(tiApples);
        tiFruit.getChildren().add(new TreeItem<String>("Pears"));
        tiFruit.getChildren().add(new TreeItem<String>("Oranges"));

        // и наконец, ввести узел фруктов в корне вой узел
        tiRoot.getChildren().add(tiFruit);
        // а теперь вве сти аналогичным обр а з ом уз ел овощей

        // и наконец, ввести аналогичным образом узел орехов
        // создать древ овидное представление , исполь зуя только
        // что построенное дерево
        TreeView<String> tvFood = new TreeView<String>(tiRoot);
        // получить модель выбора для дре вовидного представления
        MultipleSelectionModel<TreeItem<String>> tvSelModel =
                tvFood.getSelectionModel();
        // исполь зовать приемник событий изменения , чтобы оперативно
        // реагировать на выбор элементов в дре вовидном представлении
        tvSelModel.selectedItemProperty().addListener(
                new ChangeListener<TreeItem<String>>() {
                    public void changed(ObservableValue<? extends TreeItem<String>> changed, TreeItem<String> oldVal, TreeItem<String> newVal) {
                        // отобразить выбранный элемент и полный путь от
                        // него к корневому узлу
                        if (newVal != null) {

                            // пос троить весь путь к выбранному элементу
                            String path = newVal.getValue();
                            TreeItem<String> tmp = newVal.getParent();
                            while (tmp != null) {
                                path = tmp.getValue() + "->" + path;
                                tmp = tmp.getParent();
                            }


                            // отобразить выбранный элемент и полный путь к нему
                            response.setText("Selection is " + newVal.getValue() +
                                    "\nComplete path is " + path);
                            // Выбран указанный элемент
                            // Полный путь к нему

                        }
                    }
                });
        // ввести элементы управления в граф сцены
        rootNode.getChildren().addAll(tvFood, response);
        // показать подмостки и сцену на них
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

