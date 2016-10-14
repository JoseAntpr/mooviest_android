package com.mooviest.ui.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.mooviest.R;
import com.mooviest.ui.activities.LoginActivity;
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by jesus on 29/8/16.
 */
public abstract class DataCallback extends AsyncTask<Integer, String, ArrayList<Movie>> implements CallbackReceiver{

    private ProgressDialog mProgressDialog;
    Handler handler;
    Runnable callback;
    private ArrayList<Movie> movies;
    LoginActivity activity;

    public DataCallback(LoginActivity activity) {
        this.activity=activity;
        mProgressDialog = new ProgressDialog(activity, R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setMessage("Loading please wait...");
        mProgressDialog.setIndeterminate(true);
    }

    public abstract void receiveData(Object object);
    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Movie> doInBackground(Integer... params) {
        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;
        Log.i("DOINGBACKGROUND", "INIT");
        //lang, num movies to get
        Call<MooviestApiResult> call = apiInterface.movie_app_bylang(params[0], params[1]);
        /*call.enqueue(new Callback<MooviestApiResult>() {
            @Override
            public void onResponse(Call<MooviestApiResult> call, Response<MooviestApiResult> response) {
                if (response.isSuccessful()) {
                    Log.i("RESTCLIENT", "RESPONSE SUCCESSFUL");
                    MooviestApiResult result =response.body();
                    movies = result.getMovies();

                } else {
                    int statusCode = response.code();
                    Log.i("RESTCLIENT", "STATUS CODE "+ statusCode);
                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    Log.i("RESTCLIENT", "ERROR BODY "+errorBody);
                }
            }

            @Override
            public void onFailure(Call<MooviestApiResult> call, Throwable t) {
                Log.i("RESTCLIENT", "FAILURE");
                Log.d("RESTCLIENT", "onFailure()", t);
            }
        });*/
        try {
            MooviestApiResult result = call.execute().body();
            movies = result.getMovies();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("DOINGBACKGROUND", "FINISH");
        return movies;
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> jsonData) {
        super.onPostExecute(jsonData);
        Log.i("ONPOSTEXECUTE", "INIT");
        if (mProgressDialog != null || mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        if(jsonData!=null)
        {
            receiveData(jsonData);

            //activity.onLoginSuccess();
            Log.i("ONPOSTEXECUTE", "RECEIVEDATA");
        }
        Log.i("ONPOSTEXECUTE", "FINISH");
    }

}
