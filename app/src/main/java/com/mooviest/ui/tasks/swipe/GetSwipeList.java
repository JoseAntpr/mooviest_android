package com.mooviest.ui.tasks.swipe;

import android.os.AsyncTask;

import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.responses.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.InitialValuesInterface;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 25/10/16.
 */

public class GetSwipeList extends AsyncTask<Integer, String, MooviestApiResult>{

    private InitialValuesInterface initialValuesInterface = null;

    public GetSwipeList(InitialValuesInterface initialValuesInterface){
        this.initialValuesInterface = initialValuesInterface;
    }

    public GetSwipeList(){

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MooviestApiResult doInBackground(Integer... params) {
        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

        Call<MooviestApiResult> call = apiInterface.getSwipeList(
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
            initialValuesInterface.swipeResponse(mooviestApiResult);
        }
    }
}
