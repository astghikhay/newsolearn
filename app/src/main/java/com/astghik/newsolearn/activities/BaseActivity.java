package com.astghik.newsolearn.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.astghik.newsolearn.R;
import com.astghik.newsolearn.app.NewsoLearnApp;
import com.astghik.newsolearn.managers.NetworkServiceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BaseActivity extends AppCompatActivity {
    //todo
    public final String BASE_URL = "https://content.guardianapis.com/";
    public Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    private NewsoLearnApp newsoLearnApp;

    public NetworkServiceManager getNetworkServiceManager() {
        return newsoLearnApp.getNetworkServiceManager();
    }


    public NewsoLearnApp getNewsoLearnApp() {
        return newsoLearnApp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        newsoLearnApp = NewsoLearnApp.getInstance(this);
        if (savedInstanceState != null) {
            newsoLearnApp.loadState(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
    }

    public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
