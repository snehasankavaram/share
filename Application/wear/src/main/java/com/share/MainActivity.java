package com.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.WatchViewStub;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.Profile;

public class MainActivity extends FragmentActivity {
    private static final int NUM_PAGES = 3;
    private MyViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private DismissOverlayView mDismissOverlayView;
    private GestureDetector mGestureDetector;
    private final String TAG = "MainActivity";

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
        Intent intent = new Intent(getApplicationContext(), DataLayerListenerService.class);
        startService(intent);

        if (!LoginUtils.isLoggedIn(this)) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }
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
                        connect = new ConnectFragment();

                    } return connect;
                case 2:
                    if (profile == null) {
                        Bundle args = new Bundle();
                        args.putSerializable("profile", getSelf());
                        profile = new ProfileFragment();
                        profile.setArguments(args);
                    } return profile;

            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        private Profile getSelf() {
            Profile p = LoginUtils.getProfile(getBaseContext());
            if (p != null) {
                return p;
            }
            return new Profile("asdf","ggg","3","asdf");
        }
//        private void getSelf(final ProfileFragment fragment) {
//            ServerEndpoint service = retrofit.create(ServerEndpoint.class);
//            Call<GetUserRequestWrapper> call = service.getUser(LoginUtils.getLoginToken(MainActivity.this));
//            Log.d(TAG, "get self called");
//            call.enqueue(new Callback<GetUserRequestWrapper>() {
//                @Override
//                public void onResponse(Response<GetUserRequestWrapper> response, Retrofit retrofit) {
//                    // response.isSuccess() is true if the response code is 2xx
//                    Log.d(TAG, "Got response");
//                    if (response.isSuccess()) {
//                        GetUserRequestWrapper user = response.body();
////                        Bundle args = new Bundle();
////                        args.putSerializable("profile", user.getProfile());
////                        Log.d(TAG, "Put profile in args");
////                        fragment.setArguments(args);
//                        fragment.onProfileAvailable(user.getProfile());
//                    } else {
//                        int statusCode = response.code();
//                        // handle request errors yourself
//                        ResponseBody errorBody = response.errorBody();
//                    }
//                }
//                @Override
//                public void onFailure(Throwable t) {
//                    // handle execution failures like no internet connectivity
//                }
//            });
//        }
    }
}
