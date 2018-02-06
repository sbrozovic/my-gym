package com.example.sara.mygym.Modules.Members.Holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 3.12.2017..
 */

public class MemberChildViewHolder extends ChildViewHolder{
    public TextView logoutLogin,membership, edit;
    public ImageView logoutLoginImage, membershipImage, editImage;
    Context context;

    public MemberChildViewHolder(View itemView) {
        super(itemView);
        logoutLogin =(TextView) itemView.findViewById(R.id.textViewLogOff);
        logoutLoginImage = (ImageView) itemView.findViewById(R.id.imageViewLogout);

        membership = (TextView) itemView.findViewById(R.id.textViewMembership);
        membershipImage = (ImageView) itemView.findViewById(R.id.imageViewMembershipCard);

        edit =(TextView) itemView.findViewById(R.id.textViewEditMember);
        editImage  = (ImageView) itemView.findViewById(R.id.imageViewEdit);
    }

}
