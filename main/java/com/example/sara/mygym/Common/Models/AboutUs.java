package com.example.sara.mygym.Common.Models;

/**
 * Created by Sara on 14.1.2018..
 */

public class AboutUs {
    private int id;
    private String name;
    private String workingTime;
    private String phone;
    private String mail;
    private String address;

    public AboutUs(int id, String name, String workingTime, String phone, String mail, String address){
        this.id = id;
        this.name = name;
        this.workingTime = workingTime;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
