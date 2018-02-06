package com.example.sara.mygym.Common.Network.Wrappers;

import com.example.sara.mygym.Common.Models.Role;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara on 17.1.2018..
 */

public class RoleModel {
    @SerializedName("Id")
    private int id;
    @SerializedName("RoleName")
    private String roleName;

    public RoleModel(Role role){
        this.id = role.getId();
        if(role.getName().toLowerCase().equals("staff")){
            this.roleName = "Zaposlenik";                           //because of the lazy backend
        }
        else if(role.getName().toLowerCase().equals("boss")){
            this.roleName = "Gazda";                                //because of the lazy backend
        }
        else{
            this.roleName = "Član";                                 //because of the lazy backend
        }

    }

    public Role getRole(){
        String name;
        if(this.roleName.toLowerCase().startsWith("z")){
            name = "staff";
        }
        else if(this.roleName.toLowerCase().startsWith("g")){
            name = "boss";
        }
        else {
            name = "member";
        }
        return new Role(this.id, name);
    }
}
