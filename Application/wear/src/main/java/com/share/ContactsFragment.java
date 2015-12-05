package com.share;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.wearable.view.DismissOverlayView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.LoginUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    private GridView mGridView;
    private DismissOverlayView mDismissOverlayView;
    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid);
        ContactsAdapter adapter = new ContactsAdapter(this.getContext(), LoginUtils.getContacts(getActivity()));
        mGridView.setAdapter(adapter);

        mDismissOverlayView = (DismissOverlayView) view.findViewById(R.id.dismiss);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (mDismissOverlayView != null && mDismissOverlayView.getVisibility() == View.VISIBLE) {
                    return;
                }
                Contact c = (Contact) mGridView.getAdapter().getItem(position);
                Intent i = new Intent(getActivity().getApplicationContext(), ContactActivity.class);
                i.putExtra("contact", c);
                startActivity(i);
            }
        });


        final GestureDetector mGestureDetector = new GestureDetector(getActivity().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                mDismissOverlayView.show();
            }
        });

        mGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        return view;
    }
}
