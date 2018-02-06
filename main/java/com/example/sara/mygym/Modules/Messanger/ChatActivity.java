package com.example.sara.mygym.Modules.Messanger;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sara.mygym.Common.Models.Messenger.Message;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.MessageModel;
import com.example.sara.mygym.Modules.Messanger.Adapters.MessageListAdapter;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Models.Role;
import com.example.sara.mygym.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private List<Message> messageList = new ArrayList<>();

    private MessageListAdapter messageListAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    EditText sendMessage;
    Button buttonSend;

    Person personThatSendsMessage;
    Person personToSendMessage;
    Message lastMessage;
    boolean isAnonymous = false;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");

        apiInterface = ApiClient.getClient();
        sendMessage = (EditText) findViewById(R.id.edittext_chatbox);
        buttonSend = (Button) findViewById(R.id.button_chatbox_send);
        recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshActivityChat);
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

        personThatSendsMessage = (Person) getIntent().getSerializableExtra("user");

        Bundle bundle = getIntent().getExtras();
        if (bundle.get("chosenPerson") != null) {
            personToSendMessage = (Person) bundle.get("chosenPerson");
            isAnonymous = bundle.getBoolean("isAnonymous");
        } else {
            personToSendMessage = (Person) bundle.get("personToComm");
            lastMessage = (Message) bundle.get("lastMessage");
            isAnonymous = lastMessage.isAnonymous();
        }
        initData();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = sendMessage.getText().toString();
        if (!message.equals("")) {
            final Message message1 = new Message(-1, message, personThatSendsMessage, personToSendMessage, personThatSendsMessage, new Date().getTime(), isAnonymous, true);
            Message message2 = new Message(-1, message, personThatSendsMessage, personToSendMessage, personToSendMessage, new Date().getTime(), isAnonymous, true);

            Call<Boolean> call = apiInterface.postMessage(new MessageModel(message1));
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    boolean b = response.body();
                    lastMessage = message1;
                    initData();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    call.cancel();
                }
            });

            final Call<Boolean> call1 = apiInterface.postMessage(new MessageModel(message2));
            call1.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    boolean b = response.body();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    call1.cancel();
                }
            });

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            messageListAdapter = new MessageListAdapter(this, messageList, personThatSendsMessage);
            recyclerView.setAdapter(messageListAdapter);
            messageListAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageList.size() - 1);
        }
        sendMessage.setText("");
    }

    private void initData() {
        messageList.clear();
        Call<List<MessageModel>> call = apiInterface.getMessages();
        call.enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                List<MessageModel> messageModelList = response.body();
                for (MessageModel messageModel : messageModelList) {
                    int idSender = messageModel.getMessage().getSender().getId();
                    int idReceiver = messageModel.getMessage().getReceiver().getId();
                    int idOwner = messageModel.getMessage().getOwner().getId();
                    if ((idSender == personThatSendsMessage.getId() || idReceiver == personThatSendsMessage.getId()) && (idSender == personToSendMessage.getId() || idReceiver == personToSendMessage.getId()) && idOwner == personThatSendsMessage.getId()) {
                        if(lastMessage!=null) {
                            if (lastMessage.isAnonymous() && messageModel.getMessage().isAnonymous()) {
                                messageList.add(messageModel.getMessage());
                            } else if (!lastMessage.isAnonymous() && !messageModel.getMessage().isAnonymous()) {
                                messageList.add(messageModel.getMessage());
                            }
                        }
                    }
                }
                for (Message el : messageList) {
                    if (el.getSender().getId() == personThatSendsMessage.getId()) {
                        el.setDidUserSendTheMessage(true);
                    } else {
                        el.setDidUserSendTheMessage(false);
                    }
                }
                if (!messageList.isEmpty())
                    selectionSortAlgoritamForChat();
                setRecyclerView();
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setRecyclerView() {
        messageListAdapter = new MessageListAdapter(this, messageList, personThatSendsMessage);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageListAdapter);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void selectionSortAlgoritamForChat() {
        for (int i = 0; i < messageList.size(); i++) {
            Message smallestMessage = messageList.get(i);
            int smallestIndex = i;
            for (int j = i; j < messageList.size(); j++) {
                if (smallestMessage.getCreatedAt() > (messageList.get(j).getCreatedAt())) {
                    smallestMessage = messageList.get(j);
                    smallestIndex = j;
                }
            }
            Collections.swap(messageList, i, smallestIndex);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
