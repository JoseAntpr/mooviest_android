package com.mooviest.ui.rest;

import com.mooviest.ui.models.Movie;

import java.io.IOException;
import java.util.ArrayList;

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

    /* Para servidor local introducir la ip del pc donde se esté ejecutando el
     * servidor. Para que el servidor escuche llamadas realizadas desde fuera,
     * como es el caso del móvil, se debe ejecutar de la siguiente forma:
     *
     *      python3 manage.py runserver 0.0.0.0:8000
     */
    private static String baseUrl = "http://192.168.1.220:8000/api/";
    public static MooviestApiInterface mooviestApiInterface;
    private static Retrofit retrofit;
    public ArrayList<Movie> movies_buffer;

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
