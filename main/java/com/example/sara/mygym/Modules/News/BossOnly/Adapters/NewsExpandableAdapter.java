package com.example.sara.mygym.Modules.News.BossOnly.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.sara.mygym.Modules.News.Fragment.NewsFragment;
import com.example.sara.mygym.Common.Models.News.News;
import com.example.sara.mygym.Modules.News.Holders.NewsViewHolder;
import com.example.sara.mygym.Modules.News.BossOnly.Item.NewsChild;
import com.example.sara.mygym.Modules.News.BossOnly.Holders.NewsChildViewHolder;
import com.example.sara.mygym.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Sara on 6.12.2017..
 */

public class NewsExpandableAdapter extends ExpandableRecyclerAdapter<News, NewsChild,NewsViewHolder,NewsChildViewHolder> {
    LayoutInflater mInflater;
    NewsFragment newsFragment;
    List<News> newsList = new ArrayList<>();


    public NewsExpandableAdapter(Context context, List<News>newsList, NewsFragment newsFragment){
        super(newsList);
        mInflater = LayoutInflater.from(context);
        this.newsFragment = newsFragment;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View newsView = mInflater.inflate(R.layout.recycler_view_news_list_row, parentViewGroup, false);
        return new NewsViewHolder(newsView);
    }

    @NonNull
    @Override
    public NewsChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View newsView = mInflater.inflate(R.layout.recycler_view_child_news, childViewGroup, false);
        return new NewsChildViewHolder(newsView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull NewsViewHolder parentViewHolder, int parentPosition, @NonNull News parent) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        parentViewHolder.date.setText(formatter.format(parent.getDate()));
        parentViewHolder.news.setText(parent.getNewsText());
        parentViewHolder.subject.setText(parent.getSubject());
    }

    @Override
    public void onBindChildViewHolder(@NonNull NewsChildViewHolder childViewHolder, final int parentPosition, int childPosition, @NonNull NewsChild child) {
        childViewHolder.delete.setText(child.getDelete());
        childViewHolder.update.setText(child.getUpdate());
        childViewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsFragment.runDialogForUpdate("Update post: "+ newsList.get(parentPosition).getSubject(), parentPosition);
            }
        });
        childViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               newsFragment.runDialogForDelete("Do you want to delete post: "+ newsList.get(parentPosition).getSubject(), parentPosition);
            }
        });
    }
}
