package com.example.sara.mygym.Modules.Messanger.Holders;

import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sara.mygym.Common.Models.Messenger.Message;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 20.12.2017..
 */

public class SentMessageHolder extends RecyclerView.ViewHolder{
    TextView sendMessage, sendTime;


    public SentMessageHolder(View itemView) {
        super(itemView);

        sendMessage = (TextView) itemView.findViewById(R.id.textViewMessageSend);
        sendTime = (TextView) itemView.findViewById(R.id.textViewTimeOfSendMessage);
    }

    public void bind(Message message){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm aa");
        sendMessage.setText(message.getMessage());
        sendTime.setText(sdf.format(message.getCreatedAt()));
    }
}
