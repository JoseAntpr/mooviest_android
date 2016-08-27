package com.mooviest.ui.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jesus on 26/8/16.
 */
public interface MooviestApiInterface {

    @GET("movie/")
    Call<ResponseBody> movie(@Query("search") String name);

    @GET("movie_app_bylang")
    Call<ResponseBody> movie_app_bylang(@Query("lang_id") int lang_id, @Query("limit") int limit);

}
