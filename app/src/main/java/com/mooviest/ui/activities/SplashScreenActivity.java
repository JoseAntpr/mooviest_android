package com.mooviest.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mooviest.R;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences settings = getSharedPreferences("USER_PREFS", 0);
        boolean tutorial = settings.getBoolean("tutorial", false);
        boolean logged = settings.getBoolean("logged", false);

        /*
         * Si no se ha visto el tutorial
         */
        /*if(!tutorial) {

            // Start TutorialActivity
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
            finish();

        }else if(!logged){*/

        /*
         * Si no se ha logeado o registrado
         */
        if(!logged){
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start LoginActivity
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);

        /*
         * Si no se ha visto el tutorial
         */
        }else{
            //OBTENER PELÍCULAS DE SQLITE Y GUARDARLAS EN MOVIES_BUFFER
            //CON ASYNCTASK y en onPostExecute llamar al intent HomeActivity

            // Start HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }



}
