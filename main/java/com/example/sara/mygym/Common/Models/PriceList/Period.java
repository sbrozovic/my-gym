package com.example.sara.mygym.Common.Models.PriceList;


/**
 * Created by Sara on 9.1.2018..
 */

public class Period {
    int id;
    String name;
    int price;
    int duration;

    public Period(int id, String name, int price, int duration){
        this.duration = duration;
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return this.id;
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

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
