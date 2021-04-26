package org.morsecode.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;

//класс устанавливает значения для статистики и осуществляет переход на страницу home.fxml.
public class statsController {

    @FXML
    private ImageView imageButtonHome2;

    @FXML
    private Label rusLabel;

    @FXML
    private Label quantLabel;

    @FXML
    void homeBtn(MouseEvent event) {

    }

    // метод устанавливает значение самого популярного сообщения в текстовое поле
    public void setText1(String text){ rusLabel.setText(text); }
    // метод устанавливает количество встречаемых раз популярного сообщения в текстовое поле
    public void setText2(String text){ quantLabel.setText(text); }


    // переход на страницу главного меню при нажатии на кнопку-картинку Домой.
    @FXML
    public void homeBtn() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/home.fxml"));
        Stage window = (Stage) imageButtonHome2.getScene().getWindow();
        window.setScene(new Scene(root, 700, 400));
    }
}
