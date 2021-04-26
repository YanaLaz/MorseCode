package org.morsecode.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.morsecode.dao.Database;
import org.morsecode.models.dict;
import org.morsecode.models.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

// класс, который кодирует введённое сообщение пользователя на русском в азбуку Морзе. При корректном вводе
// открывается окно rezEncoding.fxml, в противном случае confWindow.fxml. Также здесь происходит импорт
// текстового файла с сообщением от пользователя и осуществляется переход на страницу home.fxml
public class rusCodeController {

    private static AnchorPane content;

    @FXML
    private TextField inputMes;

    @FXML
    private Button codeInputButton;

    @FXML
    private ImageView imageButtonHome2;

    // переход на страницу главного меню при нажатии на кнопку-картинку Домой.
    public void homeBtn() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/home.fxml"));
        Stage window = (Stage) imageButtonHome2.getScene().getWindow();
        window.setScene(new Scene(root, 700, 400));
    }

    // кодирование сообщения через DAO класс
    public void inputCode() throws SQLException, ClassNotFoundException, IOException {
        if (checkRus(inputMes.getText())) {
            String code = Database.encoding(inputMes.getText()); // получение результата
            if (!code.equals("false")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/org/morsecode/rezEncoding.fxml"));
                Parent root = (Parent) loader.load();
                rezEncodingController controller = loader.getController();
                controller.showRez(code); // передача результата в контроллер отображения результата
                Stage window = (Stage) codeInputButton.getScene().getWindow();
                window.setScene(new Scene(root, 700,400));

                sendMes(inputMes.getText(),code); // отправка принимаемого значения и результата в БД
            }
        }
    }

    // проверка на корректность ввода
    public boolean checkRus(String rus) {
        String regex = "[а-яА-Я]";
        for (int j = 0; j < rus.length(); j++) {
            String ent = String.valueOf(rus.charAt(j));
            if (!ent.equals(" ")) {
                if (!ent.matches(regex)) {
                    window(ent + " не является буквой от А до Я");
                    return false;
                }
            }
        }
        return true;
    }

    // окно с информацией
    public void window(String wind) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(wind); // сюда передаётся строка с ошибкой, которую нужно вывести
        alert.showAndWait();
    }

    // метод Импорта файла
    public void upload() throws IOException, SQLException, ClassNotFoundException {
        // FileChooser открывает вспомогающее окно с директорией проекта, где пользователь может выбарть
        // нужный для импорта файл
        FileChooser file = new FileChooser();
        File selectedFile = file.showOpenDialog(null);

        Database database = new Database();

        String rez = "";
        String input = "";
        dict item = new dict();
        if (selectedFile != null) {
            try {
                // чтение файла построчно
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (checkRus(line)){ // проверка, что буква введена корректно
                        input += line; // записываем и input для того, чтобы отправить значение в messages
                        input += " ";
                        String code = database.encoding(line);
                        rez += code; // запись результата кодировки
                        rez += " ";
                    } else {
                    rez = "false";
                    break;
                }}
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!rez.contains("false")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/org/morsecode/rezEncoding.fxml"));
                Parent root = (Parent) loader.load();
                rezEncodingController controller = loader.getController();
                controller.showRez(rez); // передача результата в контроллер отображения результата
                Stage window = (Stage) codeInputButton.getScene().getWindow();
                window.setScene(new Scene(root, 700,400));

                sendMes(input, rez); // отправка принимаемого значения и результата в БД
            }
        }
    }

    // метод, который передаёт значения в Database для дальнейшей записи в БД
    public void sendMes(String input, String output){
        message mes = new message();
        mes.setInput(input.toUpperCase());
        mes.setOutput(output);
        mes.setUsername(loginController.username1);
        Database database = new Database();
        database.addMes(mes);
    }
}


