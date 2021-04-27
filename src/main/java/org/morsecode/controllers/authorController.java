package org.morsecode.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * класс осуществляет переход на страницу home.fxml.
 */
public class authorController {

    @FXML
    private ImageView imageButtonHome2;

    // переход на страницу главного меню при нажатии на кнопку-картинку Домой.
    public void homeBtn() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/home.fxml"));
        Stage window = (Stage) imageButtonHome2.getScene().getWindow();
        window.setScene(new Scene(root, 700,400));
    }

}