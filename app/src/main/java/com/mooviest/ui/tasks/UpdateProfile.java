package com.mooviest.ui.tasks;

import android.os.AsyncTask;

import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.rest.UpdateProfileResponse;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 1/10/16.
 */

/*
* AYNCTASK GET USER AND PROFILE
*/
public class UpdateProfile extends AsyncTask<String, String, UpdateProfileResponse> {
    public UpdateProfile(){
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected UpdateProfileResponse doInBackground(String... params) {
        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

        Call<UpdateProfileResponse> call = apiInterface.updateUserProfile(
                SingletonRestClient.getInstance().user.getId(),
                params[0], params[1], params[2], params[3],
                params[4], params[5], params[6], params[7]
        );
        UpdateProfileResponse result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
