package com.example.sara.mygym.Common.Network.Wrappers;

import com.example.sara.mygym.Common.Models.Account;
import com.example.sara.mygym.Common.Models.Members.Membership;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Models.Role;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sara on 14.1.2018..
 */

public class PersonModel {
    @SerializedName("Id")
    private int id;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("BirthDate")
    private String birthDate;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("Role")
    private RoleModel role;
    @SerializedName("Membership")
    private MembershipModel membership;
    @SerializedName("Account")
    private AccountModel account;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("Address")
    private String address;
    @SerializedName("PresentState")
    private boolean presentState;
    @SerializedName("IsActive")
    private boolean isActive;

    public PersonModel(Person person) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.id = person.getId();
        this.firstName = person.getName();
        this.lastName = person.getSurname();
        this.birthDate = formatter.format(person.getBirthDate());

        if (person.isMale()) {
            this.gender = "muški";                  //because of the lazy backend
        } else {
            this.gender = "ženski";
        }

        this.role = new RoleModel(person.getRole());
        this.phone = person.getPhone();
        this.address = person.getAddress();
        this.presentState = person.isInGym();
        this.account = new AccountModel(person.getAccount());
        this.isActive = person.isActive();
        if(!person.getRole().getName().equals("member")){
            this.membership = null;
        }
        else {
            this.membership = new MembershipModel(person.getMembership());
        }
    }

    public Person getPerson() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        String day = this.birthDate.substring(8, 10);
        String month = this.birthDate.substring(5, 7);
        String year = this.birthDate.substring(0, 4);
        boolean isMale;
        if (this.gender.toLowerCase().startsWith("m")) {
            isMale = true;
        } else {
            isMale = false;
        }
        try {
            if (this.membership == null) {
                return new Person(this.id, this.firstName, this.lastName, formatter.parse(day + "-" + month + "-" + year), isMale, this.role.getRole(), null, this.account.getAccount(), this.phone, this.address, this.presentState, this.isActive);
            } else
                return new Person(this.id, this.firstName, this.lastName, formatter.parse(day + "-" + month + "-" + year), isMale, this.role.getRole(), this.membership.getMembership(), this.account.getAccount(), this.phone, this.address, this.presentState, this.isActive);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
