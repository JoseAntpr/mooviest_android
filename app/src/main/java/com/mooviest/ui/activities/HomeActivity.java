package com.mooviest.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.SingletonSwipe;
import com.mooviest.ui.adapters.ViewPagerAdapter;
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.MooviestApiInterface;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.rest.UserProfileResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView side_avatar_image;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout linear_nav_header;
    TextView side_username;
    TextView side_email;
    private ProgressDialog mProgressDialog;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private int[] tabIcons = {
            R.drawable.ic_swipe,
            R.drawable.ic_thumb_up,
            R.drawable.ic_account
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore value of user from saved state
            SingletonRestClient.getInstance().user = savedInstanceState.getParcelable("USER");
        }
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ENABLE VIEWPAGER SWIPE
        SingletonSwipe.getInstance().enabled=true;

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //GUARDA EL ESTADO DE TODOS LOS FRAGMENTS DEL VIEW PAGER
        viewPager.setOffscreenPageLimit(2);
        //SETUP VIEW PAGER (ADD 3 FRAGMENTS)
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        //SIDE MENU
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);

        setupNavHeader(headerLayout);
    }

    public void setupNavHeader(final View headerLayout){

        User u = SingletonRestClient.getInstance().user;

        linear_nav_header = (LinearLayout) headerLayout.findViewById(R.id.linear_nav_header);
        linear_nav_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ASYNCTASK GET USER AND PROFILE
                new GetUserProfile().execute(SingletonRestClient.getInstance().user.getId());
                // Close nav bar left
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        SharedPreferences user_prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);

        side_avatar_image = (ImageView) headerLayout.findViewById(R.id.side_avatar_image);
        side_username = (TextView) headerLayout.findViewById(R.id.side_username);
        side_email = (TextView) headerLayout.findViewById(R.id.side_email);

        side_username.setText("@" + u.getUsername());
        side_email.setText(u.getEmail());

        // if False
        if(!user_prefs.getBoolean("default_avatar", true)) {
            Picasso.with(this).load(user_prefs.getString("avatar_image", "")).transform(new RoundedTransformation(10, 0)).into(side_avatar_image);
            Picasso.with(this).setIndicatorsEnabled(false);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("USER", SingletonRestClient.getInstance().user);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "ONE");
        adapter.addFragment(new TwoFragment(), "TWO");
        adapter.addFragment(new UserListsFragment(), "My lists");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }



    /************************  ASYNCTASKS  ************************/


    /*
     * AYNCTASK GET USER AND PROFILE
     */
    public class GetUserProfile extends AsyncTask<Integer, String, UserProfileResponse> {

        public GetUserProfile(){
            mProgressDialog = new ProgressDialog(HomeActivity.this, R.style.AppTheme_Dark_Dialog);
            mProgressDialog.setMessage("Loading please wait...");
            mProgressDialog.setIndeterminate(true);
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected UserProfileResponse doInBackground(Integer... params) {
            MooviestApiInterface apiInterface= SingletonRestClient.getInstance().mooviestApiInterface;

            Call<UserProfileResponse> call = apiInterface.getUserProfile(params[0]);
            UserProfileResponse result = null;
            try {
                result = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(UserProfileResponse result) {
            super.onPostExecute(result);

            if (mProgressDialog != null || mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            if(result!=null) {
                SingletonRestClient.getInstance().user = result.getUser();
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        }
    }


}
