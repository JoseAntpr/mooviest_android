package com.mooviest.ui.rest;

import com.mooviest.ui.models.MooviestApiResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jesus on 26/8/16.
 */
public interface MooviestApiInterface {

    @GET("users/{id}/")
    Call<UserProfileResponse> getUserProfile(@Path("id") int id);

    @FormUrlEncoded
    @PUT("users/{id}/")
    Call<UpdateProfileResponse> updateUserProfile(
            @Path("id") int id,
            @Field("username") String username,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("profile.born") String born,
            @Field("profile.city") String city,
            @Field("profile.postalCode") String postalCode,
            @Field("profile.lang.code") String code
    );

    @FormUrlEncoded
    @POST("users/")
    Call<SignupResponse> signup(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("profile.lang.code") String code
    );

    @FormUrlEncoded
    @POST("users/login/")
    Call<LoginResponse> login(@Field("username") String emailUsername, @Field("password") String password);

    @GET("movie/")
    Call<MooviestApiResult> movie(@Query("search") String name);

    @GET("movie_app_bylang")
    Call<MooviestApiResult> movie_app_bylang(@Query("lang_id") int lang_id, @Query("limit") int limit);

}
