package com.example.sara.mygym.Modules.Messanger.Adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Modules.Messanger.MessengerActivity;
import com.example.sara.mygym.Modules.Messanger.Item.Chat;
import com.example.sara.mygym.Modules.Messanger.Item.ChatChild;
import com.example.sara.mygym.Modules.Messanger.Holders.ChatChildViewHolder;
import com.example.sara.mygym.R;

import java.util.Date;
import java.util.List;

/**
 * Created by Sara on 5.12.2017..
 */

public class MessengerAdapter extends ExpandableRecyclerAdapter<Chat, ChatChild, MessengerAdapter.MessageViewHolder, ChatChildViewHolder> {
    LayoutInflater mInflater;
    MessengerActivity messengerActivity;
    List<Chat> chatList;
    Person user;

    public class MessageViewHolder extends ParentViewHolder{
        public TextView textViewSendersName;
        public TextView textViewpartOfMessage;
        public ImageView imageViewSender;
        public TextView textViewDateOfLastmessage;

        public MessageViewHolder(View itemView) {
            super(itemView);

            textViewSendersName = (TextView) itemView.findViewById(R.id.textViewSendersName);
            textViewpartOfMessage = (TextView) itemView.findViewById(R.id.textViewPartOfMessage);
            imageViewSender = (ImageView) itemView.findViewById(R.id.imageViewSender);
            textViewDateOfLastmessage = (TextView) itemView.findViewById(R.id.textViewDateOflastMessageInChat);
        }
    }

    public MessengerAdapter(Context context, List<Chat> messagesList, MessengerActivity messengerActivity, Person user){
        super(messagesList);
        mInflater = LayoutInflater.from(context);
        this.messengerActivity = messengerActivity;
        this.chatList = messagesList;
        this.user = user;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_message_list_row,parentViewGroup,false);
        return new MessageViewHolder(view);
    }

    @NonNull
    @Override
    public ChatChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_child_message, childViewGroup, false);
        return new ChatChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull MessageViewHolder parentViewHolder, int parentPosition, @NonNull Chat parent) {
        parentViewHolder.textViewpartOfMessage.setText(parent.getLastMessage().getMessage());
        if(user.getRole().getName().equals("boss")){
            setAnonymousNoAnonymousRegularChat(parent,parentViewHolder);
        }
        else if(user.getRole().getName().equals("staff")){
            if(parent.getLastMessage().getSender().getRole().getName().equals("boss") || parent.getLastMessage().getReceiver().getRole().getName().equals("boss")){
                setAnonymousNoAnonymousChatForUserWithLessRole(parent, parentViewHolder);
            }
            else{
                setAnonymousNoAnonymousRegularChat(parent, parentViewHolder);
            }
        }
        else{
           setAnonymousNoAnonymousChatForUserWithLessRole(parent,parentViewHolder);
        }
        long diff = ((new Date()).getTime()-parent.getLastMessage().getCreatedAt());
        long days = diff/(24*60*60*1000);
        long hours = diff/(60*60*1000);
        if(days>365){
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            parentViewHolder.textViewDateOfLastmessage.setText(sdf.format(parent.getLastMessage().getCreatedAt()));
        }
        else if(days>1){
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
            parentViewHolder.textViewDateOfLastmessage.setText(sdf.format(parent.getLastMessage().getCreatedAt()));
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            parentViewHolder.textViewDateOfLastmessage.setText(sdf.format(parent.getLastMessage().getCreatedAt()));
        }
    }

    @Override
    public void onBindChildViewHolder(@NonNull ChatChildViewHolder childViewHolder, final int parentPosition, int childPosition, @NonNull ChatChild child) {
        childViewHolder.discard.setText(child.getDiscard());
        childViewHolder.answer.setText(child.getAnswer());
        childViewHolder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               messengerActivity.answer(chatList.get(parentPosition));

            }
        });
        childViewHolder.discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messengerActivity.discard(chatList.get(parentPosition));

            }
        });
    }
    private void setAnonymousNoAnonymousChatForUserWithLessRole(Chat parent,MessageViewHolder parentViewHolder){
        if(parent.isAnonymous()) {
            parentViewHolder.textViewSendersName.setText(parent.getPersonThatYouCommunicateWith().getName() + " " + parent.getPersonThatYouCommunicateWith().getSurname() +  " -anonymous chat");
            parentViewHolder.imageViewSender.setImageResource(R.drawable.icons8_account_80);
        }
        else{
            parentViewHolder.textViewSendersName.setText(parent.getPersonThatYouCommunicateWith().getName() + " " + parent.getPersonThatYouCommunicateWith().getSurname());
            parentViewHolder.imageViewSender.setImageResource(R.drawable.icons8_account_80);
        }
    }
    private void setAnonymousNoAnonymousRegularChat(Chat parent,MessageViewHolder parentViewHolder){
        if(parent.isAnonymous()){
            parentViewHolder.textViewSendersName.setText("Anonymous");
            parentViewHolder.imageViewSender.setImageResource(R.drawable.icons8_anonymous_mask_80);
        }
        else{
            parentViewHolder.textViewSendersName.setText(parent.getPersonThatYouCommunicateWith().getName()+" "+parent.getPersonThatYouCommunicateWith().getSurname());
            parentViewHolder.imageViewSender.setImageResource(R.drawable.icons8_account_80);
        }
    }

}
