package org.morsecode.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.morsecode.Const;
import org.morsecode.dao.Database;
import org.morsecode.models.dict;


// класс, в котором происходит подключение и вывод информации из БД, редактирование, удаление и добавление
// новых значений в словарь, а также здесь происходит поиск и фильтрация значений и осуществляется
// переход на страницу home.fxml.
public class DictController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBtn;

    @FXML
    private ImageView homebtn;

    @FXML
    private TextField txtRus;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtId;

    @FXML
    private TableView<dict> tableDict;

    @FXML
    private TableColumn<dict, Integer> colId;

    @FXML
    private TableColumn<dict, String> colRus;

    @FXML
    private TableColumn<dict, String> colCode;

    @FXML
    private TextField filter;


    // метод удаление значения из словаря
    @FXML
    void Delete(ActionEvent event)  {
        try {
            txtId.setVisible(false);
            Database database = new Database();
            database.Delete_dict(Integer.parseInt(txtId.getText())); // осуещствляется через DAO класс

            window("Значение удалено");
            updateTable(); // обновление таблицы
            search(); // поиск
            clear(); // очистка полей ввода
        } catch (Exception e) {
            window("Значение НЕ удалено");
            System.out.println(e);
        }
    }


    // метод добавления нового значения в словарь
    @FXML
    public boolean addDict() {
        try {
            txtId.setVisible(false);
            dict item = new dict();

            if (checkCode(txtCode.getText())) { // проверка, что в словарь вводится - или .
                if (checkRus(txtRus.getText())){ // проверка, что в словарь вводится буква
                    // проверка, что в словаре нет данного значения
                    if (checkData2(txtRus.getText().toUpperCase(), txtCode.getText())) {
                        item.setRus(txtRus.getText().toUpperCase());
                        item.setCode(txtCode.getText());
                        Database database = new Database();
                        database.addDict(item); // добавление значения через DAO класс

                        window("Значение добавлено");
                        updateTable();
                        search();
                        clear();
                    } }}
        } catch (Exception e) {
                window("Значение НЕ добавлено");
                System.out.println(e);
            }
        return false;
    }

    // метод редактирования значения
    @FXML
    public void Edit() {
        try {
            txtId.setVisible(false);
            String value1 = txtId.getText();
            String value2 = txtRus.getText().toUpperCase();
            String value3 = txtCode.getText();
            if (checkCode(txtCode.getText())) { // проверка, что в словарь вводится - или .
                if (checkRus(value2)) { // проверка, что в словарь вводится буква
                    if (checkData(String.valueOf(value1), value2, value3)){
                        Database database = new Database();
                        database.Edit(value1, value2, value3);

                        window("Значение изменено");
                        updateTable();
                        search();
                    }}}
        } catch (Exception e) {
            window("Значение НЕ изменено");
            System.out.println(e);
        }
    }


    // метод Импорта словаря
    public boolean upload() throws IOException, SQLException, ClassNotFoundException {
        // FileChooser открывает вспомогающее окно с директорией проекта, где пользователь может выбарть
        // нужный для импорта файл
        FileChooser file = new FileChooser();
        File selectedFile = file.showOpenDialog(null);

        if (selectedFile != null){
            try {
                dict item = new dict();
                // чтение файла построчно
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    int index = line.lastIndexOf(' ');
                    if ((index == -1) || (line.split(" ").length != 2)) {
                        // неверный формат
                        window("Некорректный ввод данных.\nВ файле должны быть значения в формате: буква пробел код");
                    } else {
                        // проверка, что значения нет в словаре
                        if (checkData2(line.substring(0, index).toUpperCase().trim(), line.substring(index).trim())) {
                            // проверка, что буква введена корректно
                            if (checkRus(line.substring(0, index).toUpperCase().trim())){
                                // проверка, что код введён корректно
                                if (checkCode(line.substring(index+1).trim())) {
                                    item.setRus(line.substring(0, index).toUpperCase().trim());
                                    item.setCode(line.substring(index).trim());
                                    Database database = new Database();
                                    database.addDict(item);
                                } else {return false;}
                            } else {return false;}
                        } else { return false; }
                    }}
                reader.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        updateTable();
        search();
        clear();
        return true;
    }

    //Проверка на то, что значений нет в словаре без проверки по ID. Нужно для upload и addDict
    public boolean checkData2(String rus, String code) throws SQLException, ClassNotFoundException {
        ObservableList<dict> data = Database.getDict();
        for (int i = 0; i < data.size(); i++){

            dict item = (dict) data.get(i);
            if ((item.getRus()).equals(rus)) {
                window("Введённая буква " + item.getRus() +" уже есть в словаре");
                return false;
            } else if ((item.getCode()).equals(code)){
                window("Введённый код "+ item.getCode() +" уже есть в словаре");
                return false;
            }
        }
        return true;
    }

    //Проверка на то, что значений нет в словаре с проверкой по ID. Нужно для edit
    public boolean checkData(String id, String rus, String code) throws SQLException, ClassNotFoundException {
        ObservableList<dict> data = Database.getDict();
        for (int i = 0; i < data.size(); i++){
            dict item = (dict) data.get(i);
            if (!String.valueOf(item.getId()).equals(id)){ //проверка по id
                if ((item.getRus()).equals(rus)) {
                    window("Введённая буква " + item.getRus() +" уже есть в словаре");
                    return false;
                } else if ((item.getCode()).equals(code)){
                    window("Введённый код "+ item.getCode() +" уже есть в словаре");
                    return false;
                }}
        }
        return true;
    }


    // переход на страницу главного меню при нажатии на кнопку-картинку Домой.
    @FXML
    public void homeBtn1() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/morsecode/home.fxml"));
        Stage window = (Stage) homebtn.getScene().getWindow();
        window.setScene(new Scene(root, 700, 400));
    }

    @FXML
    void initialize() {
        updateTable();
        try {
            search();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // метод выделения строки в таблице и переноса значений в поля для ввода
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        int index = -1;
        txtId.setVisible(false);
        index = tableDict.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtId.setText(colId.getCellData(index).toString());
        txtRus.setText(colRus.getCellData(index).toString());
        txtCode.setText(colCode.getCellData(index).toString());
    }


    // обновление таблицы
    public void updateTable() {
        txtId.setVisible(false);
        colId.setCellValueFactory(new PropertyValueFactory<dict, Integer>(Const.DICT_ID));
        colRus.setCellValueFactory(new PropertyValueFactory<dict, String>(Const.DICT_RUS));
        colCode.setCellValueFactory(new PropertyValueFactory<dict, String>(Const.DICT_CODE));

        ObservableList<dict> listM = null;

        try {
            listM = Database.getDict();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tableDict.setItems(listM);
    }

    // поиск значений про критерию пользователя
    @FXML
    void search() throws SQLException, ClassNotFoundException {
        txtId.setVisible(false);
        colId.setCellValueFactory(new PropertyValueFactory<dict, Integer>("id"));
        colRus.setCellValueFactory(new PropertyValueFactory<dict, String>("rus"));
        colCode.setCellValueFactory(new PropertyValueFactory<dict, String>("code"));


        ObservableList<dict> dataList;
        dataList = Database.getDict(); // получаем значения из словаря
        tableDict.setItems(dataList);
        FilteredList<dict> filteredData = new FilteredList<>(dataList, b -> true);
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //Integer newel = Integer.valueOf(newValue.trim());
                String lowerCaseFilter = newValue.toLowerCase();
                String q = newValue;
                // сам поиск
                if (person.getRus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (person.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(person.getId()).contains(q)) {
                    return true;
                } else {
                    return false;
                }
            });
            // отображение в таблице найденных по критерию значений
            SortedList<dict> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableDict.comparatorProperty());
            tableDict.setItems(sortedData);
        });
    }

    // метод используется для того, чтобы вписать букву в поле ввода. Когда кодируемого значения нет в словаре
    // появляется окно с предложением добавить это значение в словарь. Если пользователь соглашается,
    // открывается словарь с установленным значением для буквы.
    public void showLetter(String letter){
        txtRus.setText(letter);
    }

    // то же самое как с буквой только для кода
    public void showCode(String code){
        txtCode.setText(code);
    }

    // очистка полей ввода
    public void clear() {
        txtId.setText("");
        txtRus.setText("");
        txtCode.setText("");
    }

    // вспомогательное окно с информацией
    public void window(String wind){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(wind);
        alert.showAndWait();
    }

    // проверка, что пользователь ввёл - или .
    public boolean checkCode(String code) {
        for (int j = 0; j < code.length(); j++) {
            String ent = String.valueOf(code.charAt(j));
            if (!ent.contains("-")) {
                if (!ent.contains(".")){
                    window("Вы ввели не - или не .");
                    return false;}}
        }
        return true;
    }

    // проверка, что пользователь ввёл одну русскую букву 
    public boolean checkRus(String rus) {
        String regex = "[а-яА-Я]";
        if (rus.length()>1){
            window("Вы ввели не одну букву");
            return false;
        } else if (!rus.matches(regex)) {
            window("Вы ввели не букву от А до Я");
            return false;}
        return true;
    }
}



