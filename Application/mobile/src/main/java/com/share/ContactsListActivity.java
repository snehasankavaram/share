package com.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.ContactsAdapter;
import com.example.james.sharedclasses.Profile;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ContactsListActivity extends AppCompatActivity implements Observer{

    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ArrayList <Contact> contactsList = new ArrayList<>();

        //create random contacts for now, but fetch contacts from backend and add to ArrayList
        String [] names = {"Sally Smith", "Bob Jones", "Dylan Christopher Johnson", "Carry George", "Jonas Thomson"};
        String [] occupations = {"Sally Smith", "Bob Jones", "Dylan Christopher Johnson", "Carry George", "Jonas Thomson"};
        for (int i = 0; i < names.length; i++) {
            Profile p = new Profile(names[i], occupations[i]);
            Contact c = new Contact(p);
            contactsList.add(c);
        }


        adapter = new ContactsAdapter(this, contactsList);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
//        EarthquakeInfoSingleton.getInstance().addObserver(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Contact c = (Contact) adapter.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), ContactPageActivity.class);
                i.putExtra("contact", c);
                startActivity(i);
            }
        });

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("My profile");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("My files");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("My contacts");

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
                        switch (position) {
                            case 0:
                                Log.d("Hamburger", "Clicked profile");
                                break;
                            case 2:
                                Log.d("Hamburger", "Clicked files");
                                break;
                            case 4:
                                Log.d("Hamburger", "Clicked contact");
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data) {
        ListView listView = (ListView) findViewById(R.id.list);
        ((ContactsAdapter) listView.getAdapter()).notifyDataSetChanged();
    }
}
