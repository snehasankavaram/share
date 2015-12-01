package com.share;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.james.sharedclasses.Contact;

/**
 * Created by Sneha on 11/26/15.
 */
public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private Contact contact;
    private Fragment profile;
    private Fragment files;
    private Fragment notes;

    private String [] tabTitles = {"profile", "files", "notes"};

    public ProfilePagerAdapter(FragmentManager fm, Contact c) {
        super(fm);
        contact = c;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                if (profile == null) {
                    args.putSerializable("profile", contact.getProfile());
                    profile = new ProfileFragment();
                    profile.setArguments(args);

                } return profile;
            case 1:
                if (files == null) {
                    files = new FileFragment();

                } return files;
            case 2:
                if (notes == null) {
                    args.putSerializable("notes", contact.getNotes());
                    notes = new NotesFragment();
                    notes.setArguments(args);

                } return notes;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
