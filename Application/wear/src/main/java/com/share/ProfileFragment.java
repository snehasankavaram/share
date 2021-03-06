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
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements RetrieveMyProfileTaskListener {
    Profile p;
    TextView name;
    TextView phone;
    TextView occupation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name = (TextView) view.findViewById(R.id.contact_name);
        phone = (TextView) view.findViewById(R.id.contact_phone);
        occupation = (TextView) view.findViewById(R.id.contact_occupation);

        Bundle args = getArguments();
        if (args != null) {
            p = (Profile) args.get("profile");
            if (p != null) {
                name.setText(p.getName());
                phone.setText("(925)-351-1211");
                occupation.setText(p.getOccupation());
            }
        }

        //query for profile picture (using id) here
        int [] images = {R.drawable.face1, R.drawable.ben_bodien, R.drawable.face3, R.drawable.laura_lee};

        int selected = images[name.getText().charAt(0) % 4];

        if (p != null && p.getName() != null) {
            String uri = String.format("@drawable/%s",p.getName().replace(" ", "_").toLowerCase());
            int imageResource = getActivity().getResources().getIdentifier(uri, null, this.getActivity().getPackageName());
            if (imageResource != 0) {
                selected = imageResource;
            }
        }

        ((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(getResources(), selected)));
        return view;
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

    @Override
    public void onProfileAvailable(Profile profile) {
        name.setText(p.getName());
        phone.setText(p.getPhone());
        occupation.setText(p.getOccupation());
    }
}
