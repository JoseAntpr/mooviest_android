package com.mooviest.ui.rest;

import com.mooviest.ui.models.MooviestApiResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jesus on 26/8/16.
 */
public interface MooviestApiInterface {

    @FormUrlEncoded
    @POST("users/")
    Call<SignupResponse> signup(@Field("username") String username, @Field("email") String email, @Field("password") String password, @Field("profile.lang.code") String code);

    @FormUrlEncoded
    @POST("users/login/")
    Call<LoginResponse> login(@Field("username") String emailUsername, @Field("password") String password);

    @GET("movie/")
    Call<MooviestApiResult> movie(@Query("search") String name);

    @GET("movie_app_bylang")
    Call<MooviestApiResult> movie_app_bylang(@Query("lang_id") int lang_id, @Query("limit") int limit);

}
