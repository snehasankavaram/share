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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: query to get files here
        fileList = new ArrayList<>();
        String [] namesList = {"Design Doc", "Resume", "Proposal", "Cover Letter"};
        String [] dateList = {"12/1/15", "11/30/15", "11/30/15", "11/29/15"};
        for (int i = 0; i < 4; i++) {
            fileList.add(new File(namesList[i], dateList[i]));
        }

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
