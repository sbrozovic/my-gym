package com.example.sara.mygym.Modules.Messanger;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.MessageModel;
import com.example.sara.mygym.Modules.Messanger.Item.Chat;
import com.example.sara.mygym.Modules.Messanger.Item.ChatChild;
import com.example.sara.mygym.Common.Models.Messenger.Message;
import com.example.sara.mygym.Modules.Messanger.Adapters.MessengerAdapter;
import com.example.sara.mygym.Common.Models.Person;
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

public class MessengerActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMessages;
    private MessengerAdapter messengerAdapter;
    private List<Chat> chatList = new ArrayList<>();
    private FloatingActionButton fabAddMessage;
    private List<Integer> listOfExpendParents = new ArrayList<>();
    private Map<Integer, Integer> mapForRemoveDivider = new HashMap<>();
    private List<Message> messageList = new ArrayList<>();
    private List<Message> anonymousMessageList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    List<Person> listOfPersonThatUserCommunicates = new ArrayList<>();
    List<Person> listOfAnonymousPeopleThatUserCommunicates = new ArrayList<>();

    Person user;
    ApiInterface apiInterface;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        user = (Person) getIntent().getSerializableExtra("user");
        apiInterface = ApiClient.getClient();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshActivityMessenger);
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

        recyclerViewMessages = (RecyclerView) findViewById(R.id.recyclerViewMessages);
        fabAddMessage = (FloatingActionButton) findViewById(R.id.fabNewMessage);

        fabAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessengerActivity.this, ChoosePersonToChatWithActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        initData();
    }

    private void initData() {
        messageList.clear();
        chatList.clear();
        listOfExpendParents.clear();
        mapForRemoveDivider.clear();
        Call<List<MessageModel>> call = apiInterface.getMessages();
        call.enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                List<MessageModel> messageModelList = response.body();
                for (MessageModel messageModel : messageModelList) {
                    if (messageModel.getMessage().getSender().getId() == user.getId() || messageModel.getMessage().getReceiver().getId() == user.getId()) {
                        if (messageModel.getMessage().isAnonymous()) {
                            anonymousMessageList.add(messageModel.getMessage());
                        } else {
                            messageList.add(messageModel.getMessage());
                        }
                    }
                }
                for (Message el : messageList) {
                    if (el.getSender().getId() == user.getId()) {
                        el.setDidUserSendTheMessage(true);
                    } else {
                        el.setDidUserSendTheMessage(false);
                    }
                }
                setMessanger();
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setMessanger() {
        setListsOFPeopleThatUserCommunicates();

        findLastMessages();

        List<ChatChild> chatChildList = new ArrayList<>();
        chatChildList.add(new ChatChild("Answer", "Discard"));
        for (Chat chat : chatList) {
            chat.setChildObjectList(chatChildList);
        }
        selectionSortAlgoritamForChatList();
        setRecyclerView();
    }

    private void setListsOFPeopleThatUserCommunicates() {
        for (Message el : messageList) {
            if (el.getReceiver().getId() == user.getId()) {
                if (!listOfPersonThatUserCommunicates.contains(el.getSender())) {
                    listOfPersonThatUserCommunicates.add(el.getSender());
                }
            } else {
                if (!listOfPersonThatUserCommunicates.contains(el.getReceiver())) {
                    listOfPersonThatUserCommunicates.add(el.getReceiver());
                }

            }
        }
        for (Message el : anonymousMessageList) {
            if (el.getReceiver().getId() == user.getId()) {
                if (!listOfAnonymousPeopleThatUserCommunicates.contains(el.getSender())) {
                    listOfAnonymousPeopleThatUserCommunicates.add(el.getSender());
                }
            } else {
                if (!listOfAnonymousPeopleThatUserCommunicates.contains(el.getReceiver())) {
                    listOfAnonymousPeopleThatUserCommunicates.add(el.getReceiver());
                }
            }
        }
    }

    private void findLastMessages() {
        for (Person el : listOfPersonThatUserCommunicates) {
            Message lastMessage = null;
            for (Message message : messageList) {
                if ((el.equals(message.getReceiver()) || el.equals(message.getSender())) && message.getOwner().equals(user)) {
                    if (lastMessage == null && !message.isAnonymous()) {
                        lastMessage = message;
                    } else if (new Date(lastMessage.getCreatedAt()).before(new Date(message.getCreatedAt())) && !message.isAnonymous()) {
                        lastMessage = message;
                    }
                }
            }
            if (lastMessage != null) {
                chatList.add(new Chat(el, false, lastMessage));
            }
        }
        for (Person el : listOfAnonymousPeopleThatUserCommunicates) {
            Message lastAnonymousMessage = null;
            for (Message message : anonymousMessageList) {
                if ((el.equals(message.getReceiver()) || el.equals(message.getSender())) && message.getOwner().equals(user)) {
                    if (lastAnonymousMessage == null) {
                        lastAnonymousMessage = message;
                    } else if (new Date(lastAnonymousMessage.getCreatedAt()).before(new Date(message.getCreatedAt()))) {
                        lastAnonymousMessage = message;
                    }
                }
            }
            if (lastAnonymousMessage != null)
                chatList.add(new Chat(el, true, lastAnonymousMessage));
        }
    }

    private void setRecyclerView() {
        messengerAdapter = new MessengerAdapter(getBaseContext(), chatList, this, user);
        setExpandCollapseListener(messengerAdapter, chatList);
        recyclerViewMessages.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getBaseContext())
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
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMessages.setAdapter(messengerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void answer(Chat chat) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("lastMessage", chat.getLastMessage());
        bundle.putSerializable("user", user);
        bundle.putSerializable("personToComm", chat.getPersonThatYouCommunicateWith());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void discard(Chat chat) {
        String isAnonym;
        if (chat.isAnonymous()) {
            isAnonym = "true";
        } else {
            isAnonym = "false";
        }
        Call<Void> call = apiInterface.deleteMessage(user.getId(), chat.getPersonThatYouCommunicateWith().getId(), isAnonym);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                initData();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void setExpandCollapseListener(final MessengerAdapter messengerAdapter, final List<Chat> list) {
        messengerAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onParentExpanded(final int parentPosition) {
                Chat expandedMember = list.get(parentPosition);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerViewMessages.getLayoutManager());
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == parentPosition) {
                    recyclerViewMessages.smoothScrollToPosition(lastVisiblePosition + 1);
                }
                for (int i = 0; i < list.size(); i++) {
                    if (i != parentPosition) {
                        messengerAdapter.collapseParent(i);
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
                Chat collapsedRecipe = list.get(parentPosition);
                if (listOfExpendParents.contains(parentPosition)) {
                    listOfExpendParents.remove((Integer) parentPosition);
                    mapForRemoveDivider.remove(parentPosition);
                    algoritamForDealingWithDividersInRecyclerView(listOfExpendParents, mapForRemoveDivider);
                }
            }
        });
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

    private void selectionSortAlgoritamForChatList() {
        for (int i = 0; i < chatList.size(); i++) {
            Chat smallestChat = chatList.get(i);
            int smallestIndex = i;
            for (int j = i; j < chatList.size(); j++) {
                if (smallestChat.getLastMessage().getCreatedAt() < chatList.get(j).getLastMessage().getCreatedAt()) {
                    smallestChat = chatList.get(j);
                    smallestIndex = j;
                }
            }
            Collections.swap(chatList, i, smallestIndex);
        }
    }
}
