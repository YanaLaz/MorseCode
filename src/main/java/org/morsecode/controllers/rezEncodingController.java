package org.morsecode.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * класс, который отображает страницу с результатом кодирования или декодирования принятого сообщения
 * от пользователя и осуществляется переход на страницу home.fxml.
  */
public class rezEncodingController implements Initializable {

    @FXML
    private ImageView imageButtonHome2;

    @FXML
    private Label rez;

    @FXML
    void export(ActionEvent event) {

    }

    @FXML
    void homeBtn(MouseEvent event) {

    }

    /**
     * переход на страницу главного меню при нажатии на кнопку-картинку Домой.
      */
    public void homeBtn() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/home.fxml"));
        Stage window = (Stage) imageButtonHome2.getScene().getWindow();
        window.setScene(new Scene(root, 700, 400));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * метод устанавливает значение результата в текстовое поле
      */
    public void showRez(String text){
        rez.setText(text);
    }


    /**
     * экспорт файла в отдельный файл
      */
    public void export(){
        File myFile = new File("rezEncoding.txt");
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(myFile));
            writer.write(rez.getText()); // запись в файл
            writer.flush();
            writer.close();

            // окно с информацией
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Экопрт в файл rezEncoding.txt прошел успешно");
            alert.showAndWait();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
