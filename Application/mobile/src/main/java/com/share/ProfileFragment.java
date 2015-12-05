package com.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.james.sharedclasses.Profile;

/**
 * Created by Sneha on 11/26/15.
 */
public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.contact_profile, container, false);
        Bundle args = getArguments();
        Profile p = (Profile) args.get("profile");
        if (p.getName() != null) {
            ((TextView) rootView.findViewById(R.id.name)).setText(p.getName());
        }
        else {
            ((TextView) rootView.findViewById(R.id.name)).setText("NAME IS NULL");
        }
        if (p.getOccupation() != null) {
            ((TextView) rootView.findViewById(R.id.occupation)).setText(p.getOccupation());
        }
        else {
            ((TextView) rootView.findViewById(R.id.occupation)).setText("OCCUPATION IS NULL");
        }
        if (p.getEmail() != null) {
            ((TextView) rootView.findViewById(R.id.email)).setText(p.getEmail());
        }
        else {
            ((TextView) rootView.findViewById(R.id.email)).setText("EMAIL IS NULL");
        }
        if (p.getPhone() != null) {
            ((TextView) rootView.findViewById(R.id.phone)).setText(p.getPhone());
        }
        else {
            ((TextView) rootView.findViewById(R.id.phone)).setText("PHONE IS NULL");
        }

        //query for profile picture (using id) here
        int [] images = {R.drawable.face1, R.drawable.face2, R.drawable.face3, R.drawable.face4};

        int selected = images[p.getName().charAt(0) % 4];

        ((ImageView) rootView.findViewById(R.id.image)).setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(getResources(), selected)));
        return rootView;
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
