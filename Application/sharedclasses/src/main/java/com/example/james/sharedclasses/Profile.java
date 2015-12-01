package com.example.james.sharedclasses;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import java.io.Serializable;

/**
 * Created by Sneha on 11/19/15.
 */
public class Profile implements Serializable {
    private String name;
    private String email;
    private String occupation;
    private String phoneNumber;

    public Profile (String name, String occupation) {
        this.name = name;
        this.occupation = occupation;
    }

    public String getName() {
        return this.name;
    }
    public String getOccupation() {
        return this.occupation;
    }

}
