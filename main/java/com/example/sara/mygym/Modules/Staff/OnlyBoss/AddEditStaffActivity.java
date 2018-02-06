package com.example.sara.mygym.Modules.Staff.OnlyBoss;

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
import com.example.sara.mygym.Common.Models.Role;
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.PersonModel;
import com.example.sara.mygym.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditStaffActivity extends AppCompatActivity {
    EditText name;
    EditText surname;
    EditText birth_date;
    EditText phone;
    EditText email;
    EditText address;
    Spinner gender;
    Spinner isPresent;


    TextView resetPass;
    ImageView deletStaff;

    String nameS;
    String surnameS;
    String birth_dateS;
    String phoneS;
    String emailS;
    String addressS;
    String genderS;
    String isPresentS;


    List<String> genderList = new ArrayList<>();
    List<String> isPresentList = new ArrayList<>();

    Button save;
    Button cancel;

    ApiInterface apiInterface;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Person staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_staff);

        apiInterface = ApiClient.getClient();

        save = (Button) findViewById(R.id.buttonSaveStaff);
        cancel = (Button) findViewById(R.id.buttonCancelStaff);
        name = (EditText) findViewById(R.id.editTextNameStaff);
        surname = (EditText) findViewById(R.id.editTextSurnameStaff);
        birth_date = (EditText) findViewById(R.id.editTextBirthDayStaff);
        phone = (EditText) findViewById(R.id.editTextPhoneStaff);
        email = (EditText) findViewById(R.id.editTextEmailStaff);
        address = (EditText) findViewById(R.id.editTextAddressStaff);
        isPresent = (Spinner) findViewById(R.id.spinnerIsPresentStaff);
        resetPass = (TextView) findViewById(R.id.textViewResetPassStaff);
        deletStaff = (ImageView) findViewById(R.id.imageViewDeleteStaff);
        gender = (Spinner) findViewById(R.id.spinnerGenderStaff);

        setSpinners();

        TextView textViewGender = (TextView) findViewById(R.id.textViewGenderStaff);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewEmailStaff);

        String bundle = getIntent().getExtras().getString("Add_edit_opened");
        if (bundle.equals("add")) {
            setAdd();
        } else {
            textViewEmail.setVisibility(View.GONE);
            textViewGender.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);
            email.setVisibility(View.GONE);

            staff = (Person) getIntent().getSerializableExtra("Staff_element");
            setEdit(staff);
        }
    }

    private void setSpinners(){
        genderList.add("Male");
        genderList.add("Female");
        ArrayAdapter<String> dataAdapterGender = new ArrayAdapter<String>(this,
                R.layout.spinner_item, genderList);
        gender.setAdapter(dataAdapterGender);

        isPresentList.add("Yes");
        isPresentList.add("No");
        ArrayAdapter<String> dataAdapterIsPresent = new ArrayAdapter<String>(this,
                R.layout.spinner_item, isPresentList);
        isPresent.setAdapter(dataAdapterIsPresent);
    }

    private void setAdd() {
        setListenerForButtonSaveAndCancel(true);
    }

    private void setEdit(Person staff) {
        name.setText(staff.getName(), TextView.BufferType.EDITABLE);
        surname.setText(staff.getSurname(), TextView.BufferType.EDITABLE);
        birth_date.setText(formatter.format(staff.getBirthDate()), TextView.BufferType.EDITABLE);
        phone.setText(staff.getPhone(), TextView.BufferType.EDITABLE);
        address.setText(staff.getAddress(), TextView.BufferType.EDITABLE);
        if(staff.isInGym()){
            isPresent.setSelection(0);
        }
        else{
            isPresent.setSelection(1);
        }
        resetPass.setVisibility(View.VISIBLE);
        deletStaff.setVisibility(View.VISIBLE);

        setListenerForResetDelete();

        setListenerForButtonSaveAndCancel(false);
    }

    private void setListenerForResetDelete() {
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call = apiInterface.resetPassword(String.valueOf(staff.getId()));
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
        deletStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Boolean> call = apiInterface.deletePerson(staff.getId());
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
            }
        });
    }

    private void getDataFromLayout(boolean isAdd) {
        nameS = name.getText().toString();
        surnameS = surname.getText().toString();
        birth_dateS = birth_date.getText().toString();
        phoneS = phone.getText().toString();
        emailS = email.getText().toString();
        addressS = address.getText().toString();
        isPresentS = isPresent.getSelectedItem().toString();
        if (isAdd)
            genderS = gender.getSelectedItem().toString();
    }

    private void setListenerForButtonSaveAndCancel(final boolean isAdd) {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromLayout(isAdd);
                if (isAdd) {
                    addStaff();
                } else {
                    editStaff();
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

    private void addStaff() {
        boolean isMale;
        boolean isInGym;
        if (genderS.equals("Male")) {
            isMale = true;
        } else {
            isMale = false;
        }

        if(isPresentS.equals("Yes")){
            isInGym = true;
        }
        else{
            isInGym = false;
        }
        try {
            Person person = new Person(-1, nameS, surnameS, formatter.parse(birth_dateS), isMale, new Role(2, "staff"), null, new Account(-1, emailS, "pass", null), phoneS, addressS, isInGym, true);
            Call<Boolean> call = apiInterface.postPerson(new PersonModel(person));
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void editStaff() {
        boolean isInGym;
        if(isPresentS.equals("Yes")){
            isInGym = true;
        }
        else{
            isInGym = false;
        }
        try {
            Person person = new Person(staff.getId(), nameS, surnameS, formatter.parse(birth_dateS), staff.isMale(), staff.getRole(), null, staff.getAccount(), phoneS, addressS, isInGym, staff.isActive());
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
