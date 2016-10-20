package com.mooviest.ui.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.adapters.MoviesListAdapter;
import com.mooviest.ui.listeners.EndlessRecyclerViewScrollListener;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.SearchMovies;
import com.mooviest.ui.tasks.SearchMoviesInterface;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity implements SearchMoviesInterface{

    private String query;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private Boolean next;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // LOAD RECYCLERVIEW
        recyclerView = (RecyclerView) findViewById(R.id.movies_search_list_recycler);
        layoutManager = new GridLayoutManager(SearchableActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);

        if(savedInstanceState != null){
            query = savedInstanceState.getString("QUERY");
            next = savedInstanceState.getBoolean("NEXT");
            count = savedInstanceState.getInt("COUNT");
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList("MOVIES_ADAPTER");
            SingletonRestClient.getInstance().moviesListAdapter = new MoviesListAdapter(movies);
        }else {
            SingletonRestClient.getInstance().moviesListAdapter = new MoviesListAdapter();
            next = false;
            count = 0;
        }

        recyclerView.setAdapter(SingletonRestClient.getInstance().moviesListAdapter);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction()) && savedInstanceState == null) {
            query = intent.getStringExtra(SearchManager.QUERY);
            next = true;
            SearchMovies searchMovies = new SearchMovies(SearchableActivity.this, query, 1);
            searchMovies.execute();
        }

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if(next && (count != totalItemsCount)) {
                    SearchMovies searchMovies = new SearchMovies(SearchableActivity.this, query, page);
                    searchMovies.execute();
                }
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_movies_search_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(query);

    }

    @Override
    public void searchResponse(MooviestApiResult result) {
        if(result!=null) {
            if(result.getMovies().size() > 0) {
                count = result.getCount();
                if(result.getNext() == null){
                    next = false;
                }
                int curSize = SingletonRestClient.getInstance().moviesListAdapter.getItemCount();
                SingletonRestClient.getInstance().moviesListAdapter.addItems(result.getMovies());
                int totalMovies = SingletonRestClient.getInstance().moviesListAdapter.getItemCount();
                SingletonRestClient.getInstance().moviesListAdapter.notifyItemRangeInserted(curSize, totalMovies-1);
            }else{
                Toast.makeText(getApplication(), "No movies list", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingletonRestClient.getInstance().moviesListAdapter = null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("QUERY", query);
        savedInstanceState.putBoolean("NEXT", next);
        savedInstanceState.putInt("COUNT", count);
        savedInstanceState.putParcelableArrayList("MOVIES_ADAPTER", SingletonRestClient.getInstance().moviesListAdapter.getItems());
        super.onSaveInstanceState(savedInstanceState);
    }
}
