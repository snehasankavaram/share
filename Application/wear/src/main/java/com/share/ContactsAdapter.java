package com.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.james.sharedclasses.Contact;
import com.example.james.sharedclasses.Note;
import com.example.james.sharedclasses.Profile;

import java.util.ArrayList;

/**
 * Created by james on 11/28/15.
 */
public class ContactsAdapter extends BaseAdapter {
    ArrayList<Contact> contactsList = new ArrayList<>();
    //create random contacts for now, but fetch contacts from backend and add to ArrayList
    String [] names = {"Sally Smith", "Bob Jones", "Dylan Christopher Johnson", "Carry George", "Jonas Thomson"};
    String [] occupations = {"CEO", "Software Engineer", "Self employed", "Artist", "Writer"};
    String [] notes = {"Sally Smith", "Bob Jones", "Dylan Christopher Johnson", "Carry George", "Jonas Thomson"};


    private Context ctx = null;

    public ContactsAdapter(Context context) {
        this.ctx = context;

        for (int i = 0; i < names.length; i++) {
            Profile p = new Profile(names[i], occupations[i]);
            Note n = new Note(notes[i]);
            Contact c = new Contact(p, n);
            contactsList.add(c);
        }
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ImageView imgView = new ImageView(this.ctx);
        imgView.setScaleType(ImageView.ScaleType.FIT_END);
        imgView.setPadding(8, 8, 8, 8);

        int [] images = {com.example.james.sharedclasses.R.drawable.face1, com.example.james.sharedclasses.R.drawable.face2, com.example.james.sharedclasses.R.drawable.face3, com.example.james.sharedclasses.R.drawable.face4};
        int selected = images[contactsList.get(arg0).getProfile().getName().charAt(0) % 4];

        imgView.setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(arg2.getResources(), selected)));
        imgView.setAdjustViewBounds(Boolean.TRUE);
        imgView.setContentDescription(contactsList.get(arg0).getProfile().getName());
        imgView.setMaxHeight(100);
        imgView.setMaxWidth(100);

        return imgView;
    }

    @Override
    public long getItemId(int arg0) {
       return R.drawable.face4;
    }

    @Override
    public Object getItem(int arg0) {
        return contactsList.get(arg0);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
