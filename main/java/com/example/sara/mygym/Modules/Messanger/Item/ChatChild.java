package com.example.sara.mygym.Modules.Messanger.Item;

/**
 * Created by Sara on 5.12.2017..
 */

public class ChatChild {
    private String answer, discard;
    public ChatChild(String answer, String discard){
        this.answer = answer;
        this.discard = discard;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDiscard() {
        return discard;
    }

    public void setDiscard(String discard) {
        this.discard = discard;
    }
}
