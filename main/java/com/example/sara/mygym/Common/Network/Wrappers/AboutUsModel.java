package com.example.sara.mygym.Common.Network.Wrappers;

import com.example.sara.mygym.Common.Models.AboutUs;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara on 14.1.2018..
 */

public class AboutUsModel {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("WorkTime")
    private String workTime;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("Email")
    private String email;
    @SerializedName("Address")
    private String address;

    public AboutUsModel(AboutUs aboutUs){
        this.id = aboutUs.getId();
        this.name = aboutUs.getName();
        this.workTime = aboutUs.getWorkingTime();
        this.phone = aboutUs.getPhone();
        this.email = aboutUs.getMail();
        this.address = aboutUs.getAddress();
    }

    public AboutUs getAboutUs(){
        return new AboutUs(this.id, this.name, this.workTime, this.phone, this.email, this.address);
    }
}
