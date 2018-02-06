package com.example.sara.mygym.Common.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sara.mygym.Common.Models.AboutUs;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.AboutUsModel;
import com.example.sara.mygym.Modules.Welcome.MainActivity;
import com.example.sara.mygym.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AboutUsFragment extends Fragment {
    TextView textViewLogin;
    ImageView imageViewArrow;
    EditText phone;
    EditText email;
    EditText address;
    EditText workingHours;
    Button saveButton;

    ApiInterface apiInterface;
    AboutUs aboutUs;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = ApiClient.getClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_about_us, container, false);
        textViewLogin = (TextView) layout.findViewById(R.id.textViewSmallLogin2);
        imageViewArrow = (ImageView) layout.findViewById(R.id.imageViewArrowLogin2);
        phone = (EditText) layout.findViewById(R.id.editTextGymPhone);
        email = (EditText) layout.findViewById(R.id.editTextGymEmail);
        address = (EditText) layout.findViewById(R.id.editTextGymAddress);
        workingHours = (EditText) layout.findViewById(R.id.editTextWoringHours);
        saveButton = (Button) layout.findViewById(R.id.buttonSaveAboutUsEdit);

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeToRefreshAboutUsFragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getInfoFromDatabase();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String openedFragment = (String) bundle.get("Info_opened");
            if (openedFragment.equals("boss")) {
                phone.setEnabled(true);
                email.setEnabled(true);
                address.setEnabled(true);
                workingHours.setEnabled(true);
                saveButton.setVisibility(View.VISIBLE);
                saveButtonListener();
            }
            else{
                phone.setEnabled(false);
                email.setEnabled(false);
                address.setEnabled(false);
                workingHours.setEnabled(false);
            }
            textViewLogin.setVisibility(View.GONE);
            imageViewArrow.setVisibility(View.GONE);
        } else {
            phone.setEnabled(false);
            email.setEnabled(false);
            address.setEnabled(false);
            workingHours.setEnabled(false);
            loginListeners();
        }
        TextView textViewContactUS = (TextView) layout.findViewById(R.id.textViewContactUs);
        textViewContactUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        ImageView imageViewPhone = (ImageView) layout.findViewById(R.id.imageViewPhone2);
        imageViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        getInfoFromDatabase();
        return layout;
    }

    public void getInfoFromDatabase(){

        Call<AboutUsModel> call = apiInterface.getAboutUs();
        call.enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(Call<AboutUsModel> call, Response<AboutUsModel> response) {
                AboutUsModel aboutUsModel = response.body();

                aboutUs = aboutUsModel.getAboutUs();

                phone.setText(aboutUs.getPhone());
                email.setText(aboutUs.getMail());
                address.setText(aboutUs.getAddress());
                workingHours.setText(aboutUs.getWorkingTime());
            }

            @Override
            public void onFailure(Call<AboutUsModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void saveButtonListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String savephone = phone.getText().toString();
                String saveEmail = email.getText().toString();
                String saveAddress = address.getText().toString();
                String saveWorkingHours = workingHours.getText().toString();

                AboutUsModel aboutUsModel = new AboutUsModel(new AboutUs(aboutUs.getId(), aboutUs.getName(), saveWorkingHours, savephone, saveEmail, saveAddress));
                Call<Boolean> call = apiInterface.putAboutUs(aboutUsModel);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toast.makeText(getContext(), "Info is saved to database.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });
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

    private void clickOnLogin(View v) {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).pager.setCurrentItem(1);
        }
    }

    private void call() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+phone.getText().toString()));
        startActivity(callIntent);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
