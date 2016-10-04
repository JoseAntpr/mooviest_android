package com.mooviest.ui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.models.Profile;
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.Errors;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.rest.UpdateProfileResponse;
import com.mooviest.ui.tasks.UpdateProfile;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private FloatingActionButton fab_change_image;
    private TextInputLayout tilEditUsername;
    private TextInputLayout tilEditFirstName;
    private TextInputLayout tilEditLastName;
    private TextInputLayout tilEditEmail;
    private TextInputLayout tilEditBorn;
    private TextInputLayout tilEditCity;
    private TextInputLayout tilEditPostalCode;
    private Calendar bornCalendar;
    private User user;
    private Profile userProfile;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView avatarImage;
    private MultipartBody.Part imageProfileUploaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Editar perfil");

        user = SingletonRestClient.getInstance().user;
        userProfile = user.getProfile();

        tilEditUsername = (TextInputLayout) findViewById(R.id.til_edit_username);
        tilEditUsername.getEditText().setText(user.getUsername());
        tilEditEmail = (TextInputLayout) findViewById(R.id.til_edit_email);
        tilEditEmail.getEditText().setText(user.getEmail());

        tilEditFirstName = (TextInputLayout) findViewById(R.id.til_edit_first_name);
        if(user.getFirstName() != null) {
            tilEditFirstName.getEditText().setText(user.getFirstName());
        }
        tilEditLastName = (TextInputLayout) findViewById(R.id.til_edit_last_name);
        if(user.getLastName() != null) {
            tilEditLastName.getEditText().setText(user.getLastName());
        }
        tilEditCity = (TextInputLayout) findViewById(R.id.til_edit_city);
        if(userProfile.getCity() != null) {
            tilEditCity.getEditText().setText(userProfile.getCity());
        }
        tilEditPostalCode = (TextInputLayout) findViewById(R.id.til_edit_postal_code);
        if(userProfile.getPostalCode() != null) {
            tilEditPostalCode.getEditText().setText(userProfile.getPostalCode());
        }

        // Avatar image and camera button
        avatarImage = (ImageView)findViewById(R.id.edit_profile_avatar_image);
        String avatar = userProfile.getAvatar();
        if(avatar.contains("no-image")){
            Picasso.with(this).load(R.drawable.no_image).transform(new RoundedTransformation(1000, 0)).fit().centerCrop().into(avatarImage);
        }else{
            Picasso.with(this).load(SingletonRestClient.baseUrl + avatar).transform(new RoundedTransformation(1000, 0)).fit().centerCrop().into(avatarImage);
        }
        Picasso.with(this).setIndicatorsEnabled(false);

        fab_change_image = (FloatingActionButton) findViewById(R.id.fab_change_image);
        fab_change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Floating button pressed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent takePictureIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
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

        if(userProfile.getBorn() != null){
            String myFormat = "dd MMMM yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            tilEditBorn.getEditText().setText(sdf.format(userProfile.getBorn()));
            bornCalendar.setTime(userProfile.getBorn());
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
                updateProfileTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri photoUri = data.getData();
            // Do something with the photo based on Uri
            Bitmap selectedImage = null;
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Check image size. The image must be bigger than 180x180
            if(selectedImage.getHeight() >= 180 && selectedImage.getWidth() >=180){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();



                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(stream.toByteArray()));
                /*
                File file = new File("path");
                OutputStream os;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                decoded.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //avatarImage.setImageBitmap(decoded);
                Picasso.with(this).load(file).transform(new RoundedTransformation(1000, 0)).fit().centerCrop().into(avatarImage);
                */
                Picasso.with(this).load(photoUri).transform(new RoundedTransformation(1000, 0)).fit().centerCrop().into(avatarImage);

                imageProfileUploaded = MultipartBody.Part.createFormData("profile.avatar", "profile", RequestBody.create(MediaType.parse("image/*"), byteArray));

            }else {
                Toast.makeText(getBaseContext(), "Image must be bigger than 180x180", Toast.LENGTH_LONG).show();
            }

        }
    }


    private void setBornLabel() {

        String myFormat = "dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        tilEditBorn.getEditText().setText(sdf.format(bornCalendar.getTime()));
    }

    private String getBornLabel(){
        String bornFormat = null;

        if(tilEditBorn.getEditText().getText().toString() != ""){
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            bornFormat=sdf.format(bornCalendar.getTime());
        }

        return bornFormat;
    }

    private void updateProfileTask(){
        UpdateProfile updateProfile = new UpdateProfile(imageProfileUploaded){
            @Override
            protected void onPostExecute(UpdateProfileResponse result) {
                super.onPostExecute(result);
                if(result!=null) {
                    if(result.getStatus() == 200) {
                        SingletonRestClient.getInstance().user = result.getUser();

                        startActivity(
                                new Intent(EditProfileActivity.this, ProfileActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }else{
                        Errors errors = result.getErrors();
                        if(errors.getUsername().size() != 0){
                            tilEditUsername.setError(errors.getUsername().get(0));
                        }
                        if(errors.getEmail().size() != 0){
                            tilEditEmail.setError(errors.getEmail().get(0));
                        }

                        Toast.makeText(getBaseContext(), "Update profile failed, check fields errors", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        updateProfile.execute(
                tilEditUsername.getEditText().getText().toString(),
                tilEditFirstName.getEditText().getText().toString(),
                tilEditLastName.getEditText().getText().toString(),
                tilEditEmail.getEditText().getText().toString(),
                getBornLabel(),
                tilEditCity.getEditText().getText().toString(),
                tilEditPostalCode.getEditText().getText().toString(),
                Locale.getDefault().getLanguage()
        );
    }
}
