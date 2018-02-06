package com.example.sara.mygym.Modules.Members.Item;

/**
 * Created by Sara on 3.12.2017..
 */

public class MemberChild {
    private String logOutLogin, membership, edit;
    public MemberChild(String logOutLogin, String membership, String edit){
        this.logOutLogin = logOutLogin;
        this.membership = membership;
        this.edit = edit;
    }

    public String getLogOutLogin() {
        return logOutLogin;
    }

    public void setLogOutLogin(String logOutLogin) {
        this.logOutLogin = logOutLogin;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }
}
