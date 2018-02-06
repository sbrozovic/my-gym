package com.example.sara.mygym.Modules.PriceList.BossOnly;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.PeriodModel;
import com.example.sara.mygym.Common.Network.Wrappers.TariffModel;
import com.example.sara.mygym.Common.UI.DividerItemDecoration;
import com.example.sara.mygym.Common.Models.PriceList.Period;
import com.example.sara.mygym.Common.Models.PriceList.Tariff;
import com.example.sara.mygym.Modules.PriceList.BossOnly.Adapters.PeriodRecyclerViewAdapter;
import com.example.sara.mygym.Modules.PriceList.BossOnly.Adapters.TariffRecyclerViewAdapter;
import com.example.sara.mygym.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPriceListActivity extends AppCompatActivity {

    RecyclerView recyclerViewPeriod;
    RecyclerView recyclerViewTariff;
    PeriodRecyclerViewAdapter periodRecyclerViewAdapter;
    TariffRecyclerViewAdapter tariffRecyclerViewAdapter;

    List<Period> periodList = new ArrayList<>();
    List<Tariff> tariffList  = new ArrayList<>();

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_price_list);

        apiInterface = ApiClient.getClient();

        recyclerViewPeriod = (RecyclerView) findViewById(R.id.recyclerViewPeriod);
        recyclerViewTariff = (RecyclerView) findViewById(R.id.recyclerViewTariff);

        setPeriodData(true);
        setTariffData(true);
    }

    private void setPeriodData(final boolean isFirstSetup){
        periodList.clear();
        final Call<List<PeriodModel>> call1 = apiInterface.getListOfPeriods();
        call1.enqueue(new Callback<List<PeriodModel>>() {
            @Override
            public void onResponse(Call<List<PeriodModel>> call, Response<List<PeriodModel>> response) {
                List<PeriodModel> periodModelList = response.body();
                for(PeriodModel periodModel: periodModelList){
                    periodList.add(periodModel.getPeriod());
                }
                if(isFirstSetup){
                    setRecyclerViewPeriod();
                }
                else{
                    periodRecyclerViewAdapter = new PeriodRecyclerViewAdapter(periodList, EditPriceListActivity.this);
                    recyclerViewPeriod.setAdapter(periodRecyclerViewAdapter);
                    periodRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PeriodModel>> call, Throwable t) {
                call1.cancel();
            }
        });
    }


    private void setTariffData(final boolean isFirstSetup) {
        tariffList.clear();
        final Call<List<TariffModel>> call2 = apiInterface.getListOfTariffs();
        call2.enqueue(new Callback<List<TariffModel>>() {
            @Override
            public void onResponse(Call<List<TariffModel>> call, Response<List<TariffModel>> response) {
                List<TariffModel> tariffModelList = response.body();
                for(TariffModel tariffModel: tariffModelList){
                    tariffList.add(tariffModel.getTariff());
                }
                if(isFirstSetup){
                    setRecyclerViewTariff();
                }
                else{
                    tariffRecyclerViewAdapter = new TariffRecyclerViewAdapter(tariffList, EditPriceListActivity.this);
                    recyclerViewTariff.setAdapter(tariffRecyclerViewAdapter);
                    tariffRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<TariffModel>> call, Throwable t) {
                call2.cancel();
            }
        });
    }

    private void setRecyclerViewPeriod(){
        periodRecyclerViewAdapter = new PeriodRecyclerViewAdapter(periodList, this);
        RecyclerView.LayoutManager layoutManagerP = new LinearLayoutManager(getApplicationContext());
        recyclerViewPeriod.setLayoutManager(layoutManagerP);
        recyclerViewPeriod.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPeriod.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewPeriod.setAdapter(periodRecyclerViewAdapter);

    }

    private void setRecyclerViewTariff(){
        tariffRecyclerViewAdapter = new TariffRecyclerViewAdapter(tariffList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTariff.setLayoutManager(layoutManager);
        recyclerViewTariff.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTariff.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewTariff.setAdapter(tariffRecyclerViewAdapter);
    }

    public void runDialogDeleteEditPeriod(final Period period) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditPriceListActivity.this);
        builder1.setMessage("Do you want to delete or edit?");

        builder1.setPositiveButton(
                "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        Call<Boolean> call = apiInterface.deletePeriod(period.getId());
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                setPeriodData(false);
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                call.cancel();
                            }
                        });

                        dialog.cancel();
                    }
                });
        builder1.setNeutralButton(
                "Edit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        runDialogEditForPeriod(period);
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void runDialogEditForPeriod(final Period period) {
        final EditText newName = new EditText(EditPriceListActivity.this);
        final EditText newPrice = new EditText(EditPriceListActivity.this);
        final EditText newDuration = new EditText(EditPriceListActivity.this);
        LinearLayout layout = new LinearLayout(EditPriceListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditPriceListActivity.this);
        builder1.setMessage("Edit period: ");

        newName.setText(period.getName());
        layout.addView(newName);

        newPrice.setText(String.valueOf(period.getPrice()));
        layout.addView(newPrice);

        newDuration.setText(String.valueOf(period.getDuration()));
        layout.addView(newDuration);

        builder1.setView(layout);

        builder1.setPositiveButton(
                "Save changes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String name = newName.getText().toString();
                            int price = Integer.parseInt(newPrice.getText().toString());
                            int duration = Integer.parseInt(newDuration.getText().toString());
                            Call<Boolean> call = apiInterface.putPeriod(new PeriodModel(new Period(period.getId(),name, price, duration)));
                            call.enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    setPeriodData(false);
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    call.cancel();
                                }
                            });
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "You have to enter only numbers for price and duration.", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
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

    public void runDialogDeleteEditTariff(final Tariff tariff) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditPriceListActivity.this);
        builder1.setMessage("Do you want to delete or edit?");

        builder1.setPositiveButton(
                "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Call<Boolean> call = apiInterface.deleteTariff(tariff.getId());
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                setTariffData(false);
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                call.cancel();
                            }
                        });
                        dialog.cancel();
                    }
                });
        builder1.setNeutralButton(
                "Edit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        runDialogEditForTariff(tariff);
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void runDialogEditForTariff(final Tariff tariff) {
        final EditText newName = new EditText(EditPriceListActivity.this);
        final EditText newDiscount = new EditText(EditPriceListActivity.this);
        LinearLayout layout = new LinearLayout(EditPriceListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditPriceListActivity.this);
        builder1.setMessage("Edit period: ");

        newName.setText(tariff.getName());
        layout.addView(newName);

        newDiscount.setText(String.valueOf(tariff.getDiscount()));
        layout.addView(newDiscount);

        builder1.setView(layout);

        builder1.setPositiveButton(
                "Save changes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                        String name = newName.getText().toString();
                        int discount = Integer.parseInt(newDiscount.getText().toString());
                        Call<Boolean> call = apiInterface.putTariff(new TariffModel(new Tariff(tariff.getId(), name, discount)));
                            call.enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    setTariffData(false);
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    call.cancel();
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "You have to enter only numbers for discount.", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
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
}
