package org.morsecode.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.morsecode.Const;
import org.morsecode.HibernateSessionFactoryUtil;
import org.morsecode.models.User;
import org.morsecode.models.dict;
import org.hibernate.*;
import org.morsecode.models.message;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// DAO класс, в котором прописываются все взаимодействия с базой данных.
public class Database {

    /////////////////////// USER ///////////////////////
    // Сохранить пользователя
    public void Save_user(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        session.save(user); // ORM сохранение пользователя

        tx1.commit();
        session.close();
    }


    // возвращает 1, если логин и пароль пользователя существуют в БД
    public int getUser(User user) throws SQLException, ClassNotFoundException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        ResultSet resSet = null;

        int stat;
        SQLQuery query = session.createSQLQuery("SELECT * FROM " + Const.USER_TABLE + " WHERE username= '"+ user.getUsername()
                + "' AND password= '" +user.getPassword() + "'");

        List<Object[]> rows = query.list();
        stat=rows.size();

        tx1.commit();
        session.close();
        return stat;
    }

    // получить всех пользователей
    public static ObservableList<User> getUserAll(){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        ObservableList<User> list = FXCollections.observableArrayList();

        Query ps = session.createSQLQuery("SELECT * FROM " + Const.USER_TABLE);
        List <Object[]> rs = ps.list();

        for (Object[] row : rs ) {
            list.add(new User(row[0].toString(), row[1].toString(), row[2].toString(), row[3].toString(),
                    row[4].toString(), row[5].toString()));
        }

        tx1.commit();
        session.close();
        return list;
    }


    ////////////////////// DICT /////////////////////////////
    // Получить все значения в таблице dict
    public static ObservableList<dict> getDict() throws SQLException, ClassNotFoundException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        ObservableList<dict> list = FXCollections.observableArrayList();

        Query ps = session.createSQLQuery("SELECT * FROM " + Const.DICT_TABLE);
        List <Object[]> rs = ps.list();

        for (Object[] row : rs ){
            list.add(new dict (Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString()));
        }

        tx1.commit();
        session.close();
        return list;
    }

    // удалить значение из таблицы dict
    public int Delete_dict(Integer id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        int stat;
        int res;
        SQLQuery query = session.createSQLQuery("Select " + Const.DICT_ID + " from " + Const.DICT_TABLE + " where id = " + id);

        List<Integer> rows = query.list();
        stat=rows.get(0);
        res=rows.size();
        dict item =session.find(dict.class , stat);
        session.delete(item); // ORM удаление

        tx1.commit();
        session.close();

        return res;
    }

    public static String unknownLetter; // используется для добавления в словаре в поле ввода неизвестную букву
    // кодирование сообщение с русского на код Морзе
    public static String encoding(String enter) throws IOException, SQLException, ClassNotFoundException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        List list = getDict(); // получение всех значений из словаря
        String rez = "";

        for (int j = 0; j < enter.length(); j++) {
            String ent = String.valueOf(enter.charAt(j)).toUpperCase();
            int c = 0;
            if (ent.equals(" ")){
                rez = rez + " ";
                c += 1;}
            // проходимся по каждому значению в словаре и ищем совпадающую букву
            for (int i = 0; i < list.size(); i++) {
                dict item = (dict) list.get(i);
                if ((item.getRus()).equals(ent)) { // проверка, что введённая буква совпадает с буквой в словаре
                    String m = item.getCode(); // получаем код
                    rez = rez + m; // добавляем его к результату
                    c += 1;
                }
            }
            if(c == 0){
                // если значение не найдено, открывается вспомогательное окно и пользователь может выбрать
                // добавлять новое значение в словарь или нет
                unknownLetter = ent;
                FXMLLoader fxmlLoader = new FXMLLoader(Database.class.getResource("/org/morsecode/confWindow.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
                return "false";
            }
        }
        tx1.commit();
        session.close();
        return rez;
    }

    // кодирование сообщение с кода Морзе на русский
    public static String encoding2(String enter) throws IOException, SQLException, ClassNotFoundException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        List list = getDict(); // получение всех значений из словаря
        String rez = "";
        ObservableList<dict> rus = FXCollections.observableArrayList();

        String[] letters = enter.split(" ");

        for (int j = 0; letters.length > j; j++) {
            int c = 0;
            if (letters[j].equals(";")) {
                rez = rez + " ";
                c += 1;
            }
            // проходимся по каждому значению в словаре и ищем совпадающую букву
            for (int i = 0; i < list.size(); i++) {
                dict item = (dict) list.get(i);
                if ((item.getCode()).equals(letters[j])) { // проверка, что код совпадает с кодом в словаре
                    String m = item.getRus(); // получаем букву
                    rez = rez + m; // добавляем её к результату
                    c += 1;
                }
            }
            if (c == 0 && !letters[j].equals(" ")) {
                // если значение не найдено, открывается вспомогательное окно и пользователь может выбрать
                // добавлять новое значение в словарь или нет
                unknownLetter = letters[j];
                FXMLLoader fxmlLoader = new FXMLLoader(Database.class.getResource("/org/morsecode/confWindow2.fxml"));
                Parent root2 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root2));
                stage.show();
                return "false";
                }
        }
        tx1.commit();
        session.close();
        return rez;
    }

    // метод обновляющий значение в словаре
    public void Edit(String value1, String value2 , String value3) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        Query query = session.createSQLQuery( "update " + Const.DICT_TABLE + " set id = '" + value1 + "', rus= '" + value2 + "', code= '" + value3 + "' where id='" + value1 + "' ");
        query.executeUpdate();

        tx1.commit();
        session.close();
    }

    // метод добавляющий новое значение в словарь
    public void addDict(dict item) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        session.save(item);

        tx1.commit();
        session.close();
    }

    /////////////////////// MESSAGES ///////////////////////
    // метод добавления сообщения в словарь
    public void addMes(message item){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        session.save(item);

        tx1.commit();
        session.close();
    }

    // метод ищущий самое популярное сообщение от пользователя
    public List stats(){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        SQLQuery query = session.createSQLQuery("SELECT COUNT(input) AS CountInput, input " +
                "FROM messages " +
                "GROUP BY input " +
                "ORDER BY count(*) DESC " +
                "LIMIT 1;");

        List <Object[]> rs = query.list();



        ObservableList list = FXCollections.observableArrayList();

        for (Object[] row : rs ) {
            list.add(Integer.parseInt(row[0].toString())); // сколько раз встретилось самое популярное сообщение
            list.add(row[1].toString()); // самое популярное сообщение
        }
        tx1.commit();
        session.close();
        return list;
    }
}