package com.example.sara.mygym.Common.Models;

import com.example.sara.mygym.Common.Models.Members.Membership;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sara on 13.12.2017..
 */

public class Person implements Serializable{
    private int id;
    private String name;
    private String surname;
    private Date birthDate;
    private boolean isMale;
    private Role role;
    private String phone;
    private Account account;
    private String address;
    private boolean isInGym;
    private boolean isActive;
    private Membership membership;

    public Person(int id, String name, String surname, Date birthDate, boolean isMale, Role role,Membership membership, Account account, String phone, String address, boolean isInGym, boolean isActive){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.isMale = isMale;
        this.role = role;
        this.phone = phone;
        this.address = address;
        this.isInGym = isInGym;
        this.account = account;
        this.isActive = isActive;
        this.membership = membership;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isInGym() {
        return isInGym;
    }

    public void setInGym(boolean inGym) {
        isInGym = inGym;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        return id == person.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
