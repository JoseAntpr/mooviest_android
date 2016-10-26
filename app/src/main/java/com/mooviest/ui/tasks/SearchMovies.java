package com.mooviest.ui.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.mooviest.R;
import com.mooviest.ui.activities.SearchableActivity;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 19/10/16.
 */

public class SearchMovies extends AsyncTask<Integer, String, MooviestApiResult> {

    private String query;
    private String lang_code;
    private SearchMoviesInterface searchMoviesInterface = null;
    private int page;
    //private ProgressDialog progressDialog = null;
    private ProgressBar progressBar =null;

    public SearchMovies(SearchableActivity searchableActivity, String query, String lang_code, int page){
        this.query = query;
        this.lang_code = lang_code;
        this.searchMoviesInterface = searchableActivity;
        this.page = page;
        if (page == 1) {
            progressBar = (ProgressBar) searchableActivity.findViewById(R.id.progressBar);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected MooviestApiResult doInBackground(Integer... integers) {
        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

        Call<MooviestApiResult> call = apiInterface.searchMovies(query, lang_code, page);

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

        if(progressBar != null){
            progressBar.setVisibility(View.GONE);
        }

        if(searchMoviesInterface != null) {
            searchMoviesInterface.searchResponse(mooviestApiResult);
        }
    }
}
