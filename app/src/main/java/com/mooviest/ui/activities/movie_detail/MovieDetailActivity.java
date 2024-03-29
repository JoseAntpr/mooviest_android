package com.mooviest.ui.activities.movie_detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.activities.MovieActions;
import com.mooviest.ui.adapters.ViewPagerAdapter;
import com.mooviest.ui.models.Collection;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.movie_collection.CreateMovieCollection;
import com.mooviest.ui.tasks.movie_collection.MovieCollectionInterface;
import com.mooviest.ui.tasks.movie_collection.UpdateMovieCollection;
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
    FloatingActionMenu floating_action_menu;
    FloatingActionButton floating_action_blacklist;
    FloatingActionButton floating_action_seen;
    FloatingActionButton floating_action_watchlist;
    FloatingActionButton floating_action_favourite;
    MovieActions movieActions;

    private String from;
    private Movie movie;
    private boolean savedInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivityTransitions();
        setContentView(R.layout.activity_movie_detail);

        // Acciones para clasificar las películas en una clase externa
        movieActions = new MovieActions();

        if (savedInstanceState != null) {
            savedInstance = true;

            SharedPreferences user_prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
            movieActions.restoreMainInstanceState(savedInstanceState, user_prefs);
            movie = savedInstanceState.getParcelable("MOVIE");
            SingletonRestClient.getInstance().movie_selected = movie;


            from = savedInstanceState.getString("FROM");
            if(from != "" && SingletonRestClient.getInstance().moviesListAdapter != null){
                SingletonRestClient.getInstance().moviesListAdapter.setListName(from);
            }

        }else{
            savedInstance = false;
            movie = SingletonRestClient.getInstance().movie_selected;

            Intent i =getIntent();
            if(i.hasExtra("FROM")){
                from = i.getStringExtra("FROM");
            }else{
                from = "";
            }
        }


        String image = movie.getImage();
        String backdrop = movie.getBackdrop();
        final String background;
        String cover;

        if(image == null || image == "") {
            cover = "";
        }else{
            if (image.startsWith("http")) {
                cover = image;
            } else if (image.startsWith("EXTERNAL#")) {
                cover = "";
            } else {
                cover = "https://img.tviso.com/ES/poster/w430" + image;
            }
        }

        if(backdrop == "" || backdrop == null){
            background = "";
        }else {
            background = "https://img.tviso.com/ES/backdrop/w600" + image;
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
        if(background != "") {
            Picasso.with(this).load(background).placeholder(R.drawable.background_red).into(background_detail, new Callback() {
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
                    applyDefaultPalette();
                }
            });
        }else{
            Picasso.with(this).load(R.drawable.background_red).into(background_detail);
            applyDefaultPalette();
        }
        Picasso.with(this).setIndicatorsEnabled(false);


        // Load cover image
        cover_detail = (ImageView) findViewById(R.id.cover_detail);
        if(cover != "") {
            Picasso.with(this).load(cover).transform(new RoundedTransformation(10, 0)).into(cover_detail);
        }
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

    private void setupFloatingButtons(){
        Collection collection = movie.getCollection();
        floating_action_menu = (FloatingActionMenu) findViewById(R.id.floating_action_menu);
        floating_action_menu.setIconAnimated(false);
        floating_action_menu.setClosedOnTouchOutside(true);
        if(collection != null) {
            setFabActionMenuDesign(collection.getTypeMovie());
        }

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
        if(collection != null){
            disableEnableButton(collection.getTypeMovie(), false);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieInformationFragment(), getString(R.string.information));
        adapter.addFragment(new MovieCastFragment(), getString(R.string.cast));
        adapter.addFragment(new MovieWatchFragment(), getString(R.string.watch));
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

    private void applyDefaultPalette(){
        int primary = getResources().getColor(R.color.colorPrimary);
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        collapsingToolbarLayout.setContentScrimColor(primary);
        collapsingToolbarLayout.setStatusBarScrimColor(primaryDark);

        linear_collapsing_detail = (LinearLayout) findViewById(R.id.linear_collapsing_detail);
        linear_collapsing_detail.setBackgroundColor(primary);

        detail_tabs.setBackgroundColor(primary);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle(movie.getTitle());

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

    @Override
    public void onBackPressed() {
        if(floating_action_menu.isOpened()){
            floating_action_menu.close(true);
        }else {
            if (from.equals("swipe")) {
                Intent intent = new Intent();
                if (movie.getCollection() != null) {
                    intent.putExtra("typeMovie", movie.getCollection().getTypeMovie());
                    if(savedInstance) {
                        intent.putExtra("savedInstance", true);
                    }
                } else {
                    intent.putExtra("typeMovie", "");
                }

                setResult(1, intent);
            }
            super.onBackPressed();
        }
    }

    @Override
    public void updateMovieCollectionResponse(Collection result) {
        if(result != null){
            Collection collection = movie.getCollection();

            // Volver a habilitar el botón de su typeMovie anterior
            disableEnableButton(collection.getTypeMovie(), true);

            // Eliminamos la película seleccionada de la lista en la que se encontraba o recargamos toda la lista si había 10 películas
            // Ésto lo hacemos por si en esa lista tiene más de 10 en la BD, seguirá habiendo 10 en la lista de previsualizaciones
            movieActions.deleteMovieFromList(collection.getTypeMovie(), movie);

            // Set new Collection to movie_selected
            SingletonRestClient.getInstance().movie_selected.setCollection(result);
            movie.setCollection(result);

            // Añadir o eliminar del adapter
            if(SingletonRestClient.getInstance().moviesListAdapter != null){
                movieActions.addDeleteFromAdapter(from, result.getTypeMovie(), movie);
            }


            // Después la añadimos a la lista seleccionada
            movieActions.addMovieToList(result.getTypeMovie(), movie);
            // Cambiamos icono del action menu y su color al seleccionado
            setFabActionMenuDesign(result.getTypeMovie());

            View floatingActionMenuView = findViewById(R.id.floating_action_menu);
            Snackbar.make(floatingActionMenuView,
                    getString(R.string.movie_updated), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

    }

    @Override
    public void createMovieCollectionResponse(Collection result) {
        if(result != null) {
            // Set new Collection to movie_selected
            SingletonRestClient.getInstance().movie_selected.setCollection(result);
            movie.setCollection(result);

            // Después la añadimos a la lista seleccionada
            movieActions.addMovieToList(result.getTypeMovie(), movie);

            // Cambiamos icono del action menu y su color al seleccionado
            setFabActionMenuDesign(result.getTypeMovie());

            View floatingActionMenuView = findViewById(R.id.floating_action_menu);
            Snackbar.make(floatingActionMenuView,
                    getString(R.string.movie_updated), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void movieCollectionTask(String typeMovie){
        Collection collection = movie.getCollection();

        if(collection != null) {
            //La película ya se encontraba en una lista y se actualiza a la nueva
            UpdateMovieCollection updateMovieCollection = new UpdateMovieCollection(MovieDetailActivity.this);
            updateMovieCollection.execute(collection.getId(), getTypeMovieId(typeMovie));
        }else{
            // La película no se encuentraba en ninguna lista y se introduce en la nueva
            CreateMovieCollection createMovieCollection = new CreateMovieCollection(MovieDetailActivity.this);

            SharedPreferences user_prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

            createMovieCollection.execute(user_prefs.getInt("id", 0), movie.getId(), getTypeMovieId(typeMovie));
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

    private void setFabActionMenuDesign(String typeMovie){
        int imageResource;
        int colorResource;
        switch (typeMovie){
            case "seen":
                imageResource =  R.drawable.ic_seen;
                colorResource = R.color.seen;
                break;
            case "watchlist":
                imageResource =  R.drawable.ic_watchlist;
                colorResource = R.color.watchlist;
                break;
            case "favourite":
                imageResource =  R.drawable.ic_favourite;
                colorResource = R.color.favourite;
                break;
            case "blacklist":
                imageResource =  R.drawable.ic_blacklist;
                colorResource = R.color.blacklist;
                break;
            default:
                imageResource = R.drawable.fab_add;
                colorResource = R.color.blacklist;
                break;
        };
        floating_action_menu.getMenuIconView().setImageResource(imageResource);
        floating_action_menu.setMenuButtonColorNormalResId(colorResource);
        floating_action_menu.setMenuButtonColorPressedResId(colorResource);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState = movieActions.saveMainInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("MOVIE", movie);
        savedInstanceState.putString("FROM",from);


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


}
