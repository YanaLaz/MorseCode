package org.morsecode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// основной класс, который осуществляет запуск программы.
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Кодировщик Азбуки Морзе");
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
