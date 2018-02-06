package com.example.sara.mygym.Modules.Staff.OnlyStaff.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.sara.mygym.Modules.Staff.StaffFragment;
import com.example.sara.mygym.Common.Models.Staff.Staff;
import com.example.sara.mygym.Modules.Staff.Holders.StaffViewHolder;
import com.example.sara.mygym.Modules.Staff.OnlyBoss.Item.StaffChild;
import com.example.sara.mygym.Modules.Staff.OnlyStaff.Holders.StaffChildViewHolderForStaff;
import com.example.sara.mygym.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sara on 8.12.2017..
 */

public class StaffExpandableAdapterForStaff extends ExpandableRecyclerAdapter<Staff, StaffChild, StaffViewHolder, StaffChildViewHolderForStaff> {

    LayoutInflater mInflater;
    StaffFragment staffFragment;
    List<Staff> staffList = new ArrayList<>();

    public StaffExpandableAdapterForStaff(Context context, List<Staff> staffList, StaffFragment staffFragment){
        super(staffList);
        mInflater = LayoutInflater.from(context);
        this.staffFragment = staffFragment;
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
    public StaffChildViewHolderForStaff onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View staffView = mInflater.inflate(R.layout.recycler_view_child_staff_for_staff, childViewGroup, false);
        return new StaffChildViewHolderForStaff(staffView);
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
    public void onBindChildViewHolder(@NonNull StaffChildViewHolderForStaff childViewHolder, final int parentPosition, int childPosition, @NonNull StaffChild child) {
        childViewHolder.call.setText(child.getCall());
        childViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCall(parentPosition);
            }
        });
    }
    private void doCall(int parentPosition) {
        staffFragment.callStaff(staffList.get(parentPosition).getPhone());
    }
}
