package com.mooviest.ui.activities.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.activities.GetInitialValues;
import com.mooviest.ui.activities.signup.SignupActivity;
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.responses.LoginResponse;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.login.LoginUser;
import com.mooviest.ui.tasks.login.LoginResponseInterface;


public class LoginActivity extends AppCompatActivity implements LoginResponseInterface {
    private static final int REQUEST_SIGNUP = 0;


    private EditText emailUsernameText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;

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

        /*if (!validate()) {
            onLoginFailed();
        }else {
        }*/
        loginButton.setEnabled(false);

        String emailUsername = emailUsernameText.getText().toString().toLowerCase();
        String password = passwordText.getText().toString();

        /*
         * Login usuario
         */
        LoginUser loginUser = new LoginUser(this, LoginActivity.this);
        loginUser.execute(emailUsername, password);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

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
        SharedPreferences app_prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorAppPrefs = app_prefs.edit();
        editorAppPrefs.putBoolean("logged", true);
        editorAppPrefs.commit();

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
                emailUsernameText.setError(getString(R.string.invalid_email));
                valid = false;
            }
            if(emailUsername.length() < 3){
                emailUsernameText.setError(getString(R.string.username_least));
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
            passwordText.setError(getString(R.string.password_least));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void loginResponse(LoginResponse result) {
        if(result!=null) {
            int statusCode = result.getStatus();


            if(statusCode == 200) {
                onLoginSuccess(result);

                GetInitialValues getInitialValues = new GetInitialValues(this, this);
                getInitialValues.getValues();

            }else if (statusCode == 404){
                onLoginFailed(result.getMessage());
            }

        }else{
            loginButton.setEnabled(true);
            onLoginFailed(getString(R.string.login_no_internet));
        }
    }

}

