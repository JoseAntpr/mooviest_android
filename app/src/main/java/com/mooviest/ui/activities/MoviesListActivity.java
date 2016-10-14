package com.mooviest.ui.activities;

import android.content.Intent;
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
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.GetUserList;

import java.util.ArrayList;

public class MoviesListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private MoviesListAdapter moviesListAdapter;
    private String title;
    private String list_name;
    private Boolean next;
    private int count;
    private ArrayList<Movie> moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_movies_list);


        if(savedInstanceState != null){
            title = savedInstanceState.getString("TITLE");
            list_name = savedInstanceState.getString("LIST_NAME");
            next = savedInstanceState.getBoolean("NEXT");
            count = savedInstanceState.getInt("COUNT");
            moviesAdapter = savedInstanceState.getParcelableArrayList("MOVIES_ADAPTER");
        }else{
            // GET TAG INTENT
            Intent i =getIntent();
            title = getString(i.getIntExtra("TITLE",R.string.app_name));
            list_name = i.getStringExtra("LIST_NAME");
            next = i.getBooleanExtra("NEXT",false);
            count = i.getIntExtra("COUNT",0);
            moviesAdapter = SingletonRestClient.getInstance().movies_list;
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_movies_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        // LOAD RECYCLERVIEW
        recyclerView = (RecyclerView) findViewById(R.id.movies_list_recycler);
        layoutManager = new GridLayoutManager(MoviesListActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);


        moviesListAdapter = new MoviesListAdapter(moviesAdapter);

        recyclerView.setAdapter(moviesListAdapter);

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
                        int curSize = moviesListAdapter.getItemCount();
                        moviesListAdapter.addItems(result.getMovies());
                        int totalMovies = moviesListAdapter.getItemCount();
                        moviesListAdapter.notifyItemRangeInserted(curSize, totalMovies-1);
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
        savedInstanceState.putString("TITLE", title);
        savedInstanceState.putString("LIST_NAME", list_name);
        savedInstanceState.putBoolean("NEXT", next);
        savedInstanceState.putInt("COUNT", count);
        savedInstanceState.putParcelableArrayList("MOVIES_ADAPTER", moviesListAdapter.getItems());
        super.onSaveInstanceState(savedInstanceState);
    }
}
