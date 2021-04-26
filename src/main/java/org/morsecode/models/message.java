package org.morsecode.models;

import javax.persistence.*;

// класс–модель, в котором прописываются все поля, которые будут изъяты из БД и в дальнейшем записаны в БД.
@Entity
@Table(name="messages")
public class message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String input;
    private String output;
    private String username;

    public message(int id, String input, String output, String username) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.username = username;
    }

    public message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
