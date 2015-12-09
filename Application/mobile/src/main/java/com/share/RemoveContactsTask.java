package com.share;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.ContactProfileWrapper;
import com.example.james.sharedclasses.DeleteContactRequest;
import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.ServerEndpoint;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by james on 12/8/15.
 */
public class RemoveContactsTask extends AsyncTask<Void,Void,ArrayList<Contact>> {
    ArrayList<Contact> contacts;
    ServerEndpoint service;
    private final String TAG = "RemoveContactsTask";
    private Context context;

    public RemoveContactsTask(Context context, ArrayList<Contact> contacts, ServerEndpoint service) {
        this.context = context;
        this.contacts = contacts;
        this.service = service;
    }

    @Override
    protected ArrayList<Contact> doInBackground(Void... params) {
        ArrayList<Contact> deletedContacts = new ArrayList<>();
        for (Contact c: this.contacts) {
            Log.d("asdf", "" + c.getContactId());
            Call<ContactProfileWrapper> call = service.deleteContact(new DeleteContactRequest(c.getContactId()));
            try{
                Response<ContactProfileWrapper> response = call.execute();
                if (response.isSuccess()) {
                    deletedContacts.add(c);
                }
                else {
                    int statusCode = response.code();
                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    try {
                        Log.d(TAG, String.format("Error: %d with body: %s", statusCode, errorBody.string()));
                    }
                    catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }

            } catch (IOException e) {

            }
            return deletedContacts;

        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Contact> result) {
        ArrayList<Contact> contacts = LoginUtils.getContacts(context);
        for (Contact c : result) {
            contacts.remove(c);
        }
        LoginUtils.setContacts(context, contacts);
    }
}
