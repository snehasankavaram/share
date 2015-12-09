package com.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.james.sharedclasses.Contact;

import java.util.ArrayList;

/**
 * Created by james on 11/28/15.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private static class ViewHolder {
        TextView name;
        ImageView image;
    }

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, R.layout.contact_item, contacts);
        this.context = context;
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
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(contact.getProfile().getName().split("\\s+")[0]);

        int [] images = {com.example.james.sharedclasses.R.drawable.face1, com.example.james.sharedclasses.R.drawable.face2, com.example.james.sharedclasses.R.drawable.face3, com.example.james.sharedclasses.R.drawable.face4};
        int selected = images[contact.getProfile().getName().charAt(0) % 4];

        if (contact.getProfile().getName() != null) {
            String uri = String.format("@drawable/%s", contact.getProfile().getName().replace(" ", "_").toLowerCase());
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            if (imageResource != 0) {
                selected = imageResource;
            }
        }

        viewHolder.image.setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(parent.getResources(), selected)));
        // Return the completed view to render on screen
        return convertView;
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);

        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);

        return circleBitmap;
    }
}

