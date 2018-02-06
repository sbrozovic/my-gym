package com.example.sara.mygym.Modules.Messanger.Item;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.example.sara.mygym.Common.Models.Messenger.Message;
import com.example.sara.mygym.Common.Models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sara on 5.12.2017..
 */

public class Chat implements Parent<ChatChild>{
    private Person personThatYouCommunicateWith;
    private boolean isAnonymous;
    private Message lastMessage;
    private List<ChatChild> chatChildList = new ArrayList<>();

    public Chat(Person personThatYouCommunicateWith, boolean isAnonymous, Message lastMessage){
        this.personThatYouCommunicateWith = personThatYouCommunicateWith;
        this.isAnonymous = isAnonymous;
        this.lastMessage = lastMessage;
    }

    public Person getPersonThatYouCommunicateWith() {
        return personThatYouCommunicateWith;
    }

    public void setPersonThatYouCommunicateWith(Person personThatYouCommunicateWith) {
        this.personThatYouCommunicateWith = personThatYouCommunicateWith;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setChildObjectList(List<ChatChild> list) {
        chatChildList = list;
    }
    @Override
    public List<ChatChild> getChildList() {
        return chatChildList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
