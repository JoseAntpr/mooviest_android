package com.mooviest.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.mooviest.ui.activities.home.HomeActivity;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.responses.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.swipe.GetSwipeList;
import com.mooviest.ui.tasks.movie_collection.GetUserMoviesList;
import com.mooviest.ui.tasks.InitialValuesInterface;

import java.util.ArrayList;

/**
 * Created by jesus on 25/10/16.
 */

public class GetInitialValues implements InitialValuesInterface {

    private Activity activity;
    private Context context;
    private static int countGetLists;
    private static final Object countGetListsLock = new Object();

    public GetInitialValues(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void getValues(){
        SharedPreferences user_prefs = activity.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

        countGetLists = 0;

        // GET SWIPE LIST
        GetSwipeList getSwipeList = new GetSwipeList(GetInitialValues.this);
        getSwipeList.execute(user_prefs.getInt("id", 0));


        // GET USER LISTS
        GetUserMoviesList getSeenList = new GetUserMoviesList("seen", GetInitialValues.this);
        getSeenList.execute(1);

        GetUserMoviesList getWatchlist = new GetUserMoviesList("watchlist", GetInitialValues.this);
        getWatchlist.execute(1);

        GetUserMoviesList getFavouriteList = new GetUserMoviesList("favourite", GetInitialValues.this);
        getFavouriteList.execute(1);

        GetUserMoviesList getBlacklist = new GetUserMoviesList("blacklist", GetInitialValues.this);
        getBlacklist.execute(1);
    }


    @Override
    public void swipeResponse(MooviestApiResult result) {

        if(result != null) {
            SingletonRestClient.getInstance().movies_buffer = result.getMovies();

            incrementCountLists();

            if(getCountLists() == 5){
                intentHome();
            }
        }

    }

    @Override
    public void listsResponse(String list_name, MooviestApiResult result) {
        switch (list_name) {
            case "seen":
                if(result != null) {
                    SingletonRestClient.getInstance().seen_list = result.getMovies();
                }else{
                    SingletonRestClient.getInstance().seen_list = new ArrayList<Movie>();
                }
                break;
            case "watchlist":
                if(result != null) {
                    SingletonRestClient.getInstance().watchlist = result.getMovies();
                }else{
                    SingletonRestClient.getInstance().watchlist = new ArrayList<Movie>();
                }
                break;
            case "favourite":
                if(result != null) {
                    SingletonRestClient.getInstance().favourite_list = result.getMovies();
                }else{
                    SingletonRestClient.getInstance().favourite_list = new ArrayList<Movie>();
                }
                break;
            case "blacklist":
                if(result != null) {
                    SingletonRestClient.getInstance().blacklist = result.getMovies();
                }else{
                    SingletonRestClient.getInstance().blacklist = new ArrayList<Movie>();
                }
                break;
        };

        if(result != null){
            incrementCountLists();

            if(getCountLists() == 5){
                intentHome();
            }
        }
    }

    public void intentHome(){
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.getApplicationContext().startActivity(intent);

        // close this activity
        ((Activity) context).finish();
    }


    public void incrementCountLists() {
        synchronized (countGetListsLock) {
            countGetLists++;
        }
    }


    public int getCountLists(){
        synchronized (countGetListsLock){
            return countGetLists;
        }
    }
}
