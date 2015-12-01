package com.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.james.sharedclasses.File;
import com.example.james.sharedclasses.FilesAdapter;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

public class MyFilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);

        //TODO query for actual files
        ArrayList <File>fileList = new ArrayList<>();
        String [] namesList = {"Design Doc", "Resume", "Proposal", "Cover Letter", "Resume 1", "Resume 2"};
        String [] dateList = {"12/1/15", "11/30/15", "11/30/15", "11/29/15", "11/29/15", "11/29/15"};
        for (int i = 0; i < 6; i++) {
            fileList.add(new File(namesList[i], dateList[i]));
        }
        FilesAdapter adapter = new FilesAdapter(this, fileList);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("My profile");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("My files");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("My contacts");

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        item3
                )
                .withToolbar(toolbar)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent i;
                        switch (position) {
                            case 0:
                                Log.d("Hamburger", "Clicked profile");
                                i = new Intent(getApplicationContext(), MyProfileActivity.class);
                                startActivity(i);
                                break;
                            case 2:
                                Log.d("Hamburger", "Clicked files");
                                break;
                            case 4:
                                Log.d("Hamburger", "Clicked contact");
                                i = new Intent(getApplicationContext(), ContactsListActivity.class);
                                startActivity(i);
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

}
