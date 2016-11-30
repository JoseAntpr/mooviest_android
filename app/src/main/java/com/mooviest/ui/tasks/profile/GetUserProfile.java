package com.mooviest.ui.tasks.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.mooviest.R;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.rest.responses.UserProfileResponse;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 30/10/16.
 */

public class GetUserProfile extends AsyncTask<Integer, String, UserProfileResponse> {
    private ProgressDialog mProgressDialog;
    private UserProfileResponseInterface userProfileResponseInterface = null;

    public GetUserProfile(Context context, UserProfileResponseInterface userProfileResponseInterface){
        this.userProfileResponseInterface = userProfileResponseInterface;
        mProgressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setMessage(context.getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected UserProfileResponse doInBackground(Integer... params) {
        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

        Call<UserProfileResponse> call = apiInterface.getUserProfile(params[0]);
        UserProfileResponse result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(UserProfileResponse result) {
        super.onPostExecute(result);

        if (mProgressDialog != null || mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }

        if(userProfileResponseInterface != null) {
            userProfileResponseInterface.userProfileResponse(result);
        }
    }
}
