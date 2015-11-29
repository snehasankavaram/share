package com.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.WatchViewStub;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.james.sharedclasses.Contact;

public class WatchContactsActivity extends Activity {

    private GridView mGridView;
    private com.example.james.sharedclasses.ContactsAdapter adapter;
    private DismissOverlayView mDismissOverlayView;
    private GestureDetector mGestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_contacts);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mDismissOverlayView = (DismissOverlayView) findViewById(R.id.dismiss);
                mGestureDetector = new GestureDetector(WatchContactsActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {
                        mDismissOverlayView.show();
                    }
                });

                mGridView = (GridView) stub.findViewById(R.id.grid);

                mGridView.setAdapter(new ContactsAdapter(WatchContactsActivity.this));

                mGridView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Contact c = (Contact) mGridView.getAdapter().getItem(position);
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        i.putExtra("contact", c);
                        startActivity(i);
                    }
                });

                mGridView.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mGestureDetector.onTouchEvent(event);
                    }
                });

            }
        });
    }
}
