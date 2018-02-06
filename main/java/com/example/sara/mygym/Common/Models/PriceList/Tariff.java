package com.example.sara.mygym.Common.Models.PriceList;

/**
 * Created by Sara on 9.1.2018..
 */

public class Tariff {
    int id;
    String name;
    int discount;

    public Tariff(int id, String name, int discount){
        this.id = id;
        this.discount = discount;
        this.name = name;
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
