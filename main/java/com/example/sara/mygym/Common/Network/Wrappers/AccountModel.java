package com.example.sara.mygym.Common.Network.Wrappers;

import com.example.sara.mygym.Common.Models.Account;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara on 17.1.2018..
 */

public class AccountModel {
    @SerializedName("Id")
    private int id;
    @SerializedName("Email")
    private String email;
    @SerializedName("Password")
    private String password;
    @SerializedName("Photo")
    private String photo;
    public AccountModel(Account account){
        this.id = account.getId();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.photo = account.getPhoto();
    }

    public Account getAccount(){
        return new Account(this.id, this.email, this.password, this.photo);
    }
}
