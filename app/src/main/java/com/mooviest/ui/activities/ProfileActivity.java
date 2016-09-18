package com.mooviest.ui.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.TextView;

import com.mooviest.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView first_last_name_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_profile);

        //first_last_name_profile = (TextView) findViewById(R.id.first_last_name_profile);
        //first_last_name_profile.setVisibility(View.INVISIBLE);
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
