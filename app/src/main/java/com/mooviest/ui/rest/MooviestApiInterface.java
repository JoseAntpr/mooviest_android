package com.mooviest.ui.rest;

import com.mooviest.ui.models.Movie;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jesus on 26/8/16.
 */
public interface MooviestApiInterface {

    // ********** USER ************

    @GET("users/{id}/")
    Call<UserProfileResponse> getUserProfile(@Path("id") int id);

    @Multipart
    @PUT("users/{id}/")
    Call<UpdateProfileResponse> updateUserProfile(
            @Path("id") int id,
            @Part("username") RequestBody username,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("profile.born") RequestBody born,
            @Part("profile.city") RequestBody city,
            @Part("profile.postalCode") RequestBody postalCode,
            @Part("profile.lang.code") RequestBody code,
            @Part MultipartBody.Part imageFile
    );

    @GET("users/{id}/{list_name}/")
    Call<MooviestApiResult> getUserList(@Path("id") int id, @Path("list_name") String list_name, @Query("page") int page);

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

    @FormUrlEncoded
    @POST("collection/")
    Call<LoginResponse> setMovieCollection(@Field("user") int user, @Field("movie") int movie, @Field("typeMovie") int typeMovie);

    @FormUrlEncoded
    @PATCH("collection/{id}/")
    Call<LoginResponse> updateMovieCollection(@Path("id") int id, @Field("typeMovie") int typeMovie);

    // ********** MOVIE ************

    @GET("movie/")
    Call<MooviestApiResult> movie(@Query("search") String name);

    @GET("movie/{id}/")
    Call<Movie> getMovieDetail(@Path("id") int id, @Query("movie_lang_id") int movie_lang_id, @Query("user_id") int user_id);

    @GET("movie_app_bylang")
    Call<MooviestApiResult> movie_app_bylang(@Query("lang_id") int lang_id, @Query("limit") int limit);

}
