package org.morsecode.controllers;

import animations.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.morsecode.dao.Database;
import org.morsecode.models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * класс авторизации пользователя. При корректном вводе логина и пароля осуществляется переход на страницу home.fxml.
  */
public class loginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button authSignInButton;

    @FXML
    private Button loginSignUpButton;

    public static String username1; //глобальная перемена для записи пользователей в БД, которые отправили сообщение

    @FXML
    void initialize() {

        /**
         * авторизация
         */
        authSignInButton.setOnAction(event -> {
            String loginText = login_field.getText().trim();
            String loginPassword = password_field.getText().trim();

            if(!loginText.equals("") && !loginPassword.equals("")) {
                try {
                    loginUser(loginText, loginPassword); // вызов метода авторизации пользователя
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                // окно с информацией
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Информация");
                alert.setHeaderText(null);
                alert.setContentText("Логин или пароль не введён");
                alert.showAndWait();
            }});


    }

    /**
     * метод проверяющий существует ли пользователь в системе
      */
    private void loginUser(String loginText, String loginPassword) throws SQLException, ClassNotFoundException, IOException {
        Database database = new Database();
        User user = new User();
        user.setUsername(loginText);
        user.setPassword(loginPassword);
        int result = database.getUser(user);

        // если пользователь найден происходит переход на страницу home.fxml
        if(result >= 1) {
            username1 = loginText; // переменная используется при добавлении сообщений в столбце username
            Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/home.fxml"));
            Stage window = (Stage) authSignInButton.getScene().getWindow();
            window.setScene(new Scene(root, 700,400));
        // если пользователь не найден происходит "встряска" полей ввода
        } else {
            Shake userLoginAnim = new Shake(login_field);
            Shake userPassAnim = new Shake(password_field);
            userLoginAnim.playAnim();
            userPassAnim.playAnim();
        }
    }


    /**
     * переход на страницу с регистрацией — SignUp.fxml
      */
    public void singInBtn() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/SignUp.fxml"));
        Stage window = (Stage) loginSignUpButton.getScene().getWindow();
        window.setScene(new Scene(root, 700,400));

    }
}
