package com.share;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.james.sharedclasses.Profile;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView name = (TextView) view.findViewById(R.id.contact_name);
        TextView phone = (TextView) view.findViewById(R.id.contact_phone);
        TextView occupation = (TextView) view.findViewById(R.id.contact_occupation);

        Bundle args = getArguments();
        Profile p = (Profile) args.get("profile");
        if (p != null) {
            name.setText(p.getName());
            phone.setText("925-351-1211");
            occupation.setText(p.getOccupation());
        }

        return view;
    }

}
