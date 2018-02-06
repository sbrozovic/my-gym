package com.example.sara.mygym.Common.Models;

import java.io.Serializable;

/**
 * Created by Sara on 14.1.2018..
 */

public class Account implements Serializable{
    private int id;
    private String email;
    private String password;
    private String photo;

    public Account(int id, String email, String password, String photo){
        this.id = id;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
