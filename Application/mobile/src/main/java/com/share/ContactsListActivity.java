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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ContactsListActivity extends AppCompatActivity implements Observer {

    private ContactsAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    private static final String CONTACTS_KEY = "com.example.key.contacts";
    private static final String TAG = "ContactsListActivity";
    private Profile myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Intent intent = new Intent(getApplicationContext(), MobileMessageService.class);
        startService(intent);

        myProfile = (Profile) getIntent().getSerializableExtra("profile");

        ArrayList <Contact> contactsList = getContacts();
        adapter = new ContactsAdapter(this, contactsList);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

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

        createToolbar();

        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();

//        sendContactsToWear(contactsList);
//        sendMessage("/asdf", );

    }

    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient.connect();
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }

    public ArrayList<Contact>  getContacts() {
        ArrayList<Contact> contactsList = new ArrayList<>();
        String [] names = {"Sally Smith", "Bob Jones", "Dylan Lee", "Carry George", "Jonas Thomson"};
        String [] occupations = {"CEO of Tech, Inc.", "Engineer at Snapchat", "Entrepreneur", "Contractor", "Project Manager"};
        String [] notes = {"Shows potential.", "I think I like this guy", "Seems legit, brief conversation at tech conference in May", "Met in startup fair, need to look at design documents", "Told him I will get back to him"};
        for (int i = 0; i < names.length; i++) {
            Profile p = new Profile(names[i], names[i]+"@gmail.com","136-234-1111", occupations[i]);
            Contact c = new Contact(p, notes[i]);
            contactsList.add(c);
        }
        return contactsList;
    }

    public void createToolbar() {
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
                        Intent i;
                        switch (position) {
                            case 0:
                                Log.d("Hamburger", "Clicked profile");
                                i = new Intent(getApplicationContext(), MyProfileActivity.class);
                                i.putExtra("profile", myProfile);
                                startActivity(i);
                                break;
                            case 2:
                                Log.d("Hamburger", "Clicked files");
                                i = new Intent(getApplicationContext(), MyFilesActivity.class);
                                startActivity(i);
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

    private void sendContactsToWear(ArrayList<Contact> contacts) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/contacts");
        ArrayList<DataMap> contactsAsDataMaps = new ArrayList<>();
        for (Contact c : contacts) {
            contactsAsDataMaps.add(c.putToDataMap(new DataMap()));
        }
        putDataMapReq.getDataMap().putDataMapArrayList(CONTACTS_KEY, contactsAsDataMaps);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if(result.getStatus().isSuccess()) {
                    Log.d(TAG, "Data item set: " + result.getDataItem().getUri());
                }
            }
        });
    }
}
