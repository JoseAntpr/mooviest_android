package com.mooviest.ui.tasks;

import android.os.AsyncTask;

import com.mooviest.ui.models.Collection;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 16/10/16.
 */

public class UpdateMovieCollection extends AsyncTask<Integer, String, Collection> {

    MovieCollectionInterface movieCollectionInterface = null;

    public UpdateMovieCollection(MovieCollectionInterface receiver){
        movieCollectionInterface = receiver;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Collection doInBackground(Integer... params) {
        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

        Call<Collection> call = apiInterface.updateMovieCollection(params[0], params[1]);
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
            movieCollectionInterface.updateMovieCollectionResponse(result);
        }
    }
}
