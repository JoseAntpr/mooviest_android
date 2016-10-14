package com.mooviest.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.adapters.ViewPagerAdapter;
import com.mooviest.ui.models.Profile;
import com.mooviest.ui.models.User;
import com.mooviest.ui.rest.SingletonRestClient;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private TextView first_last_name_profile;
    private TextView username_profile;
    private TextView city_profile;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ViewPager profile_pager;
    TabLayout profile_tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore value of user from saved state
            SingletonRestClient.getInstance().user = savedInstanceState.getParcelable("USER");
        }
        initActivityTransitions();
        setContentView(R.layout.activity_profile);

        // **** TEXT VIEW *****
        first_last_name_profile = (TextView) findViewById(R.id.first_last_name_profile);
        username_profile = (TextView) findViewById(R.id.username_profile);
        city_profile = (TextView) findViewById(R.id.city_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // **** GET DATA FROM USER *****
        User u = SingletonRestClient.getInstance().user;
        Profile p = u.getProfile();
        if(u.getFirstName() == null || u.getLastName() == null){
            /*if(u.getFirstName().isEmpty() && u.getLastName().isEmpty()) {

            }*/
            first_last_name_profile.setVisibility(View.GONE);
        }else{
            first_last_name_profile.setText(u.getFirstName() + " " + u.getLastName());
        }

        username_profile.setText("@" + u.getUsername());

        if(p.getCity() == null){
            /*if(p.getCity().isEmpty()) {

            }*/
            city_profile.setVisibility(View.GONE);
        }else{
            city_profile.setText(p.getCity());
        }

        // Profile image
        ImageView imageView = (ImageView)findViewById(R.id.profile_avatar_image);
        String avatar =u.getProfile().getAvatar();
        if(avatar.contains("no-image")){
            //avatar = SingletonRestClient.getInstance().baseMediaUrl + avatar;
            Picasso.with(this).load(R.drawable.no_image).transform(new RoundedTransformation(1000, 0)).fit().centerCrop().into(imageView);
        }else{
            Picasso.with(this).load(SingletonRestClient.baseUrl + avatar).transform(new RoundedTransformation(1000, 0)).fit().centerCrop().into(imageView);
        }
        Picasso.with(this).setIndicatorsEnabled(false);

        // **** CONF VIEW *****
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.profile_collapsing_toolbar);
        if((u.getFirstName() == null || u.getFirstName() == "") || (u.getLastName() == null || u.getLastName() == "")) {
            collapsingToolbarLayout.setTitle(getString(R.string.my_profile));
        }else{
            collapsingToolbarLayout.setTitle(u.getFirstName() + " " + u.getLastName());
        }
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        int primary = getResources().getColor(R.color.colorPrimary);
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        collapsingToolbarLayout.setContentScrimColor(primary);
        collapsingToolbarLayout.setStatusBarScrimColor(primaryDark);

        // Load fragments
        profile_pager = (ViewPager) findViewById(R.id.profile_pager);
        profile_pager.setOffscreenPageLimit(2);
        setupViewPager(profile_pager);

        // Tabs
        profile_tabs = (TabLayout) findViewById(R.id.profile_tabs);
        profile_tabs.setupWithViewPager(profile_pager);
        profile_tabs.getTabAt(0).setText(getString(R.string.followers));
        profile_tabs.getTabAt(1).setText(getString(R.string.following));
        profile_tabs.getTabAt(2).setText(getString(R.string.celebrities));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_profile:
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                //finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("USER", SingletonRestClient.getInstance().user);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FollowersFragment(), "Followers");
        adapter.addFragment(new FollowingFragment(), "Following");
        adapter.addFragment(new CelebritiesFragment(), "Celebrities");
        viewPager.setAdapter(adapter);
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
