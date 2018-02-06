package com.example.sara.mygym.Modules.Members.OnlyMember.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.PersonModel;
import com.example.sara.mygym.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.example.sara.mygym.Common.UI.AlertManager.runDialogSettings;
import static com.example.sara.mygym.Common.UI.AlertManager.runDialogSingOut;

public class HomeMemberFragment extends Fragment {
    private TextView name, surname, membershipActiveUntil, membershipWorthUpTo, lastpaid, tariff;
    private ImageView imageViewSettings;
    private ImageView imageViewSingOut;
    private TextView membersInGym;
    private TextView couchNames;

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    Person user;

    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home_member, container, false);

        apiInterface = ApiClient.getClient();

        name = (TextView) layout.findViewById(R.id.textViewNameMember);
        surname = (TextView) layout.findViewById(R.id.textViewSurnameMember);
        membershipActiveUntil = (TextView) layout.findViewById(R.id.textViewMembershipActiveUntil);
        membershipWorthUpTo = (TextView) layout.findViewById(R.id.textViewMembershipIsWorthUpTo);
        lastpaid = (TextView) layout.findViewById(R.id.textViewlastPaid);
        tariff = (TextView) layout.findViewById(R.id.textViewTariffMember);
        membersInGym = (TextView) layout.findViewById(R.id.textViewMembersInGym);
        couchNames = (TextView) layout.findViewById(R.id.textViewCoachNameIMember) ;

        imageViewSettings = (ImageView) layout.findViewById(R.id.imageViewSettingsMember);
        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               runDialogSettings(getContext(), user, apiInterface);
            }
        });

        imageViewSingOut = (ImageView) layout.findViewById(R.id.imageViewSingOutMember);
        imageViewSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDialogSingOut(getContext(), getActivity());
            }
        });

        user = (Person) getActivity().getIntent().getSerializableExtra("user");

        setUser(user);

        setMembersInGym();

        setCouchNames();

        return layout;
    }

    private void setUser(Person user) {
        name.setText(user.getName());
        surname.setText(user.getSurname());
        membershipActiveUntil.setText(formatter.format(user.getMembership().getMembershipActiveUntil()));
        long diff = Math.abs(user.getMembership().getMembershipActiveUntil().getTime() - new Date().getTime());
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if(diffDays<0){
            membershipWorthUpTo.setText("Expired");
        }
        else {
            membershipWorthUpTo.setText(String.valueOf(diffDays));
        }
        lastpaid.setText(String.valueOf(user.getMembership().getPrice()));
        tariff.setText(user.getMembership().getName());
    }

    private void setCouchNames() {
        final List<Person> presentCounchList = new ArrayList<>();
        Call<List<PersonModel>> call = apiInterface.getListOfPersons();
        call.enqueue(new Callback<List<PersonModel>>() {
            @Override
            public void onResponse(Call<List<PersonModel>> call, Response<List<PersonModel>> response) {
                List<PersonModel> personModelList = response.body();
                for(PersonModel el: personModelList){
                    if(el.getPerson().getRole().getName().equals("staff") && el.getPerson().isInGym()){
                        presentCounchList.add(el.getPerson());
                    }
                }
                String names = null;
                if(presentCounchList.size() == 0){
                    couchNames.setText("None");
                }
                else{
                    int i = 1;
                    for(Person person: presentCounchList){
                        if(i==1){
                            names = person.getName();
                        }
                        else {
                            names += person.getName();
                        }
                        if(i<presentCounchList.size()){
                            names += ", ";
                            i++;
                        }
                    }
                    couchNames.setText(names);
                }
            }

            @Override
            public void onFailure(Call<List<PersonModel>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Currently not able to get list of coaches that are present in gym.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setMembersInGym() {
        Call<List<PersonModel>> call = apiInterface.getListOfPersons();
        final int[] counter = {0};
        call.enqueue(new Callback<List<PersonModel>>() {
            @Override
            public void onResponse(Call<List<PersonModel>> call, Response<List<PersonModel>> response) {
                List<PersonModel> personModelList = response.body();
                for(PersonModel el: personModelList){
                    if(el.getPerson().getRole().getName().equals("member")){
                        if(el.getPerson().isInGym()){
                            counter[0]++;
                        }
                    }
                }
                membersInGym.setText(String.valueOf(counter[0]));
            }

            @Override
            public void onFailure(Call<List<PersonModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
