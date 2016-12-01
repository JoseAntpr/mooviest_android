package com.mooviest.ui.tasks.movie_collection;

import android.os.AsyncTask;

import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.responses.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.InitialValuesInterface;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 8/10/16.
 */

public class GetUserMoviesList extends AsyncTask<Integer, String, MooviestApiResult> {

    private String list_name;
    private InitialValuesInterface initialValuesInterface = null;

    public GetUserMoviesList(String name){
        this.list_name = name;
    }

    public GetUserMoviesList(String name, InitialValuesInterface initialValuesInterface){
        this.list_name = name;
        this.initialValuesInterface = initialValuesInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MooviestApiResult doInBackground(Integer... params) {
        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

        Call<MooviestApiResult> call = apiInterface.getUserMoviesList(
                SingletonRestClient.getInstance().user.getId(),
                list_name,
                params[0]
        );

        MooviestApiResult result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(MooviestApiResult mooviestApiResult) {
        super.onPostExecute(mooviestApiResult);

        if(initialValuesInterface != null){
            initialValuesInterface.listsResponse(list_name, mooviestApiResult);
        }
    }
}
