package com.example.sara.mygym.Common.Models.News;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.example.sara.mygym.Modules.News.BossOnly.Item.NewsChild;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sara on 6.12.2017..
 */

public class News implements Parent<NewsChild> {
    private List<NewsChild> newsChildList = new ArrayList<>();
    private int id;
    private Date date;
    private String subject, newsText;

    public News(int id,  String subject, String newsText,Date date){
        this.id = id;
        this.date = date;
        this.subject = subject;
        this.newsText = newsText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNewsChildList(List<NewsChild> newsChildList) {
        this.newsChildList = newsChildList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    @Override
    public List<NewsChild> getChildList() {
        return newsChildList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
