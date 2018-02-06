package com.example.sara.mygym.Modules.Members.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.sara.mygym.Modules.Members.MembersFragment;
import com.example.sara.mygym.Modules.Members.Holders.MemberChildViewHolder;
import com.example.sara.mygym.Modules.Members.Holders.MemberViewHolder;
import com.example.sara.mygym.Modules.Members.Item.MemberChild;
import com.example.sara.mygym.Common.Models.Members.Members;
import com.example.sara.mygym.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sara on 1.11.2017..
 */

public class MembersAdapter extends ExpandableRecyclerAdapter<Members, MemberChild, MemberViewHolder, MemberChildViewHolder> {
    LayoutInflater mInflater;
    Context context;
    List<Members> membersList = new ArrayList<>();
    MembersFragment membersFragment;

    public MembersAdapter(Context context, List<Members> membersList, MembersFragment membersFragment){
        super(membersList);
        mInflater = LayoutInflater.from(context);
        this.membersList = membersList;
        this.context = context;
        this.membersFragment = membersFragment;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View memberView = mInflater.inflate(R.layout.recyclerview_parent_list_row_members_and_stuff, parentViewGroup, false);
        return new MemberViewHolder(memberView);
    }

    @NonNull
    @Override
    public MemberChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View memberView = mInflater.inflate(R.layout.recycler_view_child_member, childViewGroup, false);
        return new MemberChildViewHolder(memberView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull MemberViewHolder parentViewHolder, int parentPosition, @NonNull Members parent) {
        parentViewHolder.memberName.setText(parent.getName()+" "+parent.getSurname());
        long diff = (parent.getMembership().getMembershipActiveUntil().getTime() - (new Date()).getTime());
        long days = diff/(24*60*60*1000);
        long hours = diff/(60*60*1000);
        if(new Date().after(parent.getMembership().getMembershipActiveUntil())){
            parentViewHolder.imageActiveExpired.setImageResource(R.drawable.alert_red);
        }
        else if(days<=7){
            parentViewHolder.imageActiveExpired.setImageResource(R.drawable.alert_yellow);
        }
        if(parent.isInGym()){
            parentViewHolder.imageViewPresentNotPresent.setImageResource(R.mipmap.ic_active);
        }
        else if(!parent.isInGym()){
            parentViewHolder.imageViewPresentNotPresent.setImageResource(R.mipmap.ic_expired);
        }
    }

    @Override
    public void onBindChildViewHolder(@NonNull final MemberChildViewHolder childViewHolder, final int parentPosition, int childPosition, @NonNull final MemberChild child) {
        childViewHolder.edit.setText(child.getEdit());

        childViewHolder.membership.setText(child.getMembership());
        if(membersList.get(parentPosition).isInGym()){
            childViewHolder.logoutLogin.setText("Check out");
            childViewHolder.logoutLoginImage.setImageResource(R.drawable.logout);
        }
        else if(!membersList.get(parentPosition).isInGym()){
            childViewHolder.logoutLogin.setText("Check in");
            childViewHolder.logoutLoginImage.setImageResource(R.drawable.login);
        }
        childViewHolder.logoutLoginImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnLogoutLogin(v, child, parentPosition, childViewHolder);
            }
        });
        childViewHolder.logoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnLogoutLogin(v, child, parentPosition, childViewHolder);
            }
        });

        childViewHolder.membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnMembership(v, child, parentPosition, childViewHolder);
            }
        });
        childViewHolder.membershipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnMembership(v, child, parentPosition, childViewHolder);
            }
        });

        childViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnEdit(v, child, parentPosition, childViewHolder);
            }
        });
        childViewHolder.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnEdit(v, child, parentPosition, childViewHolder);
            }
        });
    }
    private void clickOnLogoutLogin(View v, MemberChild child,int parentPosition , MemberChildViewHolder childViewHolder){
        if(membersList.get(parentPosition).isInGym()){
            membersFragment.runDialogForLoginLogout("Do you want to logout "+ membersList.get(parentPosition).getName()+" "+membersList.get(parentPosition).getSurname() +"?", parentPosition);
        }
        else{
            membersFragment.runDialogForLoginLogout("Do you want to login "+ membersList.get(parentPosition).getName()+" "+membersList.get(parentPosition).getSurname() +"?",parentPosition);
        }
    }

    private void clickOnMembership(View v, MemberChild child,int parentPosition , MemberChildViewHolder childViewHolder){
        membersFragment.runDialogForMembership(membersList.get(parentPosition).getMembership(), membersList.get(parentPosition));
    }
    private void clickOnEdit(View v, MemberChild child,int parentPosition , MemberChildViewHolder childViewHolder){
        membersFragment.openEditMember(membersList.get(parentPosition), false);
    }
}
