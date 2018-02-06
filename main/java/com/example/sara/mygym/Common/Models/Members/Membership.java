package com.example.sara.mygym.Common.Models.Members;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sara on 13.12.2017..
 */

public class Membership implements Serializable {
    private int id;
    //za name ce biti spoj perioda i tarife npr. mjesecna studenska
    private String name;
    //price cu dobiti izracunat iz baze
    private int price;

    private Date membershipStartsOn;
    //kod upisa clanarine odmah postavim
    private Date membershipActiveUntil;

    private boolean status;

    public Membership(int id, String name, int price,Date membershipStartsOn, Date membershipUntil, boolean status){
        this.id = id;
        this.name = name;
        this.price = price;
        this.membershipStartsOn = membershipStartsOn;
        this.membershipActiveUntil = membershipUntil;
        this.status = status;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getMembershipActiveUntil() {
        return membershipActiveUntil;
    }

    public void setMembershipActiveUntil(Date membershipUntil) {
        this.membershipActiveUntil = membershipUntil;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getMembershipStartsOn() {
        return membershipStartsOn;
    }

    public void setMembershipStartsOn(Date membershipStartsOn) {
        this.membershipStartsOn = membershipStartsOn;
    }
}
