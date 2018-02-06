package com.example.sara.mygym.Common.Network.Wrappers;

import com.example.sara.mygym.Common.Models.Messenger.Message;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sara on 14.1.2018..
 */

public class MessageModel {
    @SerializedName("Id")
    private int id;
    @SerializedName("Sender")
    private PersonModel sender;
    @SerializedName("Receiver")
    private PersonModel receiver;
    @SerializedName("Owner")
    private PersonModel owner;
    @SerializedName("IsAnonymous")
    private boolean isAnonymous;
    @SerializedName("Text")
    private String text;
    @SerializedName("DateTime")
    private String dateTime;

    public MessageModel(Message message) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.id = message.getId();
        this.text = message.getMessage();
        this.sender = new PersonModel(message.getSender());
        this.dateTime = formatter.format(new Date(message.getCreatedAt()));
        this.receiver = new PersonModel(message.getReceiver());
        this.owner = new PersonModel(message.getOwner());
        this.isAnonymous = message.isAnonymous();
    }

    public Message getMessage() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String day = this.dateTime.substring(8,10);
        String month = this.dateTime.substring(5,7);
        String year = this.dateTime.substring(0,4);
        String hour = this.dateTime.substring(11,13);
        String min = this.dateTime.substring(14,16);
        String sec = this.dateTime.substring(17,19);

        try {
            return new Message(this.id, this.text, this.sender.getPerson(), this.receiver.getPerson(), this.owner.getPerson(), formatter.parse(day+"-"+month+"-"+year+" "+hour+":"+min+":"+sec).getTime(), this.isAnonymous, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
