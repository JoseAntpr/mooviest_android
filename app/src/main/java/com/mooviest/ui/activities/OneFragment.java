package com.mooviest.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.mooviest.R;
import com.mooviest.ui.adapters.SwipeDeckAdapter;
import com.mooviest.ui.models.Collection;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.CreateMovieCollection;
import com.mooviest.ui.tasks.GetMovieDetail;
import com.mooviest.ui.tasks.GetSwipeList;
import com.mooviest.ui.tasks.MovieCollectionInterface;
import com.mooviest.ui.tasks.SwipeMovieInterface;
import com.mooviest.ui.tasks.UpdateMovieCollection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cardstack.SwipeDeck;


public class OneFragment extends Fragment implements MovieCollectionInterface, SwipeMovieInterface {

    private SwipeDeck cardStack;
    private SwipeDeckAdapter adapter;
    private ArrayList<Movie> movies_buffer;
    private ArrayList<Movie> movies_swipe;
    private FloatingActionButton fab_seen;
    private FloatingActionButton fab_watchlist;
    private FloatingActionButton fab_favourite;
    private FloatingActionButton fab_blacklist;

    Bundle savedInstanceState;
    Movie movieSelectedUpdate;
    private Resources resources;
    private int id_image;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Log.i("OneFragment", "restoreInstanceState");
            // Restore value of members from saved state
            movies_buffer = savedInstanceState.getParcelableArrayList("MOVIES_BUFFER");
            movies_swipe = savedInstanceState.getParcelableArrayList("MOVIES_SWIPE");
        }else {
            Log.i("OneFragment", "onCreate");
            //INIT movies_swipe
            movies_swipe = new ArrayList<>();

            //INIT movies_buffer
            movies_buffer = SingletonRestClient.getInstance().movies_buffer;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.savedInstanceState=savedInstanceState;
        Log.i("OneFragment", "onCreateView");

        this.resources=getContext().getResources();
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_one, container, false);

        //**** CARDSTACK ******
        cardStack = (SwipeDeck) v.findViewById(R.id.swipe_deck);
        cardStack.setHardwareAccelerationEnabled(true);

        Log.i("OneFragment", "Movies_size: "+ movies_swipe.size());


        adapter = new SwipeDeckAdapter(movies_swipe, getContext(), this);
        cardStack.setAdapter(adapter);

        if(movies_swipe.isEmpty() && movies_buffer.size()>=2){
            Log.i("OneFragment", "movies_swipe empty, add two movies");
            addMoviesToSwipe(2);
        }

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                movieCollectionTask("watchlist", adapter.getItem(0));
                checkMoviesSwipe();
                removeAndAddMovieToAdapter(0, 1);
                Log.i("HomeActivity", "card was swiped left, position in adapter: " + position);
            }

            @Override
            public void cardSwipedRight(int position) {
                movieCollectionTask("seen", adapter.getItem(0));
                checkMoviesSwipe();
                removeAndAddMovieToAdapter(0, 1);
                Log.i("HomeActivity", "card was swiped right, position in adapter: " + position);
            }

            @Override
            public void cardSwipedUp(int position) {
                movieCollectionTask("favourite", adapter.getItem(0));
                checkMoviesSwipe();
                removeAndAddMovieToAdapter(0, 1);
                Log.i("HomeActivity", "card was swiped Up, position in adapter: " + position);
            }

            @Override
            public void cardSwipedDown(int position) {
                movieCollectionTask("blacklist", adapter.getItem(0));
                checkMoviesSwipe();
                removeAndAddMovieToAdapter(0, 1);
                Log.i("HomeActivity", "card was swiped down, position in adapter: " + position);
            }

            @Override
            public void cardRemove(int position) {
                checkMoviesSwipe();
                removeAndAddMovieToAdapter(0, 1);
            }

            @Override
            public void cardsDepleted() {
                Log.i("HomeActivity", "no more cards");
            }

            @Override
            public void cardActionDown() {
                Log.i("HomeActivity", "cardActionDown");
            }

            @Override
            public void cardActionUp() {
                Log.i("HomeActivity", "cardActionUp");
            }

        });

        cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);
        cardStack.setUpImage(R.id.up_image);
        cardStack.setDownImage(R.id.down_image);

        fab_watchlist = (FloatingActionButton) v.findViewById(R.id.fab_watchlist);
        fab_watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getCount() > 1) {
                    cardStack.swipeTopCardLeft(180);

                }

            }
        });
        fab_seen = (FloatingActionButton) v.findViewById(R.id.fab_seen);
        fab_seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getCount() > 1) {
                    cardStack.swipeTopCardRight(260);
                }
            }
        });
        fab_favourite = (FloatingActionButton) v.findViewById(R.id.fab_favourite);
        fab_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getCount() > 1) {
                    cardStack.swipeTopCardUp(150);
                }
            }
        });

        fab_blacklist = (FloatingActionButton) v.findViewById(R.id.fab_blacklist);
        fab_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getCount() > 1) {
                    cardStack.swipeTopCardDown(180);
                }
            }
        });


        return v;


    }

    private void addMoviesToSwipe(int num){
        for(int i=0; i < num; i++){
            adapter.addItem(movies_buffer.get(0));
            movies_buffer.remove(0);
        }
    }

    public void checkMoviesSwipe(){
        if(movies_buffer.size() <= 6 ) {
            SharedPreferences user_prefs = this.getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

            // GET SWIPE LIST
            GetSwipeList getSwipeList = new GetSwipeList() {
                @Override
                protected void onPostExecute(MooviestApiResult mooviestApiResult) {
                    super.onPostExecute(mooviestApiResult);
                    movies_buffer.addAll(movies_buffer.size(), mooviestApiResult.getMovies());
                }
            };
            getSwipeList.execute(user_prefs.getInt("id", 0));
        }
    }

    public void removeAndAddMovieToAdapter(int item_pos, int num_movies){
        adapter.removeItem(item_pos);
        if(movies_buffer.size() >= 1) {
            addMoviesToSwipe(num_movies);
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

    private void movieCollectionTask(String typeMovie, Movie movie){
        Collection collection = movie.getCollection();
        movieSelectedUpdate = movie;

        if(collection != null) {
            //La película ya se encontraba en una lista y se actualiza a la nueva
            UpdateMovieCollection updateMovieCollection = new UpdateMovieCollection(this);
            updateMovieCollection.execute(collection.getId(), getTypeMovieId(typeMovie));
        }else{
            // La película no se encuentraba en ninguna lista y se introduce en la nueva
            CreateMovieCollection createMovieCollection = new CreateMovieCollection(this);

            SharedPreferences user_prefs = this.getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

            createMovieCollection.execute(user_prefs.getInt("id", 0), movie.getId(), getTypeMovieId(typeMovie));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i("OneFragment", "onSaveInstanceState");
        // Save the user's current game state
        Log.i("OneFragment", "Movies_size: "+ movies_swipe.size());
        savedInstanceState.putParcelableArrayList("MOVIES_BUFFER", movies_buffer);
        savedInstanceState.putParcelableArrayList("MOVIES_SWIPE", movies_swipe);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void updateMovieCollectionResponse(Collection result) {
        if(result != null){
            Collection collection = movieSelectedUpdate.getCollection();

            // Eliminamos la película seleccionada de la lista en la que se encontraba o recargamos toda la lista si había 10 películas
            // Ésto lo hacemos por si en esa lista tiene más de 10 en la BD, seguirá habiendo 10 en la lista de previsualizaciones
            MovieActions movieActions = new MovieActions();
            movieActions.deleteMovieFromList(collection.getTypeMovie(), movieSelectedUpdate);

            movieSelectedUpdate.setCollection(result);

            // Después la añadimos a la lista seleccionada
            movieActions.addMovieToList(result.getTypeMovie(), movieSelectedUpdate);

        }
    }

    @Override
    public void createMovieCollectionResponse(Collection result) {
        if(result != null) {

            // La añadimos a la lista seleccionada
            MovieActions movieActions = new MovieActions();
            movieActions.addMovieToList(result.getTypeMovie(), movieSelectedUpdate);

        }
    }


    @Override
    public void movieClicked() {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra("FROM","swipe");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(!data.getStringExtra("typeMovie").equals("")){
            cardStack.swipeRemove(0);
        }
    }
}
