package com.share;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.Profile;

/**
 * Created by Sneha on 11/26/15.
 */
public class ContactPageActivity extends AppCompatActivity {
    ProfilePagerAdapter mProfilePagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);


        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Contact c = (Contact) getIntent().getSerializableExtra("contact");
        Profile p = c.getProfile();

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mProfilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), c);


        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(mProfilePagerAdapter);
        tabs.setupWithViewPager(pager);
    }
}


