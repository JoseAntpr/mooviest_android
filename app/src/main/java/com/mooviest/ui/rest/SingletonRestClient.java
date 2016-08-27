package com.mooviest.ui.rest;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jesus on 26/8/16.
 */
public class SingletonRestClient {

    //10.0.2.2 solo funciona con el emulador Android para probar
    //con el móvil no se todavía como funciona. Con la ip del Mac no va tampoco.
    private static String baseUrl = "http://10.0.2.2:8000/api/";
    public static MooviestApiInterface mooviestApiInterface;
    private static Retrofit retrofit;

    private static SingletonRestClient instance = new SingletonRestClient();

    private SingletonRestClient(){
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .header("User-Agent", "Mozilla/5.0")
                        .header("Authorization", Credentials.basic("jesus","root"))
                        .header("Content-type", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient okClient = builder.build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();

        this.mooviestApiInterface=this.retrofit.create(MooviestApiInterface.class);

    }

    public static SingletonRestClient getInstance(){
        return instance;
    }
}
