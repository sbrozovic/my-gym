package com.example.sara.mygym.Modules.News.BossOnly.Item;

/**
 * Created by Sara on 6.12.2017..
 */

public class NewsChild{
    private String delete, update;

    public NewsChild(String delete, String update){
        this.delete = delete;
        this.update = update;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
