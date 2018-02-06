package com.example.sara.mygym.Modules.Staff.OnlyStaff.Holders;

import android.view.View;
import android.widget.Button;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 8.12.2017..
 */

public class StaffChildViewHolderForStaff extends ChildViewHolder {
    public Button call;


    public StaffChildViewHolderForStaff(View view) {
        super(view);
        call = (Button) view.findViewById(R.id.buttonCall);

    }
}
