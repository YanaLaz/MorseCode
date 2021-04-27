package org.morsecode.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.morsecode.dao.Database;

import java.util.List;

/**
 * класс главного меню пользователя, в нём выбираются действия, которые перенаправляют на страницы
 * rusCode.fxml, codeRus.fxml, dict.fxml, stats.fxml, author.fxml в зависимости от выбранной кнопки.
  */
public class HomeController{

        @FXML
        private Button rusCodeButton;

        @FXML
        private Button codeRusButton;

        @FXML
        private Button rusDictButton;

        @FXML
        private Button authorBtn;

        @FXML
        private Button statsBtn;



        @FXML
        void initialize() {
        }

        /**
         * переход на страницу rusCode.fxml
          */
        public void rcBtn() throws Exception {
                Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/rusCode.fxml"));
                Stage window = (Stage) rusCodeButton.getScene().getWindow();
                window.setScene(new Scene(root, 700,400));

        }

        /**
         * переход на страницу codeRus.fxml
          */
        public void crBtn() throws Exception {
                Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/codeRus.fxml"));
                Stage window = (Stage) codeRusButton.getScene().getWindow();
                window.setScene(new Scene(root, 700,400));

        }

        /**
         * переход на страницу dict.fxml
          */
        public void dictBtn() throws Exception {
                Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/dict.fxml"));
                Stage window = (Stage) rusDictButton.getScene().getWindow();
                window.setScene(new Scene(root, 700,400));
        }

        /**
         * переход на страницу author.fxml
          */
        public void author() throws Exception {
                Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/author.fxml"));
                Stage window = (Stage) authorBtn.getScene().getWindow();
                window.setScene(new Scene(root, 700,400));
        }

        /**
         * переход на страницу stats.fxml
          */
        public void stats() throws Exception {
                Database database = new Database();

                List list = database.stats();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/morsecode/stats.fxml"));
                Parent root = (Parent) loader.load();
                statsController controller = loader.getController();
                controller.setText1((String) list.get(1));
                controller.setText2((String) list.get(0).toString());
                Stage window = (Stage) statsBtn.getScene().getWindow();
                window.setScene(new Scene(root, 700,400));
        }
}