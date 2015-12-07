package com.share;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.james.sharedclasses.FileMetadataWrapper;
import com.example.james.sharedclasses.LoginUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.ArrayList;

public class ContactsListActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private ContactsAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    private static final String CONTACTS_KEY = "com.example.key.contacts";
    private static final String TAG = "ContactsListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        SharedPreferences sPref = LoginUtils.getSharedPreferences(this);
        sPref.registerOnSharedPreferenceChangeListener(this);

        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);

        Intent messagesServiceIntent = new Intent(getApplicationContext(), MobileMessageService.class);
        startService(messagesServiceIntent);
        Intent pollMetadataServiceIntent = new Intent(getApplicationContext(), PollMetadataService.class);
        startService(pollMetadataServiceIntent);

        ArrayList <Contact> contactsList = new ArrayList<>();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.clear();
        adapter.addAll(LoginUtils.getContacts(this));
        adapter.notifyDataSetChanged();
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

    public void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Profile");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Files");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("Contacts");

        new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        item3
                ).withInnerShadow(false)
                .withDrawerWidthPx(220)
                .withSelectedItemByPosition(3)
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "on shared pref called on key: " + key);
        if (key.equals(LoginUtils.CONTACTS_KEY)) {
            ArrayList<Contact> contacts = LoginUtils.getContacts(this);
            adapter.clear();
            adapter.addAll(contacts);
            adapter.notifyDataSetChanged();
            DataLayerUtil.sendContactsToWear(mGoogleApiClient, contacts, TAG);
        }
        if (key.equals(LoginUtils.FILE_METADATA_KEY)) {
            ArrayList<FileMetadataWrapper> filesMetadata = LoginUtils.getFileMetadata(this);
            DataLayerUtil.sendFileMetadataToWear(mGoogleApiClient, filesMetadata, TAG);
        }

    }
}
