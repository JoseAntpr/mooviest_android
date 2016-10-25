package com.mooviest.ui.activities;

import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.GetUserList;

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
        if(SingletonRestClient.getInstance().moviesListAdapter != null){
            SingletonRestClient.getInstance().moviesListAdapter.removeItem(movie);
            SingletonRestClient.getInstance().moviesListAdapter.notifyDataSetChanged();
        }
        switch (typeMovie){
            case "seen":
                if(SingletonRestClient.getInstance().seenListAdapter.getItemCount() == 10){
                    GetUserList getSeenList = new GetUserList("seen"){
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
                    SingletonRestClient.getInstance().watchlistAdapter.removeItem(movie);
                    SingletonRestClient.getInstance().watchlistAdapter.notifyDataSetChanged();
                }
                break;
            case "favourite":
                if(SingletonRestClient.getInstance().favouriteListAdapter.getItemCount() == 10){
                    GetUserList getSeenList = new GetUserList("favourite"){
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
                    SingletonRestClient.getInstance().blacklistAdapter.removeItem(movie);
                    SingletonRestClient.getInstance().blacklistAdapter.notifyDataSetChanged();
                }
                break;
        };

    }
}
