package com.mooviest.ui.rest;

import android.app.Application;

import com.mooviest.ui.adapters.MoviesListAdapter;
import com.mooviest.ui.adapters.MoviesUserListAdapter;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.models.User;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jesus on 26/8/16.
 */
public class SingletonRestClient extends Application {

    /* En baseAPIUrl introducir la ip del pc donde se esté ejecutando el
     * servidor. Si se ejecuta la aplicación en el emulador android
     * introducir la ip equivalente a localhost que en android es
     * la siguiente: 10.0.2.2
     *
     * Para que el servidor django escuche llamadas realizadas desde fuera,
     * como es el caso del móvil, se debe ejecutar de la siguiente forma:
     *
     *      python3 manage.py runserver 0.0.0.0:8000
     *
     */
    private static String baseAPIUrl = "http://192.168.0.171:8000/api/";
    public static String baseUrl = "http://192.168.0.171:8000";
    public static MooviestApiInterface mooviestApiInterface;
    private static Retrofit retrofit;
    public ArrayList<Movie> movies_buffer;
    public ArrayList<Movie> movies_swipe;
    public ArrayList<Movie> movies_list;
    public MoviesListAdapter moviesListAdapter;
    public MoviesUserListAdapter seenListAdapter;
    public MoviesUserListAdapter watchlistAdapter;
    public MoviesUserListAdapter favouriteListAdapter;
    public MoviesUserListAdapter blacklistAdapter;
    public ArrayList<Movie> seen_list;
    public ArrayList<Movie> watchlist;
    public ArrayList<Movie> favourite_list;
    public ArrayList<Movie> blacklist;
    public Movie movie_selected;
    public User user;

    private static SingletonRestClient instance = new SingletonRestClient();

    public void init(){
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .header("User-Agent", "Mozilla/5.0")
                        .header("Authorization", "")
                        .header("Content-type", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient okClient = builder.build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseAPIUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();

        this.mooviestApiInterface=this.retrofit.create(MooviestApiInterface.class);
    }

    private SingletonRestClient(){
    }

    public static SingletonRestClient getInstance(){
        return instance;
    }

    public void setNewToken(String token){
        final String newToken = token;
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .header("User-Agent", "Mozilla/5.0")
                        .header("Authorization", "Token "+ newToken)
                        .header("Content-type", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient okClient = builder.build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseAPIUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();

        this.mooviestApiInterface=this.retrofit.create(MooviestApiInterface.class);
    }
}
