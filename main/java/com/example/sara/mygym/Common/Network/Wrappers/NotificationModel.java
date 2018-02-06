package com.example.sara.mygym.Common.Network.Wrappers;

import com.example.sara.mygym.Common.Models.News.News;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sara on 14.1.2018..
 */

public class NotificationModel {
    @SerializedName("Id")
    private int id;
    @SerializedName("Subject")
    private String subject;
    @SerializedName("Text")
    private String text;
    @SerializedName("DateTime")
    private String dateTime;

    public NotificationModel(News news) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.id = news.getId();
        this.dateTime = formatter.format(news.getDate());
        this.subject = news.getSubject();
        this.text = news.getNewsText();
    }

    public News getNews() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        String day = this.dateTime.substring(8, 10);
        String month = this.dateTime.substring(5, 7);
        String year = this.dateTime.substring(0, 4);
        String hour = this.dateTime.substring(11, 13);
        String min = this.dateTime.substring(14, 16);
        try {
            return new News(this.id, this.subject, this.text, formatter.parse(day + "-" + month + "-" + year + " " + hour + ":" + min));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
