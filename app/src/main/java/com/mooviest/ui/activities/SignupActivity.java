package com.mooviest.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.Errors;
import com.mooviest.ui.rest.SignupResponse;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.RegisterResponseInterface;
import com.mooviest.ui.tasks.RegisterUser;

import java.util.Locale;


public class SignupActivity extends AppCompatActivity implements RegisterResponseInterface{
    private static final String TAG = "SignupActivity";


    private TextInputLayout tilUsername;
    private TextInputLayout tilEmail;
    private TextInputLayout tilSignupPassword;
    private TextInputLayout tilConfirmPassword;
    private boolean textWatcher = false;
    private Button signupButton;
    private TextView loginLink;

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

            Toast.makeText(getBaseContext(), getString(R.string.register_check_errors), Toast.LENGTH_LONG).show();
        }else {

            signupButton.setEnabled(false);

            /*
             * Registro usuario
             */
            RegisterUser loginUser = new RegisterUser(this, SignupActivity.this);
            String lang = "en";
            if(Locale.getDefault().getLanguage().equals("es")){
                lang = "es";
            }
            loginUser.execute(username, email, password, lang);
        }
    }


    public void onSignupSuccess(SignupResponse result) {
        setResult(RESULT_OK, null);

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

    @Override
    public void onBackPressed() {
        // Disable going back to the HomeActivity
        moveTaskToBack(true);
    }

    public void onSignupFailed(Errors errors) {
        if(errors.getEmail().size() != 0){
            tilEmail.setError(getString(R.string.email_exists));
        }
        if(errors.getUsername().size() != 0){
            tilUsername.setError(getString(R.string.username_exists));
        }
        if(errors.getPassword().size() != 0){
            tilSignupPassword.setError(errors.getPassword().get(0));
        }

        Toast.makeText(getBaseContext(), getString(R.string.register_check_errors), Toast.LENGTH_LONG).show();
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
            tilUsername.setError(getString(R.string.username_least));
            valid = false;
        } else {
            tilUsername.setError(null);
        }

        return valid;
    }

    public boolean validEmail(String email){
        boolean valid = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            tilEmail.setError(null);
        }

        return valid;
    }

    public boolean validPassword(String password, String confirmPassword){
        boolean valid = true;
        if (password.isEmpty() || password.length() < 6) {
            tilSignupPassword.setError(getString(R.string.password_least));
            valid = false;
        } else {
            tilSignupPassword.setError(null);
            if(password.equals(confirmPassword)){
                tilConfirmPassword.setError(null);
            }else {
                valid = false;
                tilConfirmPassword.setError(getString(R.string.password_match));
            }
        }

        return valid;
    }

    @Override
    public void registerResponse(SignupResponse result) {
        if(result !=null){
            int statusCode = result.getStatus();

            if(statusCode == 201) {
                // Save SharedPreferences and init SingletonRestClient with the new token
                onSignupSuccess(result);


                GetInitialValues getInitialValues = new GetInitialValues(this, this);
                getInitialValues.getValues();

            }else if (statusCode == 400){
                onSignupFailed(result.getErrors());
            }

        }else{
            signupButton.setEnabled(true);
            Toast.makeText(getBaseContext(), getString(R.string.register_no_internet), Toast.LENGTH_LONG).show();
        }
    }

}