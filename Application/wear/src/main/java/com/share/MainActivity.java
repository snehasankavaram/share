package com.share;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.WatchViewStub;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends FragmentActivity {
    private static final int NUM_PAGES = 3;
    private MyViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private DismissOverlayView mDismissOverlayView;
    private GestureDetector mGestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mPager = (MyViewPager) findViewById(R.id.pager);
                mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {
                        mDismissOverlayView.show();
                    }
                });
                mPager.setGestureDetector(mGestureDetector);
                mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
                mPager.setAdapter(mPagerAdapter);

                mDismissOverlayView = (DismissOverlayView) findViewById(R.id.dismiss);

            }
        });
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {
        private Fragment contacts;
        private Fragment connect;
        private Fragment profile;

        public MainPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (contacts == null) {
                        contacts = new ContactsFragment();
                    } return contacts;
                case 1:
                    if (connect == null) {
                        connect = new FileFragment();

                    } return connect;
                case 2:
                    if (profile == null) {
                        profile = new NotesFragment();
                    } return profile;

            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
