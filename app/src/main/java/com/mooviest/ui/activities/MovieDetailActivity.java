package com.mooviest.ui.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.adapters.ViewPagerAdapter;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.SingletonRestClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    ImageView cover_detail;
    TextView title_detail;
    TextView average_detail;
    TextView runtime_detail;
    ViewPager detail_pager;
    TabLayout detail_tabs;
    LinearLayout linear_collapsing_detail;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_movie_detail);



        Movie movie = SingletonRestClient.getInstance().movie_selected;

        String image = movie.getImage();
        final String background;
        String cover;

        if(image.startsWith("http")){
            background = image;
            cover = image;
        }else{
            background = "https://img.tviso.com/ES/backdrop/w600" + image;
            cover = "https://img.tviso.com/ES/poster/w430" + image;
        }


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movie.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));

        // Load fragments
        detail_pager = (ViewPager) findViewById(R.id.detail_pager);
        detail_pager.setOffscreenPageLimit(2);
        setupViewPager(detail_pager);

        // Tabs
        detail_tabs = (TabLayout) findViewById(R.id.detail_tabs);
        detail_tabs.setupWithViewPager(detail_pager);
        detail_tabs.getTabAt(0).setText(getString(R.string.information));
        detail_tabs.getTabAt(1).setText(getString(R.string.cast));
        detail_tabs.getTabAt(2).setText(getString(R.string.watch));

        // Load background image
        final ImageView background_detail = (ImageView) findViewById(R.id.background_detail);
        Picasso.with(this).load(background).into(background_detail, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) background_detail.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            }

            @Override
            public void onError() {

            }
        });
        Picasso.with(this).setIndicatorsEnabled(false);


        // Load cover image
        cover_detail = (ImageView) findViewById(R.id.cover_detail);
        Picasso.with(this).load(cover).transform(new RoundedTransformation(10, 0)).into(cover_detail);
        Picasso.with(this).setIndicatorsEnabled(false);


        // Title
        title_detail = (TextView) findViewById(R.id.title_detail);
        title_detail.setText(movie.getTitle());

        // Mooviest average
        average_detail = (TextView) findViewById(R.id.average_detail);
        average_detail.setText(movie.getAverage());

        // Runtime
        runtime_detail = (TextView) findViewById(R.id.runtime_detail);
        runtime_detail.setText(movie.getRuntime() + " min");



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        setTitle(SingletonRestClient.getInstance().movie_selected.getTitle());
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_query_hint));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieInformationFragment(), "Information");
        adapter.addFragment(new MovieCastFragment(), "Cast");
        adapter.addFragment(new MovieWatchFragment(), "Watch");
        viewPager.setAdapter(adapter);
    }

    private void applyPalette(Palette palette) {
        int primary = getResources().getColor(R.color.colorPrimary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(primary));

        linear_collapsing_detail = (LinearLayout) findViewById(R.id.linear_collapsing_detail);
        linear_collapsing_detail.setBackgroundColor(palette.getMutedColor(primary));

        detail_tabs.setBackgroundColor(palette.getMutedColor(primary));

        supportStartPostponedEnterTransition();
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
