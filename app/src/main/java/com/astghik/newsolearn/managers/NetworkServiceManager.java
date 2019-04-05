package com.astghik.newsolearn.managers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.astghik.newsolearn.activities.BaseActivity;
import com.astghik.newsolearn.app.ManagerContext;
import com.astghik.newsolearn.listeners.NetworkRequestListener;
import com.astghik.newsolearn.models.NewsModel;
import com.astghik.newsolearn.models.Response;
import com.astghik.newsolearn.requests.ApiRequest;
import com.astghik.newsolearn.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class NetworkServiceManager extends BaseManager {

    private BaseActivity baseActivity;
    private ArrayList<NewsModel> newsArrayList;

    public NetworkServiceManager(ManagerContext c, Context context) {
        super(c, context);
        baseActivity = (BaseActivity) context;
    }

    public void getNews(final NetworkRequestListener listener, String pageCount) {
        ApiRequest service = baseActivity.retrofit.create(ApiRequest.class);
        Call<Response> call = service.getNeews("headline,thumbnail", Constants.PAGE_SIZE_STR, pageCount);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response != null) {
                    Response result = response.body();
                    if (result != null) {
                        newsArrayList = (ArrayList<NewsModel>) result.getResult().getNewsList();
                        listener.onResponseReceive(newsArrayList);
                    }
                } else {
                    listener.onError("Error");
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (t != null) {
                }

            }
        });
    }


    /***/
    @Override
    public BaseManager.Initializer getInitializer() {
        return new AsyncInitializer();
    }

    @Override
    public synchronized void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public synchronized void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
    }
}
