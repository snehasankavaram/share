package com.share;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.james.sharedclasses.Note;

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
        Note n = (Note) args.get("notes");

        ((TextView) rootView.findViewById(R.id.notesTextMobile)).setText(n.getNote());
        //Integer.toString(args.getInt(ARG_OBJECT)));
        return rootView;
    }
}
