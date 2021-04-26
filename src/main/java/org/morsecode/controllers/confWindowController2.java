package org.morsecode.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.morsecode.dao.Database;

import java.io.IOException;

// класс вызывается при ошибке, когда введённого значения нет в словаре. В нём можно выбрать опцию добавления нового значения (тогда откроется dict.fxml), либо не добавлять его (тогда окно закроется).
public class confWindowController2 {

    @FXML
    private Text textArea;

    @FXML
    private Button yesBtn;

    @FXML
    private Button noBtn;


    // метод срабатывающий при нажатии на кнопку yesBtn для открытия словаря
    public void dictOpen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/morsecode/dict.fxml"));
        Parent root = (Parent) loader.load();
        DictController controller = loader.getController();
        controller.showCode(Database.unknownLetter); // передача кода в поле ввода кода, который пользователь будет добавлять
        Stage window = (Stage) yesBtn.getScene().getWindow();
        window.setScene(new Scene(root, 700,400));
    }

    // закрытие окна при нажатии на кнопку noBtn
    public void noOpen(){
        Stage stage = (Stage) noBtn.getScene().getWindow();
        stage.close();
    }

}
