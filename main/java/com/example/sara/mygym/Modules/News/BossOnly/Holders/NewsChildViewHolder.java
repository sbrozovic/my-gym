package com.example.sara.mygym.Modules.News.BossOnly.Holders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 6.12.2017..
 */

public class NewsChildViewHolder extends ChildViewHolder {
    public TextView update, delete;
    public NewsChildViewHolder(@NonNull View itemView) {
        super(itemView);
        update = (TextView) itemView.findViewById(R.id.textViewUpdate);
        delete = (TextView) itemView.findViewById(R.id.textViewDelete);
    }
}
