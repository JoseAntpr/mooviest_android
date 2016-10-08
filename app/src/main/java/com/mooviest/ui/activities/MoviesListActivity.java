package com.mooviest.ui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;

import com.mooviest.R;
import com.mooviest.ui.adapters.MoviesListAdapter;
import com.mooviest.ui.rest.SingletonRestClient;

public class MoviesListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private MoviesListAdapter moviesListAdapter;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_movies_list);

        // GET TAG INTENT
        Intent i =getIntent();
        title = getString(i.getIntExtra("TITLE",R.string.app_name));

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_movies_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        // LOAD RECYCLERVIEW
        recyclerView = (RecyclerView) findViewById(R.id.movies_list_recycler);
        layoutManager = new GridLayoutManager(MoviesListActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);

        moviesListAdapter = new MoviesListAdapter(SingletonRestClient.getInstance().movies_list);
        recyclerView.setAdapter(moviesListAdapter);

        //recyclerView.addOnScrollListener();

    }


    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }
}
