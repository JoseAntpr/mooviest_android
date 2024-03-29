package com.mooviest.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.adapters.MoviesListAdapter;
import com.mooviest.ui.listeners.EndlessRecyclerViewScrollListener;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.responses.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.movie_collection.GetUserList;

import java.util.ArrayList;

public class MoviesListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private String title;
    private String list_name;
    private Boolean next;
    private int count;
    private ArrayList<Movie> moviesList;
    private MovieActions movieActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_movies_list);

        // Acciones para clasificar las películas en una clase externa
        movieActions = new MovieActions();


        if(savedInstanceState != null){
            SharedPreferences user_prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
            movieActions.restoreMainInstanceState(savedInstanceState, user_prefs);

            title = savedInstanceState.getString("TITLE");
            list_name = savedInstanceState.getString("LIST_NAME");
            next = savedInstanceState.getBoolean("NEXT");
            count = savedInstanceState.getInt("COUNT");
            if(SingletonRestClient.getInstance().moviesListAdapter == null) {
                moviesList = savedInstanceState.getParcelableArrayList("MOVIES_ADAPTER");
            }else {
                moviesList = SingletonRestClient.getInstance().moviesListAdapter.getItems();
            }
        }else{
            // GET TAGS INTENT
            Intent i =getIntent();
            title = getString(i.getIntExtra("TITLE",R.string.app_name));
            list_name = i.getStringExtra("LIST_NAME");
            next = i.getBooleanExtra("NEXT",false);
            count = i.getIntExtra("COUNT",0);
            moviesList = SingletonRestClient.getInstance().movies_list;
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_movies_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        // LOAD RECYCLERVIEW
        recyclerView = (RecyclerView) findViewById(R.id.movies_list_recycler);
        layoutManager = new GridLayoutManager(MoviesListActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);


        SingletonRestClient.getInstance().moviesListAdapter = new MoviesListAdapter(moviesList, list_name);

        recyclerView.setAdapter(SingletonRestClient.getInstance().moviesListAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if(next && (count != totalItemsCount)) {
                    customLoadMoreDataFromApi(page);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingletonRestClient.getInstance().moviesListAdapter = null;
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int page) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        //  --> Deserialize API response and then construct new objects to append to the adapter
        //  --> Notify the adapter of the changes
        GetUserList getUserList = new GetUserList(list_name){
            @Override
            protected void onPostExecute(MooviestApiResult result) {
                super.onPostExecute(result);
                if(result!=null) {
                    if(result.getMovies().size() > 0) {
                        if(result.getNext() == null){
                            next = false;
                        }
                        if(SingletonRestClient.getInstance().moviesListAdapter != null) {
                            int curSize = SingletonRestClient.getInstance().moviesListAdapter.getItemCount();
                            SingletonRestClient.getInstance().moviesListAdapter.addItems(result.getMovies());
                            int totalMovies = SingletonRestClient.getInstance().moviesListAdapter.getItemCount();
                            SingletonRestClient.getInstance().moviesListAdapter.notifyItemRangeInserted(curSize, totalMovies - 1);
                        }
                    }else{
                        Toast.makeText(getApplication(), "No movies list", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        // Params: page API result
        getUserList.execute(page);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState = movieActions.saveMainInstanceState(savedInstanceState);
        savedInstanceState.putString("TITLE", title);
        savedInstanceState.putString("LIST_NAME", list_name);
        savedInstanceState.putBoolean("NEXT", next);
        savedInstanceState.putInt("COUNT", count);
        savedInstanceState.putParcelableArrayList("MOVIES_ADAPTER", SingletonRestClient.getInstance().moviesListAdapter.getItems());
        super.onSaveInstanceState(savedInstanceState);
    }
}
