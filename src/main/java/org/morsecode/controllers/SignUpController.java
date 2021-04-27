package org.morsecode.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.morsecode.dao.Database;
import org.morsecode.models.User;


import java.io.IOException;
import java.sql.SQLException;

/**
 * класс регистрации пользователя. Добавляет значения в базу данных.
 * При успешной регистрации происходит переход на страницу login.fxml.
  */
public class SignUpController {

    @FXML
    private PasswordField password_field;

    @FXML
    private Button SignUpButton;

    @FXML
    private TextField SignUpName;

    @FXML
    private TextField SignUpLastName;

    @FXML
    private TextField login_field;

    @FXML
    private TextField SignUpCountry;

    @FXML
    private RadioButton SignUpCheckBoxMale;

    @FXML
    private RadioButton SignUpCheckBoxFemale;

    @FXML
    private ImageView homeBtn;

    @FXML
    void initialize() {

        SignUpButton.setOnAction(event -> {

            try {
                signUpNewUser();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        SignUpCheckBoxMale.setOnAction(event -> {
            if (SignUpCheckBoxMale.isSelected())
                SignUpCheckBoxFemale.setSelected(false);
        });

        SignUpCheckBoxFemale.setOnAction(event -> {
            if (SignUpCheckBoxFemale.isSelected())
                SignUpCheckBoxMale.setSelected(false);
        });
    }

    /**
     * добавление пользователя в БД
      */
    private boolean signUpNewUser() throws SQLException, ClassNotFoundException, IOException {
        Database dbHandler = new Database();

        String firstName = SignUpName.getText();
        String lastName = SignUpLastName.getText();
        String userName = login_field.getText();
        String password = password_field.getText();
        String location = SignUpCountry.getText();
        String gender = "";
        if(SignUpCheckBoxMale.isSelected())
            gender = "Мужской";
        else
            gender = "Женский";


        /**
         * проверка, что логин свободен
          */
        ObservableList<User> list = dbHandler.getUserAll();
        for (int i = 0; i < list.size(); i++){
            User item = (User) list.get(i);
            if ((item.getPassword()).equals(userName)) {
                window("Данный логин уже занят. Придумайте другой");
                return false;
        } }

        /**
         * проверка, что пол выбран
          */
        if((!SignUpCheckBoxMale.isSelected())&&(!SignUpCheckBoxFemale.isSelected())){
            window("Выберите ваш пол!");
            return false;
        }

        // проверка, что логин введён
        if (userName.equals("")){
            window("Введите логин!");
            return false;
        } // проверка, что пароль введён
        else if (password.equals("")){
            window("Введите пароль!");
            return false;
        } // проверка, что имя введено
        else if(firstName.equals("")){
            window("Введите имя!");
            return false;
        } // проверка, что фамилия введена
        else if(lastName.equals("")){
            window("Введите фамилию!");
            return false;
        } // проверка, что страна введена
        else if(location.equals("")){
            window("Введите страну!");
            return false;
        }

        User user = new User(firstName, lastName, userName, password, location, gender);
        dbHandler.Save_user(user);

        window("Пользователь создан"); // окно с информацией

        /**
         * открытие окна с авторизацией
          */
        Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/login.fxml"));
        Stage window = (Stage) SignUpButton.getScene().getWindow();
        window.setScene(new Scene(root, 700,400));
        return true;
    }

    /**
     * переход на страницу авторизации при нажатии на кнопку-картинку Домой.
      */
    public void homeBtn() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/login.fxml"));
        Stage window = (Stage) homeBtn.getScene().getWindow();
        window.setScene(new Scene(root, 700,400));
    }

    /**
     * окно с информацией
      */
    public void window(String wind){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(wind);
        alert.showAndWait();
    }
}

