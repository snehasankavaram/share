package com.example.james.sharedclasses;

import java.io.Serializable;

/**
 * Created by Sneha on 11/19/15.
 */
public class Note implements Serializable {
    private String notesText;

    public Note (String text) {
        this.notesText = text;
    }

    public void setNote (String text) {
        this.notesText = text;
    }

    public String getNote () {
        return this.notesText;
    }
}
