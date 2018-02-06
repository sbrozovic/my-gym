package com.example.sara.mygym.Modules.Messanger;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.PersonModel;
import com.example.sara.mygym.Modules.Messanger.Adapters.ChoosePersonAdapter;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Models.Role;
import com.example.sara.mygym.Common.UI.RecyclerItemClickListener;
import com.example.sara.mygym.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePersonToChatWithActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChoosePersonAdapter choosePersonAdapter;

    private List<Person> choosePersonList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    Person user;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_person_to_chat_with);

        apiInterface = ApiClient.getClient();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshActivityChoosePersonToChatWith);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        user =(Person) getIntent().getSerializableExtra("user");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_choose_a_person);

        initData();

    }

    private void setRecyclerView(){
        choosePersonAdapter = new ChoosePersonAdapter(this, choosePersonList);
        LinearLayoutManager linearMenager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearMenager);
        recyclerView.setAdapter(choosePersonAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getBaseContext())
                .color(Color.WHITE)
                .marginResId(R.dimen.left_margin, R.dimen.right_margin)
                .build());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(user.getRole().getName().equals("boss")){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("chosenPerson", choosePersonList.get(position));
                    bundle.putSerializable("user", user);
                    Intent intent = new Intent(ChoosePersonToChatWithActivity.this, ChatActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if(user.getRole().getName().equals("staff")){
                    if(choosePersonList.get(position).getRole().getName().equals("boss")){
                        runDialogIsAnonymous(choosePersonList.get(position));
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chosenPerson", choosePersonList.get(position));
                        bundle.putSerializable("user", user);
                        Intent intent = new Intent(ChoosePersonToChatWithActivity.this, ChatActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    runDialogIsAnonymous(choosePersonList.get(position));
                }
            }
        }));
    }

    private void runDialogIsAnonymous(final Person person) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ChoosePersonToChatWithActivity.this);
        builder1.setMessage("Which type of message do you want to send?");

        builder1.setPositiveButton(
                "Anonymous",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chosenPerson", person);
                        bundle.putSerializable("user", user);
                        bundle.putBoolean("isAnonymous", true);
                        Intent intent = new Intent(ChoosePersonToChatWithActivity.this, ChatActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Regular",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chosenPerson", person);
                        bundle.putSerializable("user", user);
                        bundle.putBoolean("isAnonymous", false);
                        Intent intent = new Intent(ChoosePersonToChatWithActivity.this, ChatActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });
        builder1.show();
    }

    private void initData() {
        choosePersonList.clear();
        Call<List<PersonModel>> call =  apiInterface.getListOfPersons();
        call.enqueue(new Callback<List<PersonModel>>() {
            @Override
            public void onResponse(Call<List<PersonModel>> call, Response<List<PersonModel>> response) {
                List<PersonModel> personModelList = response.body();
                for(PersonModel personModel: personModelList){
                    if(!personModel.getPerson().equals(user))
                    choosePersonList.add(personModel.getPerson());
                }
                setRecyclerView();
            }

            @Override
            public void onFailure(Call<List<PersonModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
