package com.example.arturopavon.finalproject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by arturopavon on 4/9/18.
 */

public class UserActivity extends Activity {
    private String restaurantS;
    private String timeS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //final String[] restaurants = {"iHop", "Au Cheval"};
        //final String[] times = {"1.50", "2.10"};
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.user);

        TextView tv = findViewById(R.id.description);
        Intent i2 = getIntent();
        tv.setText(i2.getStringExtra("NameM"));

        SqlHelper db = new SqlHelper(this);

        String name = i2.getStringExtra("NameM");
        ImageView im1 = findViewById(R.id.circularImageView3);
        String iS = db.getImage(name);
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
        int id = db.getIdbyusername(i2.getStringExtra("NameM"));
        final List<String> restaurants = db.getRest(String.valueOf(id));
        final List<String> times = db.getTime(String.valueOf(id));

        final Spinner time = findViewById(R.id.time);
        ArrayAdapter a1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,restaurants);
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(a1);

        final Spinner restaurant = findViewById(R.id.restaurant);
        ArrayAdapter a2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,times);
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurant.setAdapter(a2);


        restaurant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                int item = restaurant.getSelectedItemPosition();
                restaurantS = restaurants.get(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                int item = time.getSelectedItemPosition();
                timeS = times.get(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }
    public void meet(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Intent i2 = getIntent();
        Log.d("User", i2.getStringExtra("Name"));
        intent.putExtra("Name", i2.getStringExtra("Name"));



        if(timeS.equals("No times selected!")||restaurantS.equals("No restaurants selected!")){
            Context context = getApplicationContext();
            CharSequence text = "User has no times/restaurants selected";

            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            SqlHelper db = new SqlHelper(this);
            db.addNotification(i2.getStringExtra("NameM"), i2.getStringExtra("Name"), timeS, restaurantS);
            startActivity(intent);
        }

    }

}
