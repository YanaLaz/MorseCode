package org.morsecode.models;

import javax.persistence.*;


/**
 * класс, который является моделью класса dictController.java, в котором прописываются поля, используемые в БД
 */
@Entity
@Table(name="dict")
public class dict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String rus;
    private String code;

    public dict(int id, String rus, String code) {
        this.id = id;
        this.rus = rus;
        this.code = code;
    }

    public dict() {

    }

    public int getId() {
        return id;
    }

    public String getRus() {
        return rus;
    }

    public String getCode() {
        return code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRus(String rus) {
        this.rus = rus;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
