package com.astghik.newsolearn.requests;

import com.astghik.newsolearn.models.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiRequest {

    @Headers("api-key: 57ab48e2-ae69-4bd5-8395-dc7090b67202")
    @GET("search?format=json")
    Call<Response> getNeews(@Query("show-fields") String fields,
                            @Query("page-size") String pageSize,
                            @Query("page") String page);

}
