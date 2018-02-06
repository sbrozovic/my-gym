package com.example.sara.mygym.Modules.Staff.OnlyMember;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sara.mygym.Common.Models.Staff.Staff;
import com.example.sara.mygym.Modules.Staff.Holders.StaffViewHolder;
import com.example.sara.mygym.R;

import java.util.List;

/**
 * Created by Sara on 6.12.2017..
 */

public class StaffAdapter extends RecyclerView.Adapter<StaffViewHolder>{
    private List<Staff> staffList;

    public StaffAdapter(List<Staff> staffList){
        this.staffList = staffList;
    }


    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_parent_list_row_members_and_stuff, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StaffViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        holder.staffName.setText(staff.getName()+" "+staff.getSurname());
        if(staff.isInGym()==true){
            holder.imagePresent.setImageResource(R.mipmap.ic_active);
        }
        else{
            holder.imagePresent.setImageResource(R.mipmap.ic_expired);
        }
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }
}
