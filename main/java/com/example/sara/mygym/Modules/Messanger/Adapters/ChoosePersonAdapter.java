package com.example.sara.mygym.Modules.Messanger.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sara on 20.12.2017..
 */

public class ChoosePersonAdapter extends RecyclerView.Adapter<ChoosePersonAdapter.ChoosePersonHolder> {
    LayoutInflater mInflater;
    List<Person> choosePersonList = new ArrayList<>();

    public class ChoosePersonHolder extends RecyclerView.ViewHolder {
        TextView choosePersonName, choosePersonRole;

        public ChoosePersonHolder(View itemView) {
            super(itemView);

            choosePersonName = (TextView) itemView.findViewById(R.id.textViewChoosePersonName);
            choosePersonRole = (TextView) itemView.findViewById(R.id.textViewChoosePersonRole);
        }
    }

    public ChoosePersonAdapter(Context context, List<Person> choosePersonList){
        mInflater = LayoutInflater.from(context);
        this.choosePersonList = choosePersonList;
    }

    @Override
    public ChoosePersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChoosePersonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_choose_person, parent, false));
    }

    @Override
    public void onBindViewHolder(ChoosePersonHolder holder, int position) {
        Person choosePerson = choosePersonList.get(position);
        holder.choosePersonName.setText(choosePerson.getName()+" "+ choosePerson.getSurname());
        holder.choosePersonRole.setText(choosePerson.getRole().getName());
    }

    @Override
    public int getItemCount() {
        return choosePersonList.size();
    }
}
