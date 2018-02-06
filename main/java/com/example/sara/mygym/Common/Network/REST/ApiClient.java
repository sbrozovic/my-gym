package com.example.sara.mygym.Common.Network.REST;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.sara.mygym.Common.Models.AboutUs;
import com.example.sara.mygym.Common.Network.Wrappers.AboutUsModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sara on 14.1.2018..
 */

public class ApiClient{
    public static final String BASE_URL = "input server url...";
    private static Retrofit retrofit = null;
    private static ApiInterface apiInterfaceInstance;

    private static Gson gson = new GsonBuilder().create();

    public static ApiInterface getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            apiInterfaceInstance = retrofit.create( ApiInterface.class );
        }
        return apiInterfaceInstance;

    }
}
