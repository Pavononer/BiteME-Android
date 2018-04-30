package com.example.arturopavon.finalproject;

/**
 * Created by arturopavon on 4/9/18.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import java.util.List;


/**
 * Created by arturopavon on 4/7/18.
 */



public class CustomListAdapter extends ArrayAdapter<String> {

    private Activity Context;
    private List<String> username = new ArrayList<>();
    private ArrayList<Float> distances = new ArrayList<>();
    //private final Integer [] userImages;


    public CustomListAdapter(Activity context, List<String> username,  ArrayList<Float> distances) {
        super(context, R.layout.listuser, username);
        this.Context = context;
        this.username = username;
        //this.userImages = userImages;
        this.distances = distances;
        Log.d("User", username.toString());
        Log.d("msg", "I got here");
    }



    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = Context.getLayoutInflater();
        View ListViewSingle = inflater.inflate(R.layout.listuser, null, true);
        TextView ListViewItems =  ListViewSingle.findViewById(R.id.user);
        ImageView l =ListViewSingle.findViewById(R.id.imageView8);
        l.setImageResource(R.drawable.location);
        Log.d("Position in List", String.valueOf(position));
        //ImageView ListViewImage =  ListViewSingle.findViewById(R.id.userimage);
        TextView ListViewAuthors = ListViewSingle.findViewById(R.id.distance);
        ListViewItems.setText(username.get(position));

        SqlHelper db = new SqlHelper(getContext());
        ImageView im1 = ListViewSingle.findViewById(R.id.circularImageView2);
        im1.setImageURI(null);
        String iS = db.getImage(username.get(position));
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

        //ListViewImage.setImageResource(userImages[position]);
        ListViewAuthors.setText(String.format("%.2f",distances.get(position)));
        return ListViewSingle;
    }

}


