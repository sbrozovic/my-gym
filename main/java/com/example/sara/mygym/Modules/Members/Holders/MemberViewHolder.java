package com.example.sara.mygym.Modules.Members.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 4.12.2017..
 */

public class MemberViewHolder extends ParentViewHolder{
    public TextView memberName;
    public ImageView imageActiveExpired;
    public  ImageView imageViewPresentNotPresent;

    public MemberViewHolder(View itemView) {
        super(itemView);
        memberName = (TextView) itemView.findViewById(R.id.member_name);
        imageActiveExpired = (ImageView) itemView.findViewById(R.id.imageViewActiveExpired);
        imageViewPresentNotPresent = (ImageView) itemView.findViewById(R.id.imageViewPresentNotPresent);
    }


}
