package com.example.arturopavon.finalproject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturopavon on 4/22/18.
 */

public class UserPreferences extends Activity {

    private String restaurantS;
    private String timeS;
    private ArrayList<String> restaurants = new ArrayList<String>();
    private ArrayList<String> times = new ArrayList<String>();
    private ArrayAdapter<String> a1;
    private ArrayAdapter<String> a2;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        //restaurants.add("iHop");
        //restaurants.add("Au Cheval");
        //times.add("10.10");
        //times.add("10.30");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefences);
        Intent i = getIntent();
        TextView tx = findViewById(R.id.description);
        tx.setText(i.getStringExtra("Name"));
        SqlHelper db = new SqlHelper(this);

        String name = i.getStringExtra("Name");
        ImageView im1 = findViewById(R.id.circularImageView);
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


        String usn = i.getStringExtra("Name");
        int id = db.getIdbyusername(usn);
        Log.d("Username", usn);
        Log.d("Userid", String.valueOf(id));
        restaurants = db.getRest(String.valueOf(id));
        times = db.getTime(String.valueOf(id));

        Log.d("Restaurants", String.valueOf(restaurants.size()));
        Log.d("Times", String.valueOf(times.size()));

        final Spinner time = findViewById(R.id.spinner);
        final Spinner restaurant = findViewById(R.id.spinner2);

        a1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,restaurants);
        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(a1);

        a2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,times);
        a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
    public void save(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Intent i2 = getIntent();
        intent.putExtra("Name", i2.getStringExtra("Name"));
        Context context = getApplicationContext();
        CharSequence text = "Changes saved";

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        startActivity(intent);
    }

    public void logout(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        Intent i2 = getIntent();

        Context context = getApplicationContext();
        CharSequence text = "Logged out successfully";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        startActivity(intent);
    }

    public void addR(View view){

        EditText editText = findViewById(R.id.restaurantI);
        String restaurant = editText.getText().toString();

        if(restaurant.equals("")){
            Context context = getApplicationContext();
            CharSequence text = "Restaurant is blank";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            SqlHelper db = new SqlHelper(this);
            Intent i = getIntent();
            Log.d("First element", restaurants.get(0));
            Log.d("Boolean", String.valueOf(restaurants.get(0).equals("No restaurants selected!")));
            int id = db.getIdbyusername(i.getStringExtra("Name"));
            if(restaurants.get(0).equals("No restaurants selected!")){
                restaurants.remove(0);
                db.deleteR(String.valueOf(id), "No restaurants selected!");
            }
            restaurants.add(restaurant);
            String userid = String.valueOf(db.getIdbyusername(i.getStringExtra("Name")));
            db.addRest(userid, restaurant);

            a2.notifyDataSetChanged();

            Context context = getApplicationContext();
            CharSequence text = "Restaurant added.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            editText.clearFocus();
            editText.setText("");
        }

    }
    public void addT(View view){
        EditText editText = findViewById(R.id.timeI);
        String time = editText.getText().toString();

        if(time.equals("")){
            Context context = getApplicationContext();
            CharSequence text = "Time is blank.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            SqlHelper db = new SqlHelper(this);
            Intent i = getIntent();

            int id = db.getIdbyusername(i.getStringExtra("Name"));
            if(times.get(0).equals("No times selected!")){
                times.remove(0);
                db.deleteT(String.valueOf(id), "No times selected!");
            }
            times.add(time);
            String userid = String.valueOf(db.getIdbyusername(i.getStringExtra("Name")));
            db.addTime(userid, time);

            a2.notifyDataSetChanged();
            Context context = getApplicationContext();
            CharSequence text = "Time added.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            editText.clearFocus();
            editText.setText("");
        }


    }
    public void deleteR(View view){
        EditText editText = findViewById(R.id.restaurantI);
        String restaurant = editText.getText().toString();

        if(restaurant.equals("")){
            Context context = getApplicationContext();
            CharSequence text = "Restaurant is blank.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            SqlHelper db = new SqlHelper(this);
            Intent i = getIntent();
            String userid = String.valueOf(db.getIdbyusername(i.getStringExtra("Name")));
            db.deleteR(userid, restaurant);
            restaurants.remove(restaurant);

            if(restaurants.size()==0){
                restaurants.add("No restaurants selected!");
                db.addRest(userid, "No restaurants selected!");
            }

            a1.notifyDataSetChanged();

            Context context = getApplicationContext();
            CharSequence text = "Restaurant deleted.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            editText.clearFocus();
            editText.setText("");
        }

    }
    public void deleteT(View view){
        EditText editText = findViewById(R.id.timeI);
        String time = editText.getText().toString();

        if(time.equals("")){
            Context context = getApplicationContext();
            CharSequence text = "Time is blank.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            SqlHelper db = new SqlHelper(this);
            Intent i = getIntent();
            String userid = String.valueOf(db.getIdbyusername(i.getStringExtra("Name")));
            db.deleteT(userid, time);
            times.remove(time);

            if(times.size()==0){
                times.add("No times selected!");
                db.addTime(userid, "No times selected!");
            }

            a2.notifyDataSetChanged();

            Context context = getApplicationContext();
            CharSequence text = "Time deleted.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            editText.clearFocus();
            editText.setText("");
        }

    }
    public void pickImage(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getApplicationContext();
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                ImageView im = findViewById(R.id.circularImageView);
                uri = data.getData();
                im.setImageURI(uri);
                String image = uri.toString();


                Log.d("ImageS", image);

                SqlHelper db = new SqlHelper(this);
                Intent i = getIntent();
                String userid = i.getStringExtra("Name");
                db.updateImage(userid, image);

            }catch (Exception e){

            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }

}
