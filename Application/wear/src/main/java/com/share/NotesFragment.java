package com.share;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.james.sharedclasses.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {
    User p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            p = (User) args.get("files");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_notes, container, false);
        TextView notes = (TextView) view.findViewById(R.id.notesText);

        Bundle args = getArguments();
        String n = (String) args.get("notes");
        if (n != null) {
            notes.setText(n);
        }
        return view;
    }

}
