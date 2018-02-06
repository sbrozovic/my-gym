package com.example.sara.mygym.Modules.PriceList.BossOnly.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sara.mygym.Common.Models.PriceList.Period;
import com.example.sara.mygym.Modules.PriceList.BossOnly.EditPriceListActivity;
import com.example.sara.mygym.R;

import java.util.List;

/**
 * Created by Sara on 9.1.2018..
 */

public class PeriodRecyclerViewAdapter extends RecyclerView.Adapter<PeriodRecyclerViewAdapter.MyViewHolder> {

    private List<Period> list;
    private EditPriceListActivity editPriceListActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewName);
        }
    }

    public PeriodRecyclerViewAdapter(List<Period> list, EditPriceListActivity editPriceListActivity){
        this.list = list;
        this.editPriceListActivity = editPriceListActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_period_tariff, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String name = list.get(position).getName();
        holder.name.setText(name);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPriceListActivity.runDialogDeleteEditPeriod(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
