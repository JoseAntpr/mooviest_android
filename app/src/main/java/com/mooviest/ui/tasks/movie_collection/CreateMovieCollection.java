package com.mooviest.ui.tasks.movie_collection;

import android.os.AsyncTask;

import com.mooviest.ui.models.Collection;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 20/10/16.
 */

public class CreateMovieCollection extends AsyncTask<Integer, String, Collection> {
    MovieCollectionInterface movieCollectionInterface = null;

    public CreateMovieCollection(MovieCollectionInterface movieCollectionInterface){
        this.movieCollectionInterface = movieCollectionInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Collection doInBackground(Integer... params) {
        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

        Call<Collection> call = apiInterface.createMovieCollection(params[0], params[1], params[2]);
        Collection result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Collection result) {
        super.onPostExecute(result);

        if(movieCollectionInterface != null){
            movieCollectionInterface.createMovieCollectionResponse(result);
        }
    }
}
