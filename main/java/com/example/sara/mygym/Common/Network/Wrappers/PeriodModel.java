package com.example.sara.mygym.Common.Network.Wrappers;

import com.example.sara.mygym.Common.Models.PriceList.Period;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara on 14.1.2018..
 */

public class PeriodModel {
    @SerializedName("Id")
    int id;
    @SerializedName("Name")
    String name;
    @SerializedName("Price")
    int price;
    @SerializedName("Duration")
    int duration;

    public PeriodModel(Period period){
        this.id = period.getId();
        this.name = period.getName();
        this.price = period.getPrice();
        this.duration = period.getDuration();
    }

    public Period getPeriod(){
        return new Period(this.id, this.name, this.price, this.duration);
    }
}
