package com.example.sara.mygym.Common.Models.Members;

import java.util.Date;
import java.util.List;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.example.sara.mygym.Common.Models.Account;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Models.Role;
import com.example.sara.mygym.Modules.Members.Item.MemberChild;

/**
 * Created by Sara on 1.11.2017..
 */

public class Members extends Person implements Parent<MemberChild> {
    private List<MemberChild> membersViewList;
    private Membership membership;

    public Members(int id, String name, String surname, Date birthDate, boolean isMale, Role role, String phone, String address, boolean isInGym, boolean isActive, Membership membership, Account account) {
        super(id, name, surname, birthDate, isMale, role,membership ,account,  phone, address,isInGym,isActive );
        this.membership = membership;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public void setChildObjectList(List<MemberChild> list) {
        membersViewList = list;
    }

    @Override
    public List<MemberChild> getChildList() {
        return membersViewList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
