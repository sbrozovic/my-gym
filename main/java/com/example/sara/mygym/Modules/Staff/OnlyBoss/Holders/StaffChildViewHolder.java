package com.example.sara.mygym.Modules.Staff.OnlyBoss.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 4.12.2017..
 */

public class StaffChildViewHolder extends ChildViewHolder {
    public TextView call, edit;
    public ImageView callImage, editImage;
    public StaffChildViewHolder(View itemView){
        super(itemView);
        call = (TextView) itemView.findViewById(R.id.textViewCall);
        callImage = (ImageView) itemView.findViewById(R.id.imageViewCall);

        edit = (TextView) itemView.findViewById(R.id.textViewSettings);
        editImage = (ImageView) itemView.findViewById(R.id.imageViewSettings);

    }
}
