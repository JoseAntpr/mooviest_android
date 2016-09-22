package com.mooviest.ui.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.TextView;

import com.mooviest.R;
import com.mooviest.ui.models.Profile;
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.SingletonRestClient;

public class ProfileActivity extends AppCompatActivity {

    private TextView first_last_name_profile;
    private TextView username_profile;
    private TextView city_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_profile);

        first_last_name_profile = (TextView) findViewById(R.id.first_last_name_profile);
        username_profile = (TextView) findViewById(R.id.username_profile);
        city_profile = (TextView) findViewById(R.id.city_profile);

        User u = SingletonRestClient.getInstance().user;
        Profile p = u.getProfile();
        if(u.getFirstName().isEmpty() && u.getLastName().isEmpty()) {
            first_last_name_profile.setVisibility(View.GONE);
        }else{
            first_last_name_profile.setText(u.getFirstName() + " " + u.getLastName());
        }

        username_profile.setText("@" + u.getUsername());

        if(p.getCity().isEmpty()){
            city_profile.setVisibility(View.GONE);
        }else{
            city_profile.setText(p.getCity());
        }
    }






    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }
}
