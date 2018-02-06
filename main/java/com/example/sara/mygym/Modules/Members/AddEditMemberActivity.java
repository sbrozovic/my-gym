package com.example.sara.mygym.Modules.Members;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sara.mygym.Common.Models.Account;
import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Models.PriceList.Period;
import com.example.sara.mygym.Common.Models.PriceList.Tariff;
import com.example.sara.mygym.Common.Models.Role;
import com.example.sara.mygym.Common.Models.Members.Membership;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.MembershipModel;
import com.example.sara.mygym.Common.Network.Wrappers.PeriodModel;
import com.example.sara.mygym.Common.Network.Wrappers.PersonModel;
import com.example.sara.mygym.Common.Network.Wrappers.TariffModel;
import com.example.sara.mygym.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditMemberActivity extends AppCompatActivity {
    EditText name;
    EditText surname;
    EditText birth_date;
    EditText phone;
    EditText email;
    EditText address;
    Spinner gender;
    Spinner periodSpinner;
    Spinner tariffSpinner;

    String nameS;
    String surnameS;
    String birth_dateS;
    String phoneS;
    String emailS;
    String addressS;
    String genderS;
    String roleS;
    int periodS;
    int tariffS;
    Period period;
    Tariff tariff;
    Calendar calendar;
    int price;

    List<String> genderList = new ArrayList<>();
    List<Period> periodList = new ArrayList<>();
    List<Tariff> tariffList = new ArrayList<>();

    Button save;
    Button cancel;
    ImageView imageViewDelete;
    TextView textViewResetPass;

    ApiInterface apiInterface;
    Person member;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_member);

        apiInterface = ApiClient.getClient();

        save = (Button) findViewById(R.id.buttonSaveMembers);
        cancel = (Button) findViewById(R.id.buttonCancelMembers);
        imageViewDelete = (ImageView) findViewById(R.id.imageViewDelete);
        textViewResetPass = (TextView) findViewById(R.id.textViewResetPass);

        name = (EditText) findViewById(R.id.editTextNameMembers);
        surname = (EditText) findViewById(R.id.editTextSurnameMembers);
        birth_date = (EditText) findViewById(R.id.editTextBirthDayMembers);
        phone = (EditText) findViewById(R.id.editTextPhoneMembers);
        email = (EditText) findViewById(R.id.editTextEmailMembers);
        address = (EditText) findViewById(R.id.editTextAddressMembers);

        gender = (Spinner) findViewById(R.id.spinnerGenderMembers);
        periodSpinner = (Spinner) findViewById(R.id.spinnerPeriod);
        tariffSpinner = (Spinner) findViewById(R.id.spinnerTariff);

        TextView textMembership = (TextView) findViewById(R.id.textViewMemb);
        TextView textViewGender = (TextView) findViewById(R.id.textViewGenderMembers);
        TextView textPeriod = (TextView) findViewById(R.id.textViewPeriod);
        TextView textTariff = (TextView) findViewById(R.id.textViewTariffMembers);
        TextView textEmail = (TextView) findViewById(R.id.textViewEmailMembers);


        String bundle = getIntent().getExtras().getString("Add_edit_opened_member");
        if (bundle.equals("add")) {
            genderList.add("Male");
            genderList.add("Female");
            ArrayAdapter<String> dataAdapterGender = new ArrayAdapter<String>(this,
                    R.layout.spinner_item, genderList);
            gender.setAdapter(dataAdapterGender);

            getPeriodList();
            getTariffList();

            setAdd();
        } else {
            boolean isMembershipExpired = getIntent().getExtras().getBoolean("Membership_expired");

            if(isMembershipExpired){
                getPeriodList();
                getTariffList();
            }
            else{
                periodSpinner.setVisibility(View.GONE);
                tariffSpinner.setVisibility(View.GONE);
                textMembership.setVisibility(View.GONE);
                textPeriod.setVisibility(View.GONE);
                textTariff.setVisibility(View.GONE);
            }
            email.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);
            textEmail.setVisibility(View.GONE);
            textViewGender.setVisibility(View.GONE);
            member = (Person) getIntent().getSerializableExtra("Member_element");
            Membership membership = (Membership) getIntent().getSerializableExtra("Membership_element");
            setEdit(member, membership, isMembershipExpired);
        }
    }

    private void setAdd() {
        setListenerForButtonSaveAndCancel(true, false);
    }


    public void setEdit(Person member, Membership membership, boolean isMembershipExpired) {
        imageViewDelete.setVisibility(View.VISIBLE);
        textViewResetPass.setVisibility(View.VISIBLE);

        setDeleteListener(member.getName() + " " + member.getSurname());
        setResetListener(member);

        name.setText(member.getName(), TextView.BufferType.EDITABLE);
        surname.setText(member.getSurname(), TextView.BufferType.EDITABLE);
        birth_date.setText(formatter.format(member.getBirthDate()), TextView.BufferType.EDITABLE);
        phone.setText(member.getPhone(), TextView.BufferType.EDITABLE);
        address.setText(member.getAddress(), TextView.BufferType.EDITABLE);

        setListenerForButtonSaveAndCancel(false, isMembershipExpired);
    }

    private void setResetListener(final Person member) {
        textViewResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call = apiInterface.resetPassword(String.valueOf(member.getId()));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });
    }

    private void setDeleteListener(final String name) {
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialogDelete(name);
            }
        });
    }

    private void callDialogDelete(String name) {
        new AlertDialog.Builder(AddEditMemberActivity.this)
                .setMessage("Do you want to delete " + name + "?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Boolean> call = apiInterface.deletePerson(member.getId());
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                call.cancel();
                            }
                        });
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void setListenerForButtonSaveAndCancel(final boolean isAdd, final boolean isMembershipExpired) {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromLayout(isAdd, isMembershipExpired);
                if (isAdd) {
                    setDataAboutMember();
                } else {
                    editMember(isMembershipExpired);
                }
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDataFromLayout(boolean isAdd, boolean isMembershipExpired) {
        nameS = name.getText().toString();
        surnameS = surname.getText().toString();
        birth_dateS = birth_date.getText().toString();
        phoneS = phone.getText().toString();
        addressS = address.getText().toString();
        roleS = "member";
        if (isAdd) {
            emailS = email.getText().toString();
            genderS = gender.getSelectedItem().toString();
            periodS = periodSpinner.getSelectedItemPosition();
            tariffS = tariffSpinner.getSelectedItemPosition();
        }
        else if(isMembershipExpired){
            periodS = periodSpinner.getSelectedItemPosition();
            tariffS = tariffSpinner.getSelectedItemPosition();
        }
    }

    private void setDataAboutMember() {
        final boolean gender;
        if (genderS.equals("Male")) {
            gender = true;
        } else {
            gender = false;
        }
        getPeriodTariff();
        createMembershipAndPerson(gender);
    }

    private void createMembershipAndPerson(final boolean gender) {
        Membership membership = new Membership(-1, period.getName() + " " + tariff.getName(), price, new Date(), calendar.getTime(), true);
        Call<Integer> call = apiInterface.postMembership(new MembershipModel(membership));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int idMembership = response.body();

                final Call<MembershipModel> call3 = apiInterface.getMembershipById(idMembership);
                call3.enqueue(new Callback<MembershipModel>() {
                    @Override
                    public void onResponse(Call<MembershipModel> call, Response<MembershipModel> response) {
                        MembershipModel membershipModel = response.body();
                        createPerson(membershipModel, gender);
                    }

                    @Override
                    public void onFailure(Call<MembershipModel> call, Throwable t) {
                        call3.cancel();
                    }
                });

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void createPerson(MembershipModel membershipModel, boolean gender) {
        Account account = new Account(-1, emailS, "pass", null);
        try {
            Person person = new Person(-1, nameS, surnameS, formatter.parse(birth_dateS), gender, new Role(1, roleS), membershipModel.getMembership(), account, phoneS, addressS, false, true);
            Call<Boolean> call4 = apiInterface.postPerson(new PersonModel(person));
            call4.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    boolean b = response.body();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    call.cancel();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void editMember(boolean isMembershipExpired) {
        try {
            if(isMembershipExpired){
                getPeriodTariff();
                Membership membership = new Membership(member.getMembership().getId(),period.getName() + " " + tariff.getName(), price, new Date(), calendar.getTime(), true);
                Call<Boolean> call = apiInterface.putMembership(new MembershipModel(membership));
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        boolean b = response.body();

                        Call<MembershipModel> call1 = apiInterface.getMembershipById(member.getMembership().getId());
                        call1.enqueue(new Callback<MembershipModel>() {
                            @Override
                            public void onResponse(Call<MembershipModel> call1, Response<MembershipModel> response) {
                                MembershipModel membershipModel = response.body();
                                Person person = null;
                                try {
                                    person = new Person(member.getId(), nameS, surnameS, formatter.parse(birth_dateS), member.isMale(), member.getRole(), membershipModel.getMembership() , member.getAccount(), phoneS, addressS, member.isInGym(), member.isActive());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                updatePerson(person);
                            }

                            @Override
                            public void onFailure(Call<MembershipModel> call1, Throwable t) {
                                call1.cancel();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
            else{
                Person person = new Person(member.getId(), nameS, surnameS, formatter.parse(birth_dateS), member.isMale(), member.getRole(), member.getMembership(), member.getAccount(), phoneS, addressS, member.isInGym(), member.isActive());
                updatePerson(person);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getTariffList() {
        Call<List<TariffModel>> call2 = apiInterface.getListOfTariffs();
        call2.enqueue(new Callback<List<TariffModel>>() {
            @Override
            public void onResponse(Call<List<TariffModel>> call2, Response<List<TariffModel>> response) {
                List<TariffModel> tariffModelList = response.body();
                for (TariffModel el : tariffModelList) {
                    tariffList.add(new Tariff(el.getTariff().getId(), el.getTariff().getName(), el.getTariff().getDiscount()));
                }
                List<String> spinnerTariffList = new ArrayList<String>();
                for (Tariff el : tariffList) {
                    spinnerTariffList.add(el.getName());
                }
                ArrayAdapter<String> dataAdapterTariff = new ArrayAdapter<String>(AddEditMemberActivity.this,
                        R.layout.spinner_item, spinnerTariffList);
                tariffSpinner.setAdapter(dataAdapterTariff);
            }

            @Override
            public void onFailure(Call<List<TariffModel>> call2, Throwable t) {
                call2.cancel();
            }
        });
    }

    private void getPeriodList() {
        Call<List<PeriodModel>> call = apiInterface.getListOfPeriods();
        call.enqueue(new Callback<List<PeriodModel>>() {
            @Override
            public void onResponse(Call<List<PeriodModel>> call, Response<List<PeriodModel>> response) {
                List<PeriodModel> periodModelList = response.body();
                for (PeriodModel el : periodModelList) {
                    periodList.add(new Period(el.getPeriod().getId(), el.getPeriod().getName(), el.getPeriod().getPrice(), el.getPeriod().getDuration()));
                }
                List<String> spinnerPeriodList = new ArrayList<>();
                for (Period el : periodList) {
                    spinnerPeriodList.add(el.getName());
                }
                ArrayAdapter<String> dataAdapterPeriod = new ArrayAdapter<String>(AddEditMemberActivity.this,
                        R.layout.spinner_item, spinnerPeriodList);
                periodSpinner.setAdapter(dataAdapterPeriod);
            }

            @Override
            public void onFailure(Call<List<PeriodModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }
    private void getPeriodTariff(){
        period = periodList.get(periodS);
        tariff = tariffList.get(tariffS);
        if (tariff.getDiscount() == 0) {
            price = period.getPrice();
        } else {
            double discount = 1-tariff.getDiscount()/100.0;
            int periodPrice = period.getPrice();
            price =(int) (periodPrice * discount);
        }
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, period.getDuration());
    }
    private void updatePerson(Person person){
        Call<Boolean> call = apiInterface.putPerson(new PersonModel(person));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean b = response.body();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
