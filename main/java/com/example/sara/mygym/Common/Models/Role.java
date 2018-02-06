package com.example.sara.mygym.Common.Models;

import java.io.Serializable;

/**
 * Created by Sara on 13.12.2017..
 */

public class Role implements Serializable {
    private int id;
    private String name;

    public Role(int id, String name){
        this.id = id;
        this.name =name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
