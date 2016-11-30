package com.mooviest.ui.tasks.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.mooviest.R;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.responses.SignupResponse;
import com.mooviest.ui.rest.SingletonRestClient;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 27/10/16.
 */

public class RegisterUser extends AsyncTask<String, String, SignupResponse> {
    ProgressDialog mProgressDialog;
    Context context;
    private RegisterResponseInterface registerResponseInterface = null;

    public RegisterUser(Context context, RegisterResponseInterface registerResponseInterface){
        this.context = context;
        this.registerResponseInterface = registerResponseInterface;
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
    protected SignupResponse doInBackground(String... params) {
        //Inicialización del Cliente Rest
        SingletonRestClient.getInstance().init();

        MooviestApiInterface apiInterface = SingletonRestClient.getInstance().mooviestApiInterface;

        Call<SignupResponse> call = apiInterface.signup(params[0], params[1], params[2], params[3]);
        SignupResponse result = null;

        try{
            result = call.execute().body();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(SignupResponse result){
        super.onPostExecute(result);
        if (mProgressDialog != null || mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }

        if(registerResponseInterface != null) {
            registerResponseInterface.registerResponse(result);
        }
    }
}
