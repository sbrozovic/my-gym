package com.example.sara.mygym.Common.Network.Wrappers;

import com.example.sara.mygym.Common.Models.Members.Membership;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sara on 14.1.2018..
 */

public class MembershipModel {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Price")
    private int price;
    @SerializedName("DateFrom")
    private String dateFrom;
    @SerializedName("DateTo")
    private String dateTo;
    @SerializedName("Status")
    private boolean status;

    public MembershipModel(Membership membership){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.id = membership.getId();
        this.name = membership.getName();
        this.price = membership.getPrice();
        this.dateFrom = formatter.format(membership.getMembershipStartsOn());
        this.dateTo = formatter.format(membership.getMembershipActiveUntil());
        this.status = membership.isStatus();
    }

    public Membership getMembership(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String day = this.dateFrom.substring(8,10);
        String month = this.dateFrom.substring(5,7);
        String year = this.dateFrom.substring(0,4);

        String day1 = this.dateTo.substring(8,10);
        String month1 = this.dateTo.substring(5,7);
        String year1 = this.dateTo.substring(0,4);

        try {
            return new Membership(this.id, this.name, this.price, formatter.parse(day+"-"+month+"-"+year), formatter.parse(day1+"-"+month1+"-"+year1), this.status);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
