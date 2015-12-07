package com.share;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.example.james.sharedclasses.LoginUtils;
import com.example.james.sharedclasses.Profile;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;

    private View.OnClickListener editListener = new View.OnClickListener() {
        public void onClick(View v) {
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

//        SharedPreferences mPrefs = getSharedPreferences(getString(R.string.USER_DATA), Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        JsonParser parser=new JsonParser();
//
//        Profile myProfile = gson.fromJson(parser.parse(mPrefs.getString("profile", "")).getAsJsonObject(), Profile.class);
        Profile myProfile = LoginUtils.getProfile(this);
        // Set our profile information.
                ((TextView) findViewById(R.id.name)).setText(myProfile.getName());
        ((TextView) findViewById(R.id.occupation)).setText(myProfile.getOccupation());
        ((TextView) findViewById(R.id.phone)).setText(myProfile.getPhone());
        ((TextView) findViewById(R.id.email)).setText(myProfile.getEmail());

        ((ImageView) findViewById(R.id.image)).setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face2)));
        Button button = (Button)findViewById(R.id.edit);
        button.setOnClickListener(editListener);

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Profile");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Files");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("Contacts");

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                .withSelectedItemByPosition(0)
                .withToolbar(toolbar)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent i;
                        switch (position) {
                            case 0:
                                Log.d("Hamburger", "Clicked profile");
                                break;
                            case 2:
                                Log.d("Hamburger", "Clicked files");
                                i = new Intent(getApplicationContext(), MyFilesActivity.class);
                                startActivity(i);
                                break;
                            case 4:
                                Log.d("Hamburger", "Clicked contact");
                                i = new Intent(getApplicationContext(), ContactsListActivity.class);
                                startActivity(i);
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.image);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(getCroppedBitmap(BitmapFactory.decodeFile(imgDecodableString)));
                //InputStream i = BitmapFactory.decodeFile(imgDecodableString);
                InputStream i = getContentResolver().openInputStream(data.getData());
                this.upload(i, LoginUtils.getLoginToken(this.getApplicationContext()));
            }
        } catch (Exception e) {
            Toast.makeText(this, "oops! try again", Toast.LENGTH_LONG).show();
        }
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

    public static void upload(final InputStream inputStream, final String publicId) {

        final Map<String, String> options = new HashMap<>();
        options.put("public_id", publicId);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Map config = new HashMap();
                config.put("cloud_name", "sneha-sankavaram");
                config.put("api_key", "534326311652891");
                config.put("api_secret", "8EzcaEKA8Fg7899KF5N_LCPArws");
                Cloudinary cloudinary = new Cloudinary(config);

                try {
                    cloudinary.uploader().upload(inputStream, options);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();
    }




}
