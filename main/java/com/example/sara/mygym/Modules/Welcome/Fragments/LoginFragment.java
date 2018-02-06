package com.example.sara.mygym.Modules.Welcome.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.LoginModel;
import com.example.sara.mygym.Common.Network.Wrappers.PersonModel;
import com.example.sara.mygym.Modules.Home.BossStartActivity;
import com.example.sara.mygym.Modules.Welcome.MainActivity;
import com.example.sara.mygym.Modules.Home.MemberStartActivity;
import com.example.sara.mygym.Modules.Home.StaffStartActivity;
import com.example.sara.mygym.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    public TextView textViewPriceListInLogin;
    public TextView textViewAboutUsInLogin;
    public ImageView imageArrowPriceListInLogin;
    public ImageView imageViewArrowAboutUsInLogin;
    public EditText editTextEmail;
    public EditText editTextPassword;

    ApiInterface apiInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_login, container, false);
        Button buttonLogin = (Button) layout.findViewById(R.id.buttonLogin);
        textViewPriceListInLogin = (TextView) layout.findViewById(R.id.textViewSmallPriceList);
        imageArrowPriceListInLogin = (ImageView) layout.findViewById(R.id.imageViewArrowLeft);
        textViewAboutUsInLogin = (TextView) layout.findViewById(R.id.textViewSmallAboutUs);
        imageViewArrowAboutUsInLogin = (ImageView) layout.findViewById(R.id.imageViewArrowRight);
        editTextEmail = (EditText) layout.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) layout.findViewById(R.id.editTextPassword);

        textViewPriceListInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnPriceList(v);
            }
        });
        imageArrowPriceListInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnPriceList(v);
            }
        });

        textViewAboutUsInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnAboutUs(v);
            }
        });
        imageViewArrowAboutUsInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnAboutUs(v);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                Call<Integer> call = apiInterface.login(new LoginModel(email, password));
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        int id = response.body();
                        if(id == -1){
                            Toast.makeText(getContext(), "Email or password isn't correct!", Toast.LENGTH_LONG).show();
                            editTextPassword.setText("");
                        }
                        else {
                            Call<PersonModel> call1 = apiInterface.getPersonById(id);
                            call1.enqueue(new Callback<PersonModel>() {
                                @Override
                                public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                                    PersonModel personModel = response.body();
                                    Person user = personModel.getPerson();
                                    Intent intent = null;
                                    if (user.getRole().getName().equals("boss")) {
                                        intent = new Intent(getActivity(), BossStartActivity.class);
                                    } else if (user.getRole().getName().equals("staff")) {
                                        intent = new Intent(getActivity(), StaffStartActivity.class);
                                    } else {
                                        intent = new Intent(getActivity(), MemberStartActivity.class);
                                    }
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<PersonModel> call, Throwable t) {
                                    call.cancel();
                                    Toast.makeText(getContext(), "Sorry, login is unsuccessful.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(getContext(), "Sorry, login is unsuccessful.", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
        return layout;
    }

    private void clickOnAboutUs(View v) {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).pager.setCurrentItem(2);

        }
    }

    private void clickOnPriceList(View v) {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).pager.setCurrentItem(0);

        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
