package com.mooviest.ui.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.models.MooviestApiResult;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.DataCallback;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);

        loginButton = (Button) findViewById(R.id.btn_login);
        signupLink = (TextView) findViewById(R.id.link_signup);

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

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // LOGIN API CALL CON ASYNCTASK en onPostExecute
        // hacer llamada para crear la lista de películas para swipe en la BD
        // Una vez terminada esta llamada hacer otra para traer las pelis para
        // movies buffer, terminada esta última llamar a HomeActivity

        //CON ASYNCTASK y en onPostExecute llamar al intent HomeActivity
        // GET API DATA TO MOVIES_BUFFER, lang, num_movies
        new GetMoviesBuffer().execute(2,10);

        /*DataCallback callbackservice = new DataCallback(LoginActivity.this) {
            @Override
            public void receiveData(Object object) {
                SingletonRestClient.getInstance().movies_buffer = (ArrayList<Movie>) object;
            }
        };
        callbackservice.execute(1, 10);*/



        /*final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/

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

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    /*
     * ASYNCTASK FOR GET MOVIES TO BUFFER
     */
    public class GetMoviesBuffer extends AsyncTask<Integer, String, ArrayList<Movie>>{

        private ProgressDialog mProgressDialog;
        private ArrayList<Movie> movies;

        public GetMoviesBuffer(){
            movies = null;
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
        protected ArrayList<Movie> doInBackground(Integer... params) {
            MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;
            Log.i("DOINGBACKGROUND", "INIT");
            //lang, num movies to get
            Call<MooviestApiResult> call = apiInterface.movie_app_bylang(params[0], params[1]);

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
        protected void onPostExecute(ArrayList<Movie> result) {
            super.onPostExecute(result);
            Log.i("ONPOSTEXECUTE", "INIT");
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
                Log.i("ONPOSTEXECUTE", "RECEIVEDATA");
            }else{
                onLoginFailed();
            }
            Log.i("ONPOSTEXECUTE", "FINISH");
        }
    }

}

