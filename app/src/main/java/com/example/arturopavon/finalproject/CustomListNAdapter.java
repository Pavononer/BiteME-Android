package com.example.arturopavon.finalproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by arturopavon on 4/27/18.
 */

public class CustomListNAdapter extends ArrayAdapter<String> {
    private Activity Context;
    private ArrayList<String> users = new ArrayList<String>();
    private ArrayList<String> times = new ArrayList<String>();
    private ArrayList<String> restaurants = new ArrayList<String>();
    //private final Integer [] userImages;


    public CustomListNAdapter(Activity context, ArrayList<String> users,  ArrayList<String> times, ArrayList<String> restaurants) {
        super(context, R.layout.listuser, users);
        this.Context = context;
        this.users = users;
        //this.userImages = userImages;
        this.times = times;
        this.restaurants = restaurants;
    }



    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = Context.getLayoutInflater();
        View ListViewSingle = inflater.inflate(R.layout.nlist, null, true);
        TextView restaurant =  ListViewSingle.findViewById(R.id.user);
        TextView user =  ListViewSingle.findViewById(R.id.user2);
        TextView time = ListViewSingle.findViewById(R.id.user3);
        ImageView l =ListViewSingle.findViewById(R.id.imageView8);
        ImageView c1 = ListViewSingle.findViewById(R.id.circle1);
        c1.setImageResource(R.drawable.indigo);
        ImageView c2 = ListViewSingle.findViewById(R.id.circle2);
        c2.setImageResource(R.drawable.indigo);
        ImageView r = ListViewSingle.findViewById(R.id.imageView11);
        r.setImageResource(R.drawable.restaurant);
        ImageView t = ListViewSingle.findViewById(R.id.imageView12);
        t.setImageResource(R.drawable.time);
        ImageView v = ListViewSingle.findViewById(R.id.imageView9);
        v.setImageResource(R.drawable.check);
        ImageView x = ListViewSingle.findViewById(R.id.imageView10);
        x.setImageResource(R.drawable.cross);
        restaurant.setText(restaurants.get(position));


        SqlHelper db = new SqlHelper(getContext());
        ImageView im1 = ListViewSingle.findViewById(R.id.circularImageView2);
        String iS = db.getImage(users.get(position));
        Log.d("ImageR", iS);
        //
        // Uri uri = Uri.parse(image);
        //Log.d("Equals", String.valueOf(iS.equals(String.valueOf(R.drawable.imageuser))));
        if(iS.equals(String.valueOf(R.drawable.imageuser))){
            im1.setImageResource(Integer.parseInt(iS));
        }else{
            Uri uri = Uri.parse(iS);
            ContentResolver contentResolver= getContext().getContentResolver();
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

        user.setText(users.get(position));
        time.setText(times.get(position));
        //ListViewImage.setImageResource(userImages[position]);
        return ListViewSingle;
    }
}
