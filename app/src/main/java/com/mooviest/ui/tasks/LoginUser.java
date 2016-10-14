package com.mooviest.ui.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.mooviest.R;
import com.mooviest.ui.rest.LoginResponse;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by jesus on 14/10/16.
 */

public class LoginUser extends AsyncTask<String, String, LoginResponse> {

    ProgressDialog progressDialog;
    private LoginResponseInterface loginResponseInterface = null;

    public LoginUser(Context context, LoginResponseInterface receiver){
        loginResponseInterface = receiver;
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setIndeterminate(true);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected LoginResponse doInBackground(String... params) {
        //Inicialización del Cliente Rest
        SingletonRestClient.getInstance().init();

        MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;
        Call<LoginResponse> call = apiInterface.login(params[0], params[1]);
        LoginResponse result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(LoginResponse result) {
        super.onPostExecute(result);

        if (progressDialog != null || progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        if(loginResponseInterface != null) {
            loginResponseInterface.loginResponse(result);
        }
    }
}
