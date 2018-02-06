package com.example.sara.mygym.Modules.News.Holders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 6.12.2017..
 */

public class NewsViewHolder extends ParentViewHolder {
    public TextView date, subject, news;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);

        date = (TextView) itemView.findViewById(R.id.textViewDateActually);
        subject=(TextView) itemView.findViewById(R.id.textViewSubjectActually);
        news = (TextView) itemView.findViewById(R.id.textViewNews);
    }
}
