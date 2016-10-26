package com.mooviest.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.LoginResponse;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.GetUserList;
import com.mooviest.ui.tasks.LoginUser;
import com.mooviest.ui.tasks.LoginResponseInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;


public class LoginActivity extends AppCompatActivity implements LoginResponseInterface {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    private EditText emailUsernameText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;
    private static int countGetLists;
    private static final Object countGetListsLock = new Object();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailUsernameText = (EditText) findViewById(R.id.input_email_username);
        passwordText = (EditText) findViewById(R.id.input_login_password);

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

        String emailUsername = emailUsernameText.getText().toString().toLowerCase();
        String password = passwordText.getText().toString();

        // LOGIN API CALL CON ASYNCTASK en onPostExecute
        // hacer llamada para crear la lista de películas para swipe en la BD
        // Una vez terminada esta llamada hacer otra para traer las pelis para
        // movies buffer, terminada esta última llamar a HomeActivity
        LoginUser loginUser = new LoginUser(this, LoginActivity.this){

        };
        loginUser.execute(emailUsername, password);

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

    public void onLoginSuccess(LoginResponse result) {
        //loginButton.setEnabled(true);
        //finish();

        SharedPreferences user_prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user_prefs.edit();

        SingletonRestClient.getInstance().user = result.getUser();
        User user = result.getUser();
        String avatar = user.getProfile().getAvatar();

        if(avatar.contains("http")){
            editor.putBoolean("default_avatar", false);
            editor.putString("avatar_image", avatar);
        }

        editor.putInt("id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.putString("token", result.getToken());

        editor.commit();

        // Actualización del cliente para usar el nuevo token
        SingletonRestClient.getInstance().setNewToken(result.getToken());
    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

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

    public void getMoviesBuffer(){
        GetMoviesBuffer getMoviesBuffer = new GetMoviesBuffer(){
            @Override
            protected void onPostExecute(ArrayList<Movie> result) {
                super.onPostExecute(result);

                if(result!=null) {

                    // Counter response user lists
                    countGetLists = 0;

                    /*SingletonRestClient.getInstance().movies_buffer = result;
                    GetUserList getSeenList = new GetUserList("seen", LoginActivity.this);
                    getSeenList.execute(1);

                    GetUserList getWatchlist = new GetUserList("watchlist", LoginActivity.this);
                    getWatchlist.execute(1);

                    GetUserList getFavouriteList = new GetUserList("favourite", LoginActivity.this);
                    getFavouriteList.execute(1);

                    GetUserList getBlacklist = new GetUserList("blacklist", LoginActivity.this);
                    getBlacklist.execute(1);*/


                }else{
                    onLoginFailed("Load movies failed. Check your internet connection.");
                }
            }
        };
        getMoviesBuffer.execute(2,10);
    }

    @Override
    public void loginResponse(LoginResponse result) {
        if(result!=null) {
            int statusCode = result.getStatus();


            if(statusCode == 200) {
                onLoginSuccess(result);

                //getMoviesBuffer();
                GetInitialValues getInitialValues = new GetInitialValues(this, this);
                getInitialValues.getValues();

            }else if (statusCode == 404){
                onLoginFailed(result.getMessage());
            }

        }else{
            onLoginFailed("Login failed. Check your internet connection.");
        }
    }

    @Override
    public void listsResponse(String list_name, MooviestApiResult result) {
        switch (list_name) {
            case "seen":
                if(result != null) {
                    SingletonRestClient.getInstance().seen_list = result.getMovies();
                }else{
                    SingletonRestClient.getInstance().seen_list = new ArrayList<Movie>();
                }
                break;
            case "watchlist":
                if(result != null) {
                    SingletonRestClient.getInstance().watchlist = result.getMovies();
                }else{
                    SingletonRestClient.getInstance().watchlist = new ArrayList<Movie>();
                }
                break;
            case "favourite":
                if(result != null) {
                    SingletonRestClient.getInstance().favourite_list = result.getMovies();
                }else{
                    SingletonRestClient.getInstance().favourite_list = new ArrayList<Movie>();
                }
                break;
            case "blacklist":
                if(result != null) {
                    SingletonRestClient.getInstance().blacklist = result.getMovies();
                }else{
                    SingletonRestClient.getInstance().blacklist = new ArrayList<Movie>();
                }
                break;
        };

        incrementCountLists();

        if(getCountLists() == 4){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);

            // close this activity
            finish();
        }
    }

    public void incrementCountLists() {
        synchronized (countGetListsLock) {
            countGetLists++;
        }
    }


    public int getCountLists(){
        synchronized (countGetListsLock){
            return countGetLists;
        }
    }



    /************************  ASYNCTASKS  ************************/

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
    }

}

