package com.mooviest.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.mooviest.ui.adapters.MoviesListAdapter;
import com.mooviest.ui.adapters.MoviesUserListAdapter;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.responses.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.movie_collection.GetUserMoviesList;

/**
 * Created by jesus on 25/10/16.
 */

public class MovieActions {

    public void addMovieToList(String typeMovie, Movie movie){
        // Si hay 10 películas no se introduce en la lista
        // Se comprueba en el método addItem de MoviesListAdatper
        switch (typeMovie){
            case "seen":
                SingletonRestClient.getInstance().seenListAdapter.addItem(movie);
                SingletonRestClient.getInstance().seenListAdapter.notifyDataSetChanged();
                break;
            case "watchlist":
                SingletonRestClient.getInstance().watchlistAdapter.addItem(movie);
                SingletonRestClient.getInstance().watchlistAdapter.notifyDataSetChanged();
                break;
            case "favourite":
                SingletonRestClient.getInstance().favouriteListAdapter.addItem(movie);
                SingletonRestClient.getInstance().favouriteListAdapter.notifyDataSetChanged();
                break;
            case "blacklist":
                SingletonRestClient.getInstance().blacklistAdapter.addItem(movie);
                SingletonRestClient.getInstance().blacklistAdapter.notifyDataSetChanged();
                break;
        };
    }


