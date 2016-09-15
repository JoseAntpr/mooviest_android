package com.mooviest.ui.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.models.MooviestApiResult;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.LoginResponse;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    private EditText emailUsernameText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailUsernameText = (EditText) findViewById(R.id.input_email_username);
        passwordText = (EditText) findViewById(R.id.input_login_password);

        loginButton = (Button) findViewById(R.id.btn_login);
        signupLink = (TextView) findViewById(R.id.link_signup);

        mProgressDialog = null;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SignupActivity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void login() {
        Log.d(TAG, "Login");

        /*if (!validate()) {
            onLoginFailed();
        }else {
        }*/
        loginButton.setEnabled(false);

        String emailUsername = emailUsernameText.getText().toString();
        String password = passwordText.getText().toString();

        // LOGIN API CALL CON ASYNCTASK en onPostExecute
        // hacer llamada para crear la lista de películas para swipe en la BD
        // Una vez terminada esta llamada hacer otra para traer las pelis para
        // movies buffer, terminada esta última llamar a HomeActivity
        new APILogin().execute(emailUsername, password);

        //CON ASYNCTASK y en onPostExecute llamar al intent HomeActivity
        // GET API DATA TO MOVIES_BUFFER, lang, num_movies
        //new GetMoviesBuffer().execute(2,10);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the HomeActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        loginButton.setEnabled(true);
    }

    public boolean validate(String emailUsername, String password) {
        return validEmail(emailUsername) && validPassword(password);
    }

    public boolean validEmail(String emailUsername){
        boolean valid = true;
        if (!emailUsername.isEmpty()) {
            if(emailUsername.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(emailUsername).matches()) {
                emailUsernameText.setError("Enter a valid email address");
                valid = false;
            }
            if(emailUsername.length() < 3){
                emailUsernameText.setError("Username must be at least 3 characters");
                valid = false;
            }
        }else {
            emailUsernameText.setError(null);
        }

        return valid;
    }

    public boolean validPassword(String password){
        boolean valid = true;
        if (password.isEmpty() || password.length() < 6) {
            passwordText.setError("Password must be at least 6 characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    /************************  ASYNCTASKS  ************************/


    /*
     * AYNCTASK USER LOGIN
     */
    public class APILogin extends AsyncTask<String, String, LoginResponse>{

        public APILogin(){
            mProgressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
            mProgressDialog.setMessage("Loading please wait...");
            mProgressDialog.setIndeterminate(true);
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected LoginResponse doInBackground(String... params) {
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

            if(result!=null) {
                int statusCode = result.getStatus();

                //onLoginSuccess();
                if(statusCode == 200) {
                    new GetMoviesBuffer().execute(2,10);

                }else if (statusCode == 404){
                    onLoginFailed(result.getMessage());
                }

            }else{
                onLoginFailed("Login failed. Check your internet connection.");
            }
        }
    }


    /*
     * ASYNCTASK FOR GET MOVIES TO BUFFER
     */
    public class GetMoviesBuffer extends AsyncTask<Integer, String, ArrayList<Movie>>{


        private ArrayList<Movie> movies;

        public GetMoviesBuffer(){
            movies = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Integer... params) {
            MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

            //lang, num movies to get
            Call<MooviestApiResult> call = apiInterface.movie_app_bylang(params[0], params[1]);

            try {
                MooviestApiResult result = call.execute().body();
                movies = result.getMovies();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            super.onPostExecute(result);

            if (mProgressDialog != null || mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            if(result!=null) {

                SingletonRestClient.getInstance().movies_buffer = result;
                //onLoginSuccess();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);

                // close this activity
                finish();

            }else{
                onLoginFailed("Login failed");
            }
        }
    }

}

