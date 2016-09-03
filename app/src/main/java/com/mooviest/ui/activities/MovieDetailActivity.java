package com.mooviest.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.mooviest.R;
import com.mooviest.ui.adapters.ViewPagerAdapter;
import com.mooviest.ui.rest.SingletonRestClient;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    ImageView image_detail;
    ViewPager detail_pager;
    TabLayout detail_tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Load movie image
        image_detail = (ImageView) findViewById(R.id.image_detail);
        Picasso.with(this).load(SingletonRestClient.getInstance().movie_selected.getLangs().get(0).getImage()).into(image_detail);
        Picasso.with(this).setIndicatorsEnabled(false);

        // Load fragments
        detail_pager = (ViewPager) findViewById(R.id.detail_pager);
        detail_pager.setOffscreenPageLimit(2);
        setupViewPager(detail_pager);

        detail_tabs = (TabLayout) findViewById(R.id.detail_tabs);
        detail_tabs.setupWithViewPager(detail_pager);
        detail_tabs.getTabAt(0).setText(getString(R.string.information));
        detail_tabs.getTabAt(1).setText(getString(R.string.cast));
        detail_tabs.getTabAt(2).setText(getString(R.string.watch));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        setTitle(SingletonRestClient.getInstance().movie_selected.getLangs().get(0).getTitle());
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_query_hint));
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieInformationFragment(), "Information");
        adapter.addFragment(new MovieCastFragment(), "Cast");
        adapter.addFragment(new MovieWatchFragment(), "Watch");
        viewPager.setAdapter(adapter);
    }
}
