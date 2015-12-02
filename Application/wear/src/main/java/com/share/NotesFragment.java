package com.share;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.james.sharedclasses.Note;
import com.example.james.sharedclasses.Profile;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {
    Profile p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            p = (Profile) args.get("files");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_notes, container, false);
        TextView notes = (TextView) view.findViewById(R.id.notesText);

        Bundle args = getArguments();
        Note n = (Note) args.get("notes");
        if (n != null) {
            notes.setText(n.getNote());
        }
        return view;
    }

}