    public void deleteMovieFromList(String typeMovie, Movie movie){
        switch (typeMovie){
            case "seen":
                if(SingletonRestClient.getInstance().seenListAdapter.getItemCount() == 18){
                    GetUserMoviesList getSeenList = new GetUserMoviesList("seen"){
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
                    SingletonRestClient.getInstance().seenListAdapter.removeItem(movie);
                    SingletonRestClient.getInstance().seenListAdapter.notifyDataSetChanged();
                }
                break;
            case "watchlist":
                if(SingletonRestClient.getInstance().watchlistAdapter.getItemCount() == 18){
                    GetUserMoviesList getSeenList = new GetUserMoviesList("watchlist"){
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
                    SingletonRestClient.getInstance().watchlistAdapter.removeItem(movie);
                    SingletonRestClient.getInstance().watchlistAdapter.notifyDataSetChanged();
                }
                break;
            case "favourite":
                if(SingletonRestClient.getInstance().favouriteListAdapter.getItemCount() == 18){
                    GetUserMoviesList getSeenList = new GetUserMoviesList("favourite"){
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
                    SingletonRestClient.getInstance().favouriteListAdapter.removeItem(movie);
                    SingletonRestClient.getInstance().favouriteListAdapter.notifyDataSetChanged();
                }
                break;
            case "blacklist":
                if(SingletonRestClient.getInstance().blacklistAdapter.getItemCount() == 18){
                    GetUserMoviesList getSeenList = new GetUserMoviesList("blacklist"){
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
                    SingletonRestClient.getInstance().blacklistAdapter.removeItem(movie);
                    SingletonRestClient.getInstance().blacklistAdapter.notifyDataSetChanged();
                }
                break;
        };

    }

    public void addDeleteFromAdapter(String fromTypeMovie, String newtypeMovie, Movie movie) {
        if(fromTypeMovie.equals("seen") || fromTypeMovie.equals("watchlist") || fromTypeMovie.equals("favourite") || fromTypeMovie.equals("blacklist")){
            if(fromTypeMovie.equals(newtypeMovie)) {
                SingletonRestClient.getInstance().moviesListAdapter.addItem(movie);
                SingletonRestClient.getInstance().moviesListAdapter.notifyDataSetChanged();
            }else{
                SingletonRestClient.getInstance().moviesListAdapter.removeItem(movie);
                SingletonRestClient.getInstance().moviesListAdapter.notifyDataSetChanged();
            }

        }
    }

    public Bundle saveMainInstanceState(Bundle savedInstanceState){

        savedInstanceState.putParcelable("USER", SingletonRestClient.getInstance().user);

        savedInstanceState.putParcelableArrayList("MOVIES_BUFFER", SingletonRestClient.getInstance().movies_buffer);
        savedInstanceState.putParcelableArrayList("MOVIES_SWIPE", SingletonRestClient.getInstance().movies_swipe);

        savedInstanceState.putParcelableArrayList("MOVIES_SEEN", SingletonRestClient.getInstance().seenListAdapter.getItems());
        savedInstanceState.putParcelableArrayList("MOVIES_WATCHLIST", SingletonRestClient.getInstance().watchlistAdapter.getItems());
        savedInstanceState.putParcelableArrayList("MOVIES_FAVOURITE", SingletonRestClient.getInstance().favouriteListAdapter.getItems());
        savedInstanceState.putParcelableArrayList("MOVIES_BLACKLIST", SingletonRestClient.getInstance().blacklistAdapter.getItems());
        if(SingletonRestClient.getInstance().moviesListAdapter != null) {
            savedInstanceState.putParcelableArrayList("MOVIES_LIST", SingletonRestClient.getInstance().moviesListAdapter.getItems());
        }
        return savedInstanceState;
    }

    public void restoreMainInstanceState(Bundle savedInstanceState, SharedPreferences user_prefs){
        if(SingletonRestClient.getInstance().mooviestApiInterface == null) {
            SingletonRestClient.getInstance().setNewToken(user_prefs.getString("token", ""));
        }

        if(SingletonRestClient.getInstance().user == null) {
            SingletonRestClient.getInstance().user = savedInstanceState.getParcelable("USER");
        }

        if(SingletonRestClient.getInstance().movies_buffer == null) {
            SingletonRestClient.getInstance().movies_buffer = savedInstanceState.getParcelableArrayList("MOVIES_BUFFER");
        }

        if(SingletonRestClient.getInstance().movies_swipe == null) {
            SingletonRestClient.getInstance().movies_swipe = savedInstanceState.getParcelableArrayList("MOVIES_SWIPE");
        }

        if(SingletonRestClient.getInstance().seenListAdapter == null) {
            SingletonRestClient.getInstance().seen_list = savedInstanceState.getParcelableArrayList("MOVIES_SEEN");
            SingletonRestClient.getInstance().seenListAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().seen_list);
        }else{
            if(SingletonRestClient.getInstance().seen_list == null) {
                SingletonRestClient.getInstance().seen_list = SingletonRestClient.getInstance().seenListAdapter.getItems();
            }
        }

        if(SingletonRestClient.getInstance().watchlistAdapter == null) {
            SingletonRestClient.getInstance().watchlist = savedInstanceState.getParcelableArrayList("MOVIES_WATCHLIST");
            SingletonRestClient.getInstance().watchlistAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().watchlist);
        }else{
            if(SingletonRestClient.getInstance().watchlist == null) {
                SingletonRestClient.getInstance().watchlist = SingletonRestClient.getInstance().watchlistAdapter.getItems();
            }
        }

        if(SingletonRestClient.getInstance().favouriteListAdapter == null) {
            SingletonRestClient.getInstance().favourite_list = savedInstanceState.getParcelableArrayList("MOVIES_FAVOURITE");
            SingletonRestClient.getInstance().favouriteListAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().favourite_list);
        }else {
            if(SingletonRestClient.getInstance().favourite_list == null) {
                SingletonRestClient.getInstance().favourite_list = SingletonRestClient.getInstance().favouriteListAdapter.getItems();
            }
        }

        if(SingletonRestClient.getInstance().blacklistAdapter == null) {
            SingletonRestClient.getInstance().blacklist = savedInstanceState.getParcelableArrayList("MOVIES_BLACKLIST");
            SingletonRestClient.getInstance().blacklistAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().blacklist);
        }else{
            if(SingletonRestClient.getInstance().blacklist == null) {
                SingletonRestClient.getInstance().blacklist = SingletonRestClient.getInstance().blacklistAdapter.getItems();
            }
        }

        if(SingletonRestClient.getInstance().moviesListAdapter == null){
            if(savedInstanceState.getParcelableArrayList("MOVIES_LIST") != null) {
                SingletonRestClient.getInstance().movies_list = savedInstanceState.getParcelableArrayList("MOVIES_LIST");
                SingletonRestClient.getInstance().moviesListAdapter = new MoviesListAdapter(SingletonRestClient.getInstance().movies_list);
            }
        }else{
            if(SingletonRestClient.getInstance().movies_list == null){
                SingletonRestClient.getInstance().movies_list = SingletonRestClient.getInstance().moviesListAdapter.getItems();
            }
        }

    }
}
