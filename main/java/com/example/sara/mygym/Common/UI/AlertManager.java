package com.example.sara.mygym.Common.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sara.mygym.Common.Models.Person;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.AccountModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sara on 20.1.2018..
 */

public class AlertManager {
    public static void runDialogSettings(Context context, final Person user, final ApiInterface apiInterface){
        final EditText oldPassword = new EditText(context);
        final EditText newPassword1 = new EditText(context);
        final EditText newPassword2 = new EditText(context);
        final TextView error = new TextView(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Change password");

        oldPassword.setHint("Enter current password...");
        layout.addView(oldPassword);

        newPassword1.setHint("Enter new password...");
        layout.addView(newPassword1);

        newPassword2.setHint("Enter new password again...");
        layout.addView(newPassword2);

        error.setTextSize(17);

        layout.addView(error);

        builder1.setView(layout);

        builder1.setPositiveButton(
                "Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newPass1 = newPassword1.getText().toString();
                        String newpass2 = newPassword2.getText().toString();
                        if (newPass1.equals(newpass2)) {
                            user.getAccount().setPassword(newPass1);
                            Call<Void> call = apiInterface.putAccount(new AccountModel(user.getAccount()));
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
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

        newPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newPass1 = newPassword1.getText().toString();
                String newpass2 = newPassword2.getText().toString();
                if (newPass1.equals(newpass2)) {
                    error.setText("New passwords match");
                    error.setTextColor(Color.GREEN);
                } else {
                    error.setText("New passwords don't match");
                    error.setTextColor(Color.RED);
                }
            }
        });
    }

    public static void runDialogSingOut(final Context context, final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("You you want to sing out?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }
}
