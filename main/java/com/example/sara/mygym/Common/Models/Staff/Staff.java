package com.example.sara.mygym.Common.Models.Staff;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.example.sara.mygym.Common.Models.Account;
import com.example.sara.mygym.Common.Models.Members.Membership;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Models.Role;
import com.example.sara.mygym.Modules.Staff.OnlyBoss.Item.StaffChild;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sara on 3.12.2017..
 */

public class Staff extends Person implements Parent<StaffChild>{
    private List<StaffChild> staffChildList = new ArrayList<>();

    public Staff(int id, String name, String surname, Date birthDate, boolean isMale, Role role, String phone, String address, boolean isInGym, Account account, boolean isActive) {
        super(id, name, surname, birthDate, isMale, role, null ,account,  phone, address,isInGym,isActive );
    }

    public void setChildObjectList(List<StaffChild> list) {
        staffChildList = list;
    }

    @Override
    public List<StaffChild> getChildList() {
        return staffChildList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
