package com.mooviest.ui.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.adapters.ViewPagerAdapter;
import com.mooviest.ui.models.Collection;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.GetUserList;
import com.mooviest.ui.tasks.MovieCollectionInterface;
import com.mooviest.ui.tasks.UpdateMovieCollection;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements MovieCollectionInterface {

    ImageView cover_detail;
    TextView title_detail;
    TextView average_detail;
    TextView runtime_detail;
    ViewPager detail_pager;
    TabLayout detail_tabs;
    LinearLayout linear_collapsing_detail;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floating_action_blacklist;
    FloatingActionButton floating_action_seen;
    FloatingActionButton floating_action_watchlist;
    FloatingActionButton floating_action_favourite;


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

        // Floating buttons
        setupFloatingButtons();

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

    public void deleteMovieFromList(String typeMovie){
        if(SingletonRestClient.getInstance().moviesListAdapter != null){
            SingletonRestClient.getInstance().moviesListAdapter.removeItem(SingletonRestClient.getInstance().movie_selected);
            SingletonRestClient.getInstance().moviesListAdapter.notifyDataSetChanged();
        }
        switch (typeMovie){
            case "seen":
                if(SingletonRestClient.getInstance().seenListAdapter.getItemCount() == 10){
                    GetUserList getSeenList = new GetUserList("seen_list"){
                        @Override
                        protected void onPostExecute(MooviestApiResult result) {
                            super.onPostExecute(result);
                            if(result!=null) {
                                if(result.getMovies().size() > 0) {
                                    SingletonRestClient.getInstance().seenListAdapter.reloadItems(result.getMovies());
                                    SingletonRestClient.getInstance().seenListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    };
                    getSeenList.execute(1);
                }else{
                    SingletonRestClient.getInstance().seenListAdapter.removeItem(SingletonRestClient.getInstance().movie_selected);
                    SingletonRestClient.getInstance().seenListAdapter.notifyDataSetChanged();
                }
                break;
            case "watchlist":
                if(SingletonRestClient.getInstance().watchlistAdapter.getItemCount() == 10){
                    GetUserList getSeenList = new GetUserList("watchlist"){
                        @Override
                        protected void onPostExecute(MooviestApiResult result) {
                            super.onPostExecute(result);
                            if(result!=null) {
                                if(result.getMovies().size() > 0) {
                                    SingletonRestClient.getInstance().watchlistAdapter.reloadItems(result.getMovies());
                                    SingletonRestClient.getInstance().watchlistAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    };
                    getSeenList.execute(1);
                }else {
                    SingletonRestClient.getInstance().watchlistAdapter.removeItem(SingletonRestClient.getInstance().movie_selected);
                    SingletonRestClient.getInstance().watchlistAdapter.notifyDataSetChanged();
                }
                break;
            case "favourite":
                if(SingletonRestClient.getInstance().favouriteListAdapter.getItemCount() == 10){
                    GetUserList getSeenList = new GetUserList("favourite_list"){
                        @Override
                        protected void onPostExecute(MooviestApiResult result) {
                            super.onPostExecute(result);
                            if(result!=null) {
                                if(result.getMovies().size() > 0) {
                                    SingletonRestClient.getInstance().favouriteListAdapter.reloadItems(result.getMovies());
                                    SingletonRestClient.getInstance().favouriteListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    };
                    getSeenList.execute(1);
                }else {
                    SingletonRestClient.getInstance().favouriteListAdapter.removeItem(SingletonRestClient.getInstance().movie_selected);
                    SingletonRestClient.getInstance().favouriteListAdapter.notifyDataSetChanged();
                }
                break;
            case "blacklist":
                if(SingletonRestClient.getInstance().blacklistAdapter.getItemCount() == 10){
                    GetUserList getSeenList = new GetUserList("blacklist"){
                        @Override
                        protected void onPostExecute(MooviestApiResult result) {
                            super.onPostExecute(result);
                            if(result!=null) {
                                if(result.getMovies().size() > 0) {
                                    SingletonRestClient.getInstance().blacklistAdapter.reloadItems(result.getMovies());
                                    SingletonRestClient.getInstance().blacklistAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    };
                    getSeenList.execute(1);
                }else {
                    SingletonRestClient.getInstance().blacklistAdapter.removeItem(SingletonRestClient.getInstance().movie_selected);
                    SingletonRestClient.getInstance().blacklistAdapter.notifyDataSetChanged();
                }
                break;
        };

    }

    public void addMovieToList(String typeMovie){
        switch (typeMovie){
            case "seen":
                SingletonRestClient.getInstance().seenListAdapter.addItem(SingletonRestClient.getInstance().movie_selected);
                SingletonRestClient.getInstance().seenListAdapter.notifyDataSetChanged();
                break;
            case "watchlist":
                SingletonRestClient.getInstance().watchlistAdapter.addItem(SingletonRestClient.getInstance().movie_selected);
                SingletonRestClient.getInstance().watchlistAdapter.notifyDataSetChanged();
                break;
            case "favourite":
                SingletonRestClient.getInstance().favouriteListAdapter.addItem(SingletonRestClient.getInstance().movie_selected);
                SingletonRestClient.getInstance().favouriteListAdapter.notifyDataSetChanged();
                break;
            case "blacklist":
                SingletonRestClient.getInstance().blacklistAdapter.addItem(SingletonRestClient.getInstance().movie_selected);
                SingletonRestClient.getInstance().blacklistAdapter.notifyDataSetChanged();
                break;
        };
    }

    @Override
    public void updateMovieCollectionResponse(Collection result) {
        if(result != null){

            // Volver a habilitar el botón de su typeMovie anterior
            disableEnableButton(SingletonRestClient.getInstance().movie_selected.getCollection().getTypeMovie(), true);

            // Eliminamos la película seleccionada de la lista en la que se encontraba o recargamos toda la lista si había 10 películas
            // Ésto lo hacemos por si en esa lista tiene más de 10 en la BD, seguirá habiendo 10 en la lista de previsualizaciones
            deleteMovieFromList(SingletonRestClient.getInstance().movie_selected.getCollection().getTypeMovie());

            // Set new Collection to movie_selected
            SingletonRestClient.getInstance().movie_selected.setCollection(result);

            // Después la añadimos a la lista seleccionada
            addMovieToList(result.getTypeMovie());

            View floatingActionMenuView = findViewById(R.id.floating_action_menu);
            Snackbar.make(floatingActionMenuView,
                    "Movida a la lista seleccionada correctamente", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

    }

    private void movieCollectionTask(String typeMovie){
        Movie m = SingletonRestClient.getInstance().movie_selected;
        Collection collection = SingletonRestClient.getInstance().movie_selected.getCollection();

        if(collection != null) {
            UpdateMovieCollection updateMovieCollection = new UpdateMovieCollection(MovieDetailActivity.this);
            updateMovieCollection.execute(collection.getId(), getTypeMovieId(typeMovie));
        }else{
            // La película no se encuentre en ninguna lista
        }
    }

    private int getTypeMovieId(String typeMovie){
        int typeMovieId = 1;
        switch (typeMovie){
            case "seen":
                typeMovieId = 1;
                break;
            case "watchlist":
                typeMovieId = 2;
                break;
            case "favourite":
                typeMovieId = 3;
                break;
            case "blacklist":
                typeMovieId = 5;
                break;
        };

        return typeMovieId;
    }

    private void disableEnableButton(String typeMovie, boolean action){
        switch (typeMovie){
            case "seen":
                floating_action_seen.setEnabled(action);
                break;
            case "watchlist":
                floating_action_watchlist.setEnabled(action);
                break;
            case "favourite":
                floating_action_favourite.setEnabled(action);
                break;
            case "blacklist":
                floating_action_blacklist.setEnabled(action);
                break;
        };
    }

    private void setupFloatingButtons(){

        floating_action_seen = (FloatingActionButton) findViewById(R.id.floating_action_seen);
        floating_action_seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floating_action_seen.setEnabled(false);
                movieCollectionTask("seen");
            }
        });

        floating_action_watchlist = (FloatingActionButton) findViewById(R.id.floating_action_watchlist);
        floating_action_watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floating_action_watchlist.setEnabled(false);
                movieCollectionTask("watchlist");
            }
        });

        floating_action_favourite = (FloatingActionButton) findViewById(R.id.floating_action_favourite);
        floating_action_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floating_action_favourite.setEnabled(false);
                movieCollectionTask("favourite");
            }
        });


        floating_action_blacklist = (FloatingActionButton) findViewById(R.id.floating_action_blacklist);
        floating_action_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floating_action_blacklist.setEnabled(false);
                movieCollectionTask("blacklist");
            }
        });


        // Deshabilitar botón perteneciente a su typeMovie
        if(SingletonRestClient.getInstance().movie_selected.getCollection() != null){
            disableEnableButton(SingletonRestClient.getInstance().movie_selected.getCollection().getTypeMovie(), false);
        }
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
