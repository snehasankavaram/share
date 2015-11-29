package com.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.Profile;

public class ProfileActivity extends Activity {

    private float x1, x2;
    private float y1, y2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                RelativeLayout profile = (RelativeLayout) stub.findViewById(R.id.profile);
                profile.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                x1 = event.getX();
                                y1 = event.getY();
                                break;
                            case MotionEvent.ACTION_UP:
                                x2 = event.getX();
                                y2 = event.getY();
                                float deltaX = x2 - x1;
                                if (Math.abs(deltaX) > MIN_DISTANCE)
                                {
                                    Contact c = (Contact) getIntent().getSerializableExtra("contact");
                                    // Left to right swipe
                                    if (x2 > x1)
                                    {
                                        Intent i = new Intent(getApplicationContext(), NotesActivity.class);
                                        i.putExtra("contact", c);
                                        startActivity(i);
                                    }
                                    // Right to left swipe action
                                    else
                                    {
                                        Intent i = new Intent(getApplicationContext(), FilesActivity.class);
                                        i.putExtra("contact", c);
                                        startActivity(i);
                                    }
                                    return true;
                                }

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
                        return true;
                    }
                });

                TextView name = (TextView) stub.findViewById(R.id.contact_name);
                TextView phone = (TextView) stub.findViewById(R.id.contact_phone);
                TextView occupation = (TextView) stub.findViewById(R.id.contact_occupation);

                Contact c = (Contact) getIntent().getSerializableExtra("contact");
                Profile p = c.getProfile();
                name.setText(p.getName());
                phone.setText("925-351-1211");
                occupation.setText(p.getOccupation());

            }
        });
    }
}
