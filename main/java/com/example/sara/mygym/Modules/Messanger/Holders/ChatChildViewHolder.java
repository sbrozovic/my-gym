package com.example.sara.mygym.Modules.Messanger.Holders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.sara.mygym.R;

/**
 * Created by Sara on 5.12.2017..
 */

public class ChatChildViewHolder extends ChildViewHolder {
    public TextView answer, discard;

    public ChatChildViewHolder(@NonNull View itemView) {
        super(itemView);
        answer = (TextView) itemView.findViewById(R.id.textViewAnswer);
        discard =(TextView) itemView.findViewById(R.id.textViewDiscard);
    }
}
