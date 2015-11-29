package com.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.MotionEvent;
import android.view.View;

import com.example.james.sharedclasses.Contact;

public class ContactActivity extends FragmentActivity {
    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private float y1, y2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Contact c = (Contact) getIntent().getSerializableExtra("contact");
                mPager = (ViewPager) findViewById(R.id.pager);
                mPagerAdapter = new ContactPagerAdapter(getSupportFragmentManager(), c);
                mPager.setAdapter(mPagerAdapter);
                mPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                y1 = event.getY();
                                break;
                            case MotionEvent.ACTION_UP:
                                y2 = event.getY();
                                float deltaY = y2 - y1;
                                if (Math.abs(deltaY) > MIN_DISTANCE) {
                                    // Swipe from top to bottom
                                    if (y2 > y1) {
                                        Intent i = new Intent(getApplicationContext(), WatchContactsActivity.class);
                                        startActivity(i);
                                        return true;
                                    }
                                }

                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    private class ContactPagerAdapter extends FragmentPagerAdapter {
        private Contact contact;
        private Fragment profile;
        private Fragment files;
        private Fragment notes;

        public ContactPagerAdapter(android.support.v4.app.FragmentManager fm, Contact c) {
            super(fm);
            contact = c;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            switch (position) {
                case 0:
                    if (profile == null) {
                        args.putSerializable("profile", contact.getProfile());
                        profile = new ProfileFragment();
                        profile.setArguments(args);

                    } return profile;
                case 1:
                    if (files == null) {
                        args.putSerializable("files", contact.getProfile());
                        files = new FileFragment();
                        files.setArguments(args);

                    } return files;
                case 2:
                    if (notes == null) {
                        args.putSerializable("notes", contact.getProfile());
                        notes = new NotesFragment();
                        notes.setArguments(args);

                    } return notes;

            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
