package com.share;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Sneha on 11/26/15.
 */
public class NotesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.contact_notes, container, false);
        Bundle args = getArguments();
        String notes = (String) args.get("notes");

        ((TextView) rootView.findViewById(R.id.notesTextMobile)).setText(notes);
        //Integer.toString(args.getInt(ARG_OBJECT)));
        return rootView;
    }
}
