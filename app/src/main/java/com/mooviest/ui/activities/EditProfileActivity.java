package com.mooviest.ui.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.SingletonRestClient;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout tilEditUsername;
    private TextInputLayout tilFirstName;
    private TextInputLayout tilLastName;
    private TextInputLayout tilEditEmail;
    private FloatingActionButton fab_change_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Editar perfil");

        User u = SingletonRestClient.getInstance().user;

        tilEditUsername = (TextInputLayout) findViewById(R.id.til_edit_username);
        tilEditUsername.getEditText().setText(u.getUsername());
        tilEditEmail = (TextInputLayout) findViewById(R.id.til_edit_email);
        tilEditEmail.getEditText().setText(u.getEmail());

        tilFirstName = (TextInputLayout) findViewById(R.id.til_edit_first_name);
        if(u.getFirstName() != null) {
            tilFirstName.getEditText().setText(u.getFirstName());
        }
        tilLastName = (TextInputLayout) findViewById(R.id.til_edit_last_name);
        if(u.getLastName() != null) {
            tilLastName.getEditText().setText(u.getLastName());
        }

        ImageView imageView = (ImageView)findViewById(R.id.edit_profile_avatar_image);
        String avatar =u.getProfile().getAvatar();
        if(avatar.contains("no-image")){
            //avatar = SingletonRestClient.getInstance().baseMediaUrl + avatar;
            Picasso.with(this).load(R.drawable.no_image).transform(new RoundedTransformation(1000, 0)).fit().centerCrop().into(imageView);
        }else{
            Picasso.with(this).load(avatar).transform(new RoundedTransformation(1000, 0)).fit().centerCrop().into(imageView);
        }
        Picasso.with(this).setIndicatorsEnabled(false);

        fab_change_image = (FloatingActionButton) findViewById(R.id.fab_change_image);
        fab_change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Floating button pressed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }
}
