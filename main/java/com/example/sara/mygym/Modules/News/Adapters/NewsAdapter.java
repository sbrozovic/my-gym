package com.example.sara.mygym.Modules.News.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sara.mygym.Common.Models.News.News;
import com.example.sara.mygym.Modules.News.Holders.NewsViewHolder;
import com.example.sara.mygym.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Sara on 6.12.2017..
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private List<News> newsList;

    public NewsAdapter(List<News> newsList){
        this.newsList=newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_news_list_row, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        News news = newsList.get(position);
        holder.subject.setText(news.getSubject());
        holder.news.setText(news.getNewsText());
        holder.date.setText(formatter.format(news.getDate()));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
