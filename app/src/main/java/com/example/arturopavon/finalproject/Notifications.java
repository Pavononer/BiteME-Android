package com.example.arturopavon.finalproject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by arturopavon on 4/27/18.
 */

public class Notifications extends Activity {
    private int positionC;
    private ArrayList<String> users = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private ArrayList<String> restaurants = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.notifications);
        ImageView im = findViewById(R.id.logon);
        im.setImageResource(R.drawable.logone);

        Intent i = getIntent();
        String username = i.getStringExtra("Name");
        SqlHelper db = new SqlHelper(this);

        ImageView im1 = findViewById(R.id.circularImageView2);
        String iS = db.getImage(username);
        Log.d("ImageR", iS);
        //
        // Uri uri = Uri.parse(image);
        //Log.d("Equals", String.valueOf(iS.equals(String.valueOf(R.drawable.imageuser))));
        if(iS.equals(String.valueOf(R.drawable.imageuser))){
            im1.setImageResource(Integer.parseInt(iS));
        }else{
            Uri uri = Uri.parse(iS);
            ContentResolver contentResolver= getContentResolver();
            ParcelFileDescriptor parcelFileDescriptor =
                    null;
            try {
                parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            try {
                parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            im1.setImageBitmap(image);
        }

        Log.d("User Receiver", username);
        users = db.getNotUsers(username);
        times = db.getNotTimes(username);
        restaurants = db.getNotRestaurants(username);
        CustomListNAdapter adapter = new CustomListNAdapter(this, users, times, restaurants);
        ListView listView = findViewById(R.id.nlist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                positionC = position;
            }
        });
    }
    public void accept(View v){
        Intent i = getIntent();
        String username = i.getStringExtra("Name");
        SqlHelper db = new SqlHelper(this);
        db.deleteNotification(username, users.get(positionC), times.get(positionC), restaurants.get(positionC));
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Intent i2 = getIntent();
        intent.putExtra("Name", i2.getStringExtra("Name"));
        startActivity(intent);
    }
    public void decline(View v){
        Intent i = getIntent();
        String username = i.getStringExtra("Name");
        SqlHelper db = new SqlHelper(this);
        db.deleteNotification(username, users.get(positionC), times.get(positionC), restaurants.get(positionC));
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Intent i2 = getIntent();
        intent.putExtra("Name", i2.getStringExtra("Name"));
        startActivity(intent);
    }
    public void preferences(View v){
        Intent i = getIntent();
        String username = i.getStringExtra("Name");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("Name", i.getStringExtra("Name"));
        startActivity(intent);
    }
}
