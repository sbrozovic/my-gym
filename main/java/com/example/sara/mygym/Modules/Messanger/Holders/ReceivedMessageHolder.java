package com.example.sara.mygym.Modules.Messanger.Holders;

import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sara.mygym.Common.Models.Messenger.Message;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 20.12.2017..
 */

public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
    TextView receivedMessage, receivedTime, sendersName;

    public ReceivedMessageHolder(View itemView) {
        super(itemView);

        receivedMessage = (TextView) itemView.findViewById(R.id.textViewMessageRcv);
        receivedTime = (TextView) itemView.findViewById(R.id.textViewTimeOfRcvMessage);
        sendersName = (TextView) itemView.findViewById(R.id.textViewSenderName);
    }
    public void bind(Message message, Person user){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm aa");
        receivedMessage.setText(message.getMessage());
        receivedTime.setText(sdf.format(message.getCreatedAt()));
        if(user.getRole().getName().equals("boss")){
           setStandardAnonymousNotAnonymousMessage(message);
        }
        else if(user.getRole().getName().equals("staff")){
            if(message.getSender().getRole().getName().equals("boss") || message.getReceiver().getRole().getName().equals("boss")){
                sendersName.setText(message.getSender().getName() + " " +message.getSender().getSurname());
            }
            else{
                setStandardAnonymousNotAnonymousMessage(message);
            }
        }
        else{
            sendersName.setText(message.getSender().getName() + " " +message.getSender().getSurname());
        }
    }
    private void setStandardAnonymousNotAnonymousMessage(Message message){
        if(message.isAnonymous()){
            sendersName.setText("Anonymous");
        }
        else {
            sendersName.setText(message.getSender().getName() + " " +message.getSender().getSurname());
        }
    }
}
