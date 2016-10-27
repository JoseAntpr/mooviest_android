package com.mooviest.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.mooviest.R;
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.SingletonRestClient;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* Si el nombre del atributo en el archivo de preferencias no existe, guarda
         * en él el valor del segundo parámetro del método get()
         *
         * Para guardar un valor en el archivo de preferencias:
         *
         * SharedPreferences user_prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
         *
         * SharedPreferences.Editor editor = user_prefs.edit();
         * editor.putBoolean("default_avatar", false);
         */
        SharedPreferences app_prefs = getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        boolean tutorial = app_prefs.getBoolean("tutorial", false);
        boolean logged = app_prefs.getBoolean("logged", false);

        SharedPreferences user_prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        user_prefs.getBoolean("default_avatar", true);
        user_prefs.getString("avatar_image", "");
        user_prefs.getInt("id", 0);
        user_prefs.getString("username", "");
        user_prefs.getString("email", "");
        user_prefs.getString("token", "");


        /*
         * Si no ha visto el tutorial
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
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over

                    // Iniciamos LoginActivity para poder logearnos o registrarnos
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);

        /*
         * Si ya estaba logeado
         */
        }else{
            if(isInternetOn()) {
            /*
             * Inicializamos la instancia SingletonRestClient,
             * le añadimos el token del usuario y cargamos el usuario en una variable
             * del Singleton para su posterior uso en la aplicación
             */
                SingletonRestClient.getInstance();
                SingletonRestClient.getInstance().setNewToken(user_prefs.getString("token", ""));
                SingletonRestClient.getInstance().user = new User(user_prefs.getInt("id", 0), user_prefs.getString("username", ""), user_prefs.getString("email", ""));

            /*
             * Obtenemos los valores iniciales, que sería la lista de películas para el
             * Swipe, y las 4 listas del usuario: seen, watchlist, favourite y blacklist.
             * Una vez obtenidos, cargamos HomeActivity
             */
                GetInitialValues getInitialValues = new GetInitialValues(this, this);
                getInitialValues.getValues();
            }
        }
    }

    public final boolean isInternetOn() {
        boolean internetOn = false;

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connec.getActiveNetworkInfo();

        internetOn = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Check for network connections
        /*if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            internetOn = true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
        }*/

        if(!internetOn){
            Snackbar.make(findViewById(R.id.splash_screen),
                    "No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null).show();
        }


        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
