package com.mooviest.ui.tasks;

import android.os.AsyncTask;

import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 14/10/16.
 */

public class GetMovieDetail extends AsyncTask<Integer, String, Movie> {



    public GetMovieDetail(){

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Movie doInBackground(Integer... params) {
        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

        Call<Movie> call = apiInterface.getMovieDetail(
                params[0],
                params[1],
                params[2]
        );

        Movie result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}