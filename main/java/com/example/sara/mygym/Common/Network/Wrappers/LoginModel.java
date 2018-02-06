package com.example.sara.mygym.Common.Network.Wrappers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara on 20.1.2018..
 */

public class LoginModel {
    @SerializedName("Email")
    private String email;
    @SerializedName("Password")
    private String password;

    public LoginModel(String email, String password){
        this.email = email;
        this.password = password;
    }
}
