package com.example.sara.mygym.Modules.News.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.NotificationModel;
import com.example.sara.mygym.Modules.Messanger.MessengerActivity;
import com.example.sara.mygym.Common.Models.News.News;
import com.example.sara.mygym.Modules.News.Adapters.NewsAdapter;
import com.example.sara.mygym.Modules.News.BossOnly.Item.NewsChild;
import com.example.sara.mygym.Modules.News.BossOnly.Adapters.NewsExpandableAdapter;
import com.example.sara.mygym.R;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsExpandableAdapter newsExpandableAdapter;
    private NewsAdapter newsAdapter;
    private List<News> newsList = new ArrayList<>();
    private List<Integer> listOfExpendParents = new ArrayList<>();
    private Map<Integer, Integer> mapForRemoveDivider = new HashMap<>();
    private boolean isBossOpened = false;
    private boolean isStaffOpened = false;
    private ImageView imageViewMessenger;
    private SwipeRefreshLayout swipeRefreshLayout;
    View view;

    ApiInterface apiInterface;
    Person user;

    @Override
    public void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        apiInterface = ApiClient.getClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewNews);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddNews);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefreshNewsFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setDataAndRecyclerView(false);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        user =(Person) getActivity().getIntent().getSerializableExtra("user");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String openedFragment = (String) bundle.get("News_opened");
            if (openedFragment.equals("boss")) {
                isBossOpened = true;
                isStaffOpened = false;
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        runDialogForAdd();
                    }
                });
            } else if (openedFragment.equals("staff")) {
                isBossOpened = false;
                isStaffOpened = true;
                fab.setVisibility(View.GONE);
            } else {
                isBossOpened = false;
                isStaffOpened = false;
                fab.setVisibility(View.GONE);
            }
        }
        setDataAndRecyclerView(true);

        imageViewMessenger = (ImageView) view.findViewById(R.id.imageViewMessenger);
        imageViewMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MessengerActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setDataAndRecyclerView(final boolean isFirstSetup) {
        NewsChild newsChild = new NewsChild("Delete", "Update");
        final List<NewsChild> list = new ArrayList<>();
        list.add(newsChild);

        Call<List<NotificationModel>> call = apiInterface.getListOfNews();
        call.enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                newsList.clear();
                List<NotificationModel> notificationModelList = response.body();
                for(NotificationModel el: notificationModelList){
                    News news = el.getNews();
                    newsList.add(news);
                }
                selectionSortAlgoritamForNewsList();
                for (News news : newsList) {
                    news.setNewsChildList(list);
                }
                if(isFirstSetup){
                    setRecyclerView();
                }
                else {
                    //this can only boss do
                    listOfExpendParents.clear();
                    mapForRemoveDivider.clear();
                    newsExpandableAdapter = new NewsExpandableAdapter(getContext(), newsList, NewsFragment.this);
                    setExpandCollapseListener(newsExpandableAdapter, newsList);
                    recyclerView.setAdapter(newsExpandableAdapter);
                    newsExpandableAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setRecyclerView() {
        if (isBossOpened) {
            newsExpandableAdapter = new NewsExpandableAdapter(getContext(), newsList, NewsFragment.this);
            setExpandCollapseListener(newsExpandableAdapter, newsList);

        } else {
            newsAdapter = new NewsAdapter(newsList);
        }
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.WHITE)
                .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int position, RecyclerView parent) {
                        if (mapForRemoveDivider.containsValue(position)) {
                            return true;
                        }
                        return false;
                    }
                })
                .marginResId(R.dimen.left_margin, R.dimen.right_margin)
                .build());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isBossOpened) {
            recyclerView.setAdapter(newsExpandableAdapter);
        } else {
            recyclerView.setAdapter(newsAdapter);
        }
    }

    private void algoritamForDealingWithDividersInRecyclerView(List<Integer> listOfExpendParents, Map<Integer, Integer> mapForRemoveDivider) {
        for (Integer i : listOfExpendParents) {
            int count = 0;
            for (Integer integer : listOfExpendParents) {
                if (integer < i) {
                    count++;
                }
            }
            mapForRemoveDivider.put(i, count + i);
        }
    }

    public void runDialogForAdd(){
        final EditText subject = new EditText(getContext());
        final EditText text = new EditText(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Create new post");

        subject.setHint("Post name...");
        layout.addView(subject);

        text.setHint("Put description of post...");
        layout.addView(text);

        builder1.setView(layout);

        builder1.setPositiveButton(
                "Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        String newSubject = subject.getText().toString();
                        String newtext = text.getText().toString();
                        Date newDate = new Date();

                        Call<Boolean> call = apiInterface.postNews(new NotificationModel(new News(-1,  newSubject, newtext,newDate)));
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                setDataAndRecyclerView(false);
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                call.cancel();
                            }
                        });
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void runDialogForUpdate(String textOfMessage, final int parentPosition){
        final EditText subject = new EditText(getContext());
        final EditText text = new EditText(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(textOfMessage);

        subject.setText(newsList.get(parentPosition).getSubject());
        layout.addView(subject);
        text.setText(newsList.get(parentPosition).getNewsText());
        layout.addView(text);
        builder1.setView(layout);

        builder1.setPositiveButton(
                "Save changes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        String newSubject = subject.getText().toString();
                        String newtext = text.getText().toString();
                        Date newDate = new Date();

                        Call<Boolean> call = apiInterface.putNews(new NotificationModel(new News(newsList.get(parentPosition).getId(),  newSubject, newtext,newDate)));
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                setDataAndRecyclerView(false);
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                    call.cancel();
                            }
                        });
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void runDialogForDelete(String textOfMessage, final int perentPosition){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(textOfMessage);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Call<Boolean> call = apiInterface.deleteNews(newsList.get(perentPosition).getId());
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                setDataAndRecyclerView(false);
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                call.cancel();
                            }
                        });

                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    private void setExpandCollapseListener(final NewsExpandableAdapter adapter, final List<News> list){
        adapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onParentExpanded(int parentPosition) {
                LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if(lastVisiblePosition == parentPosition) {
                    recyclerView.scrollToPosition(lastVisiblePosition+1);
                    adapter.notifyDataSetChanged();

                }
                for(int i = 0; i<list.size(); i++){
                    if(i != parentPosition){
                        adapter.collapseParent(i);
                        onParentCollapsed(i);
                    }
                }

                if (!listOfExpendParents.contains(parentPosition)) {
                    listOfExpendParents.add(parentPosition);
                }
                algoritamForDealingWithDividersInRecyclerView(listOfExpendParents, mapForRemoveDivider);
            }

            @Override
            public void onParentCollapsed(int parentPosition) {
                if(listOfExpendParents.contains(parentPosition)) {
                    listOfExpendParents.remove((Integer) parentPosition);
                    mapForRemoveDivider.remove(parentPosition);
                    algoritamForDealingWithDividersInRecyclerView(listOfExpendParents, mapForRemoveDivider);
                }
            }
        });
    }

    private void selectionSortAlgoritamForNewsList(){

        for(int i  = 0; i<newsList.size(); i++){
            News smallestNews = newsList.get(i);
            int smallestIndex = i;
            for(int j=i; j<newsList.size(); j++){
                if(smallestNews.getDate().before(newsList.get(j).getDate())){
                    smallestNews = newsList.get(j);
                    smallestIndex = j;
                }
            }
            Collections.swap(newsList,i, smallestIndex);
        }
    }
}
