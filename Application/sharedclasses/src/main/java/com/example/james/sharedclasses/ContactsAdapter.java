package com.example.james.sharedclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sneha on 11/19/15.
 */
public class ContactsAdapter extends ArrayAdapter<Contact>{
    private static class ViewHolder {
        TextView name;
        TextView occupation;
        ImageView image;
    }
    public ArrayList<Contact> contacts;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, R.layout.contact_item, contacts);
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.contact_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.occupation = (TextView) convertView.findViewById(R.id.occupation);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(contact.getProfile().getName());
        viewHolder.occupation.setText(contact.getProfile().getOccupation() + "");

        int [] images = {R.drawable.face1, R.drawable.face2, R.drawable.face3, R.drawable.face4};
        int selected = images[contact.getProfile().getName().charAt(0) % 4];
        viewHolder.image.setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(parent.getResources(), selected)));
        // Return the completed view to render on screen
        return convertView;
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
