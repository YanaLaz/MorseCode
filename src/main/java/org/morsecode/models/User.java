package org.morsecode.models;

import javax.persistence.*;

/**
 *  модель класса signUpController.java, в которой прописываются поля в дальнейшем используемые для записи в БД.
 */
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String location;
    private String gender;


    public User(String firstName, String lastName, String userName, String password, String location, String gender) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.username = userName;
        this.password = password;
        this.location = location;
        this.gender = gender;
    }

    public User() {

    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return "Id: " + id + " , FirstName: "+ firstname +" , Lastname: " +lastname + " , Username: " + username + " , Password: " + password + " , Location: " + location + " , Gender: " + gender ;
    }


}
