package com.mooviest.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.models.MooviestApiResult;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.Errors;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SignupResponse;
import com.mooviest.ui.rest.SingletonRestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";


    private TextInputLayout tilUsername;
    private TextInputLayout tilEmail;
    private TextInputLayout tilSignupPassword;
    private TextInputLayout tilConfirmPassword;
    private boolean textWatcher = false;
    private Button signupButton;
    private TextView loginLink;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tilUsername = (TextInputLayout) findViewById(R.id.til_username);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilSignupPassword = (TextInputLayout) findViewById(R.id.til_signup_password);
        tilConfirmPassword = (TextInputLayout) findViewById(R.id.til_confirm_password);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);

        mProgressDialog = null;

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the LoginActivity
                finish();
            }
        });



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void signup() {
        Log.d(TAG, "Signup");
        if(!textWatcher){
            tilUsername.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validUsername(String.valueOf(s));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            tilEmail.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validEmail(String.valueOf(s));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            tilSignupPassword.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tilSignupPassword.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            tilConfirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validPassword(tilSignupPassword.getEditText().getText().toString(), String.valueOf(s));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            textWatcher = true;
        }

        String username = tilUsername.getEditText().getText().toString();
        String email = tilEmail.getEditText().getText().toString();
        String password = tilSignupPassword.getEditText().getText().toString();
        String confirmPassword = tilConfirmPassword.getEditText().getText().toString();

        if (!validate(username, email, password, confirmPassword)) {

            Toast.makeText(getBaseContext(), "Sign up failed, check fields errors", Toast.LENGTH_LONG).show();
        }else {

            signupButton.setEnabled(false);

            //CON ASYNCTASK y en onPostExecute llamar al intent HomeActivity
            //REGISTRAR Y LOGEAR en doInBackground
            new APISignup().execute(username, email, password, Locale.getDefault().getLanguage());
        }
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the HomeActivity
        moveTaskToBack(true);
    }

    public void onSignupFailed(Errors errors) {
        String message = "";
        if(errors.getEmail().size() != 0){
            message += errors.getEmail().get(0);
        }
        if(errors.getUsername().size() != 0){
            message += errors.getUsername().get(0);
        }
        if(errors.getPassword().size() != 0){
            message += errors.getPassword().get(0);
        }
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        signupButton.setEnabled(true);
    }

    public boolean validate(String username, String email, String password, String confirmPassword) {
        boolean vu, ve, vp;
        vu = validUsername(username);
        ve = validEmail(email);
        vp = validPassword(password, confirmPassword);

        return vu && ve && vp;
    }

    public boolean validUsername(String username){
        boolean valid = true;
        if (username.isEmpty() || username.length() < 3) {
            tilUsername.setError("Username must be at least 3 characters");
            valid = false;
        } else {
            tilUsername.setError(null);
        }

        return valid;
    }

    public boolean validEmail(String email){
        boolean valid = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            tilEmail.setError(null);
        }

        return valid;
    }

    public boolean validPassword(String password, String confirmPassword){
        boolean valid = true;
        if (password.isEmpty() || password.length() < 6) {
            tilSignupPassword.setError("Password must be at least 6 alphanumeric characters");
            valid = false;
        } else {
            tilSignupPassword.setError(null);
            if(password.equals(confirmPassword)){
                tilConfirmPassword.setError(null);
            }else {
                valid = false;
                tilConfirmPassword.setError("Passwords do not match");
            }
        }

        return valid;
    }

    /************************  ASYNCTASKS  ************************/

    /*
     * ASYNCTASK USER SIGN UP
     */
    public class APISignup extends AsyncTask<String, String, SignupResponse>{

        public APISignup(){
            mProgressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
            mProgressDialog.setMessage("Loading please wait...");
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

            if(result !=null){
                int statusCode = result.getStatus();

                if(statusCode == 201) {
                    //onSignupSuccess(result)
                    //en ese metodo guardar los sharedpreferences e inicializar el cliente rest con el nuevo token
                    new GetMoviesBuffer().execute(2,10);

                }else if (statusCode == 404){
                    onSignupFailed(result.getErrors());
                }

            }else{
                Toast.makeText(getBaseContext(), "Register failed. Check your internet connection.", Toast.LENGTH_LONG).show();

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

                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                startActivity(intent);

                onSignupSuccess();

            }else{
                Toast.makeText(getBaseContext(), "Load movies failed. Check your internet connection.", Toast.LENGTH_LONG).show();
            }
        }
    }


}