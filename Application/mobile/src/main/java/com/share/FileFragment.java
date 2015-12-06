package com.share;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.james.sharedclasses.File;
import com.example.james.sharedclasses.FilesAdapter;

import java.util.ArrayList;

/**
 * Created by Sneha on 11/26/15.
 */
public class FileFragment extends Fragment {
    ArrayList<File> fileList;

    private FilesAdapter adapter;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        fileList = (ArrayList<File>) args.get("files");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.contact_files, container, false);

        adapter = new FilesAdapter(getContext(), fileList);

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);


        //Integer.toString(args.getInt(ARG_OBJECT)));
        return rootView;
    }
}
