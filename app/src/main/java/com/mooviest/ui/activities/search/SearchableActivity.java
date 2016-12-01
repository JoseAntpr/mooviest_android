package com.mooviest.ui.activities.search;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.mooviest.R;
import com.mooviest.ui.activities.MovieActions;
import com.mooviest.ui.adapters.MoviesListAdapter;
import com.mooviest.ui.listeners.EndlessRecyclerViewScrollListener;
import com.mooviest.ui.rest.responses.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.search.SearchMovies;
import com.mooviest.ui.tasks.search.SearchMoviesInterface;
import java.util.Locale;

public class SearchableActivity extends AppCompatActivity implements SearchMoviesInterface, SearchView.OnQueryTextListener{

    private String query;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private Boolean next;
    private int count;
    private String lang_code;
    private MovieActions movieActions;
    private FloatingSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // Acciones para clasificar, guardar y restaurar películas
        movieActions = new MovieActions();

        // LOAD RECYCLERVIEW
        recyclerView = (RecyclerView) findViewById(R.id.movies_search_list_recycler);
        layoutManager = new GridLayoutManager(SearchableActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);

        if(savedInstanceState != null){
            SharedPreferences user_prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
            movieActions.restoreMainInstanceState(savedInstanceState, user_prefs);

            query = savedInstanceState.getString("QUERY");
            next = savedInstanceState.getBoolean("NEXT");
            count = savedInstanceState.getInt("COUNT");

        }else {
            SingletonRestClient.getInstance().moviesListAdapter = new MoviesListAdapter();
            next = false;
            count = 0;
        }

        if(Locale.getDefault().getLanguage().equals("es")){
            lang_code = "es";
        }else{
            lang_code = "en";
        }


        recyclerView.setAdapter(SingletonRestClient.getInstance().moviesListAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if(next && (count != totalItemsCount)) {
                    SearchMovies searchMovies = new SearchMovies(SearchableActivity.this, query, lang_code, page);
                    searchMovies.execute();
                }
            }
        });

        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar_movies_search_list));

        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery

                //pass them on to the search view
                //mSearchView.swapSuggestions(newSuggestions);
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {

            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String query) {
                String queryReplaced = query.replace(" ", "");
                if(!queryReplaced.equals("")){
                    onQueryTextSubmit(query);
                }else {
                    mSearchView.clearQuery();
                }
            }
        });

        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                onBackPressed();
            }
        });
        if(savedInstanceState == null) {
            mSearchView.setSearchFocused(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this add SearchView to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_fixed, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_query_hint));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

        if(query == null) {
            searchView.setFocusable(true);
            searchView.requestFocusFromTouch();
        }else{
            searchView.setQuery(query, false);
        }

        // Remove underline
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(getResources().getColor(R.color.transparent));

        return true;
    }

    @Override
    public void searchResponse(MooviestApiResult result) {
        if(result!=null) {
            if(result.getMovies().size() > 0) {
                count = result.getCount();
                if(result.getNext() == null){
                    next = false;
                }
                if(SingletonRestClient.getInstance().moviesListAdapter != null) {
                    int curSize = SingletonRestClient.getInstance().moviesListAdapter.getItemCount();
                    SingletonRestClient.getInstance().moviesListAdapter.addItems(result.getMovies());
                    int totalMovies = SingletonRestClient.getInstance().moviesListAdapter.getItemCount();
                    SingletonRestClient.getInstance().moviesListAdapter.notifyItemRangeInserted(curSize, totalMovies);
                }
            }else{
                Toast.makeText(getApplication(), getString(R.string.no_movies_found)+ " " + query, Toast.LENGTH_LONG).show();
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
        savedInstanceState = movieActions.saveMainInstanceState(savedInstanceState);

        savedInstanceState.putString("QUERY", query);
        savedInstanceState.putBoolean("NEXT", next);
        savedInstanceState.putInt("COUNT", count);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.query = query;
        this.next = true;
        this.count = 0;

        SingletonRestClient.getInstance().moviesListAdapter.removeAllItems();
        SingletonRestClient.getInstance().moviesListAdapter.notifyDataSetChanged();

        SearchMovies searchMovies = new SearchMovies(SearchableActivity.this, query, lang_code, 1);
        searchMovies.execute();

        // Hide keyboard when click in search button
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
