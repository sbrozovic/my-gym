package com.example.sara.mygym.Modules.Staff.OnlyBoss.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.sara.mygym.Modules.Staff.StaffFragment;
import com.example.sara.mygym.Common.Models.Staff.Staff;
import com.example.sara.mygym.Modules.Staff.OnlyBoss.Item.StaffChild;
import com.example.sara.mygym.Modules.Staff.Holders.StaffViewHolder;
import com.example.sara.mygym.Modules.Staff.OnlyBoss.Holders.StaffChildViewHolder;
import com.example.sara.mygym.R;

import java.util.List;

/**
 * Created by Sara on 3.12.2017..
 */

public class StaffExpandableAdapter extends ExpandableRecyclerAdapter<Staff, StaffChild, StaffViewHolder, StaffChildViewHolder> {
    LayoutInflater mInflater;
    StaffFragment staffFragment;
    List<Staff> staffList;

    public StaffExpandableAdapter(Context context, List<Staff> staffList, StaffFragment staffFragment){
        super(staffList);
        mInflater = LayoutInflater.from(context);
        this.staffFragment =staffFragment;
        this.staffList = staffList;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View staffView = mInflater.inflate(R.layout.recyclerview_parent_list_row_members_and_stuff, parentViewGroup, false);
        return new StaffViewHolder(staffView);
    }

    @NonNull
    @Override
    public StaffChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View staffView = mInflater.inflate(R.layout.recyclerview_child_staff, childViewGroup, false);
        return new StaffChildViewHolder(staffView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull StaffViewHolder parentViewHolder, int parentPosition, @NonNull Staff parent) {
        parentViewHolder.staffName.setText(parent.getName()+ " "+parent.getSurname());
        if(parent.isInGym()==true){
            parentViewHolder.imagePresent.setImageResource(R.mipmap.ic_active);
        }
        else{
            parentViewHolder.imagePresent.setImageResource(R.mipmap.ic_expired);
        }
    }

    @Override
    public void onBindChildViewHolder(@NonNull StaffChildViewHolder childViewHolder, final int parentPosition, int childPosition, @NonNull StaffChild child) {
        childViewHolder.call.setText(child.getCall());
        childViewHolder.edit.setText(child.getEdit());

        childViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnCall(parentPosition);
            }
        });
        childViewHolder.callImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnCall(parentPosition);
            }
        });
        childViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnEdit(staffList.get(parentPosition));
            }
        });
        childViewHolder.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnEdit(staffList.get(parentPosition));
            }
        });
    }
    private void clickOnEdit(Staff staff) {
        staffFragment.openEditStaff(staff);
    }

    private void clickOnCall(int parentPosition) {
        staffFragment.callStaff(staffList.get(parentPosition).getPhone());
    }
}
