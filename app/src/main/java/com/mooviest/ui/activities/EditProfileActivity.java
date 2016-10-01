package com.mooviest.ui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.models.Profile;
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.ProfileSaved;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity implements ProfileSaved{

    private Toolbar toolbar;
    private FloatingActionButton fab_change_image;
    private TextInputLayout tilEditUsername;
    private TextInputLayout tilFirstName;
    private TextInputLayout tilLastName;
    private TextInputLayout tilEditEmail;
    private TextInputLayout tilEditBorn;
    private TextInputLayout tilEditCity;
    private TextInputLayout tilEditPostalCode;
    private Calendar bornCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Editar perfil");

        User u = SingletonRestClient.getInstance().user;
        Profile p = u.getProfile();

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
        tilEditCity = (TextInputLayout) findViewById(R.id.til_edit_city);
        if(p.getCity() != null) {
            tilEditCity.getEditText().setText(p.getCity());
        }
        tilEditPostalCode = (TextInputLayout) findViewById(R.id.til_edit_postal_code);
        if(p.getPostalCode() != null) {
            tilEditPostalCode.getEditText().setText(p.getPostalCode());
        }

        // Avatar image and camera button
        ImageView imageView = (ImageView)findViewById(R.id.edit_profile_avatar_image);
        String avatar = p.getAvatar();
        if(avatar.contains("no-image")){
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

        // Born Datepicker
        tilEditBorn = (TextInputLayout) findViewById(R.id.til_edit_born);
        bornCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                bornCalendar.set(Calendar.YEAR, year);
                bornCalendar.set(Calendar.MONTH, monthOfYear);
                bornCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setBornLabel();
            }

        };

        if(p.getBorn() != null){
            String myFormat = "dd MMMM yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            tilEditBorn.getEditText().setText(sdf.format(p.getBorn()));
            bornCalendar.setTime(p.getBorn());
        }
        tilEditBorn.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(
                        EditProfileActivity.this,
                        date,
                        bornCalendar.get(Calendar.YEAR),
                        bornCalendar.get(Calendar.MONTH),
                        bornCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save_profile:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setBornLabel() {

        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        tilEditBorn.getEditText().setText(sdf.format(bornCalendar.getTime()));
    }

    @Override
    public void profileSaved() {
        
    }
}
