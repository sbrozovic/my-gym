package com.example.sara.mygym.Modules.Staff.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 4.12.2017..
 */

public class StaffViewHolder extends ParentViewHolder {
    public TextView staffName;
    public ImageView imagePresent;

    public StaffViewHolder(View itemView) {
        super(itemView);
        staffName = (TextView) itemView.findViewById(R.id.member_name);
        imagePresent = (ImageView) itemView.findViewById(R.id.imageViewPresentNotPresent);
    }
}
