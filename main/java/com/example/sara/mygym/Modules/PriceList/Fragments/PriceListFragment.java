package com.example.sara.mygym.Modules.PriceList.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sara.mygym.Common.Models.PriceList.Period;
import com.example.sara.mygym.Common.Models.PriceList.Tariff;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.AboutUsModel;
import com.example.sara.mygym.Common.Network.Wrappers.PeriodModel;
import com.example.sara.mygym.Common.Network.Wrappers.TariffModel;
import com.example.sara.mygym.Modules.PriceList.BossOnly.EditPriceListActivity;
import com.example.sara.mygym.Modules.Welcome.MainActivity;
import com.example.sara.mygym.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PriceListFragment extends Fragment {
    TextView textViewLogin;
    ImageView imageViewArrow;
    ImageView imageViewEditAdd;
    TableLayout tableLayout;
    TableRow tr;
    TextView text1;
    TextView text2;
    boolean isBossOpened = false;
    List<Pair> listPrice = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;


    ApiInterface apiInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_price_list, container, false);

        textViewLogin = (TextView) layout.findViewById(R.id.textViewSmallLogin1);
        imageViewArrow = (ImageView) layout.findViewById(R.id.imageViewArrowLogin1);
        imageViewEditAdd = (ImageView) layout.findViewById(R.id.imageViewEditAdd);

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeToRefreshPriceListFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String openedFragment = (String) bundle.get("Price_opened");
            if (openedFragment.equals("boss")) {
                textViewLogin.setVisibility(View.GONE);
                imageViewArrow.setVisibility(View.GONE);
                imageViewEditAdd.setVisibility(View.VISIBLE);
                isBossOpened = true;
                setAddEdit();
            } else {
                textViewLogin.setVisibility(View.GONE);
                imageViewArrow.setVisibility(View.GONE);
            }
        } else {
            loginListeners();
        }
        tableLayout = (TableLayout) layout.findViewById(R.id.table_layout);

        getData();

        TextView textViewContactUS = (TextView) layout.findViewById(R.id.textViewContactUs);
        textViewContactUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        ImageView imageViewPhone = (ImageView) layout.findViewById(R.id.imageViewPhone1);
        imageViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        return layout;
    }

    private void setAddEdit() {
        imageViewEditAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Do you want to add or edit?");

                builder1.setPositiveButton(
                        "Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setAdd();
                                dialog.cancel();
                            }
                        });
                builder1.setNeutralButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setEdit();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    private void setEdit() {
        Intent intent = new Intent(getActivity(), EditPriceListActivity.class);
        startActivity(intent);
    }

    private void getData() {
        listPrice.clear();
        final List<Period> periodList = new ArrayList<>();
        final Call<List<PeriodModel>> call1 = apiInterface.getListOfPeriods();
        call1.enqueue(new Callback<List<PeriodModel>>() {
            @Override
            public void onResponse(Call<List<PeriodModel>> call, Response<List<PeriodModel>> response) {
                List<PeriodModel> periodModelList = response.body();
                for (PeriodModel el : periodModelList) {
                    periodList.add(el.getPeriod());
                }
                callTariff(periodList);
            }

            @Override
            public void onFailure(Call<List<PeriodModel>> call, Throwable t) {
                call1.cancel();
            }
        });
    }

    private void callTariff(final List<Period> periodList) {
        final List<Tariff> tariffList = new ArrayList<>();
        final Call<List<TariffModel>> call2 = apiInterface.getListOfTariffs();
        call2.enqueue(new Callback<List<TariffModel>>() {
            @Override
            public void onResponse(Call<List<TariffModel>> call, Response<List<TariffModel>> response) {
                List<TariffModel> tariffModelList = response.body();
                for (TariffModel el : tariffModelList) {
                    tariffList.add(el.getTariff());
                }
                for (Period period : periodList) {
                    for (Tariff tariff : tariffList) {
                        if (tariff.getDiscount() == 0) {
                            listPrice.add(new Pair(period.getName() + " " + tariff.getName(), period.getPrice()));
                        } else {
                            double discount = (double) tariff.getDiscount()/100.0;
                            listPrice.add(new Pair(period.getName() + " " + tariff.getName(), (int)(period.getPrice() * (1-discount))));
                        }
                    }
                }
                createNewTable();
            }

            @Override
            public void onFailure(Call<List<TariffModel>> call, Throwable t) {
                call2.cancel();
            }
        });

    }

    private void setAdd() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("What do you want to add?");

        builder1.setPositiveButton(
                "Add tariff",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        runDialogAddTariff();
                        dialog.cancel();
                    }
                });
        builder1.setNeutralButton(
                "Add period",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        runDialogAddPeriod();
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    private void runDialogAddTariff() {
        final EditText newName = new EditText(getContext());
        final EditText newPercent = new EditText(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Create new tariff");

        newName.setHint("Name of tariff...");
        layout.addView(newName);

        newPercent.setHint("Discount percent(example 20, 30)...");
        layout.addView(newPercent);

        builder1.setView(layout);

        builder1.setPositiveButton(
                "Save changes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            TariffModel tariffModel = new TariffModel(new Tariff(-1, newName.getText().toString(), Integer.parseInt(newPercent.getText().toString())));
                            Call<Boolean> call = apiInterface.postTariff(tariffModel);
                            call.enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    tableLayout.removeAllViewsInLayout();
                                    listPrice.clear();
                                    getData();
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    call.cancel();
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "You have to enter only numbers for discount.", Toast.LENGTH_LONG).show();
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

    private void runDialogAddPeriod() {
        final EditText newText = new EditText(getContext());
        final EditText newPrice = new EditText(getContext());
        final EditText newDuration = new EditText(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Create new period");

        newText.setHint("Name of price...");
        layout.addView(newText);

        newPrice.setHint("Price...");
        layout.addView(newPrice);

        newDuration.setHint("Duration(How many days?)...");
        layout.addView(newDuration);

        builder1.setView(layout);

        builder1.setPositiveButton(
                "Save changes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            PeriodModel periodModel = new PeriodModel(new Period(-1, newText.getText().toString(), Integer.parseInt(newPrice.getText().toString()), Integer.parseInt(newDuration.getText().toString())));
                            Call<Boolean> call = apiInterface.postPeriod(periodModel);
                            call.enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    tableLayout.removeAllViewsInLayout();
                                    listPrice.clear();
                                    getData();
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    call.cancel();
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "You have to enter only numbers for price and duration.", Toast.LENGTH_LONG).show();
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

    private void createNewTable() {
        tableLayout.removeAllViewsInLayout();
        for (Pair p : listPrice) {
            tr = new TableRow(getContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            text1 = new TextView(getActivity());
            text2 = new TextView(getActivity());
            text1.setText(p.first.toString());
            text1.setTextColor(Color.WHITE);
            text1.setTextSize(20);
            text1.setPadding(100, 20, 20, 50);
            text1.setTypeface(Typeface.create("casual", Typeface.NORMAL));
            text2.setText(p.second.toString());
            text2.setTextColor(Color.WHITE);
            text2.setTextSize(20);
            text2.setPadding(20, 20, 20, 50);
            text2.setTypeface(Typeface.create("casual", Typeface.NORMAL));
            tr.addView(text1);
            tr.addView(text2);

            tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private void loginListeners() {
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnLogin(v);
            }
        });
        imageViewArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnLogin(v);
            }
        });
    }

    private void call() {
        Call<AboutUsModel> call = apiInterface.getAboutUs();
        call.enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(Call<AboutUsModel> call, Response<AboutUsModel> response) {
                AboutUsModel aboutUsModel = response.body();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+aboutUsModel.getAboutUs().getPhone()));
                startActivity(callIntent);
            }
            @Override
            public void onFailure(Call<AboutUsModel> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Unable to call.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clickOnLogin(View v) {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).pager.setCurrentItem(1);
        }
    }

    public interface OnFragmentInteractionListener {
        String onFragmentInteraction(String string);
    }
}
