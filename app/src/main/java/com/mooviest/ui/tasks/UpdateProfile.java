package com.mooviest.ui.tasks;

import android.os.AsyncTask;

import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.rest.UpdateProfileResponse;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by jesus on 1/10/16.
 */

/*
* AYNCTASK GET USER AND PROFILE
*/
public class UpdateProfile extends AsyncTask<String, String, UpdateProfileResponse> {
    private MultipartBody.Part image;
    MediaType TYPE_TEXT = MediaType.parse("text/plain");

    public UpdateProfile(MultipartBody.Part i){
        this.image = i;
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
                RequestBody.create(TYPE_TEXT, params[0]), RequestBody.create(TYPE_TEXT, params[1]),
                RequestBody.create(TYPE_TEXT, params[2]), RequestBody.create(TYPE_TEXT, params[3]),
                RequestBody.create(TYPE_TEXT, params[4]), RequestBody.create(TYPE_TEXT, params[5]),
                RequestBody.create(TYPE_TEXT, params[6]), RequestBody.create(TYPE_TEXT, params[7]), image
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
