package com.example.sara.mygym.Common.Models.Messenger;


import com.example.sara.mygym.Common.Models.Person;

import java.io.Serializable;

/**
 * Created by Sara on 20.12.2017..
 */

public class Message implements Serializable {
    int id;
    String message;
    Person sender;
    Person receiver;
    Person owner;
    boolean isAnonymous;
    long createdAt;
    boolean didUserSendTheMessage;

    public Message(int id, String message, Person sender, Person receiver,Person owner, long createdAt, boolean isAnonymous, boolean didUserSendTheMessage){
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.owner = owner;
        this.createdAt = createdAt;
        this.receiver = receiver;
        this.isAnonymous = isAnonymous;
        this.didUserSendTheMessage = didUserSendTheMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Person getSender() {
        return sender;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public boolean getDidUserSendTheMessage() {
        return didUserSendTheMessage;
    }

    public void setDidUserSendTheMessage(boolean didUserSendTheMessage) {
        this.didUserSendTheMessage = didUserSendTheMessage;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Person getReceiver() {
        return receiver;
    }

    public void setReceiver(Person receiver) {
        this.receiver = receiver;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
