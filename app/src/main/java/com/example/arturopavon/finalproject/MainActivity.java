package com.example.arturopavon.finalproject;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private FusedLocationProviderClient mFusedLocationClient1;

    final String usernames[] = {};
    //final String distances [] = {"1", "20"};
    //final Integer userImages [] = {R.drawable.logo, R.drawable.logo};
    final String Password[] = {};
    final Double Longitude[] = {-87.6023423, -86.5023243};
    final Double Latitude[] = {41.9332121, 42.191283898};
    Location a = new Location("Arturo");
    Location b = new Location("Babouche");
    final Location location[] = {a, b};
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_DOCUMENTS};
        for(String permission: permissions){
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permission)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            permissions, 0);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }



        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        ImageView logo = findViewById(R.id.imageView4);
        logo.setImageResource(R.drawable.logo);
        SqlHelper db = new SqlHelper(this);

        //Add some locations for the users
        a.setLatitude(Latitude[0]);
        a.setLongitude(Longitude[0]);
        b.setLatitude(Latitude[1]);
        b.setLongitude(Longitude[1]);

        Intent intentR = getIntent();
        String name = intentR.getStringExtra("Name");


        // Check for the freshest data.

        ImageView im1 = findViewById(R.id.circularImageView2);
        im1.setImageURI(null);
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
            ParcelFileDescriptor parcelFileDescriptor = null;
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


        ArrayList<String> notifications = db.getNotifications(name);
        if(notifications.size()>0) {
            ImageView im = findViewById(R.id.bell);
            ImageView c = findViewById(R.id.circleN);
            TextView tx = findViewById(R.id.nNot);
            im.setVisibility(View.VISIBLE);
            c.setVisibility(View.VISIBLE);
            tx.setVisibility(View.VISIBLE);
            tx.setText(String.valueOf(notifications.size()));
        }

       // db.addUser(new User("Babouche", "Babouche", location[1]), this);
       // db.addUser(new User("Arturo", "Arturo", location[0]), this);


    }

    public void preferences(View view) {
        Intent intentR = getIntent();
        Intent intent = new Intent(getApplicationContext(), UserPreferences.class);
        String name = intentR.getStringExtra("Name");
        Log.d("User to change Preferences", name);
        intent.putExtra("Name", name);
        startActivity(intent);
    }
    public void notifications(View view){
        Intent i = getIntent();
        Intent intentR = new Intent(getApplicationContext(), Notifications.class);
        String name = i.getStringExtra("Name");
        Log.d("User to Notify", name);
        intentR.putExtra("Name", name);
        startActivity(intentR);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30000); // Update location every second

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("", "GoogleApiClient connection has been suspend");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("", "GoogleApiClient connection has failed");
    }
    @Override
    public void onLocationChanged(Location location) {
        Intent i1 = getIntent();
        String username = i1.getStringExtra("Name");
        SqlHelper db = new SqlHelper(this);
        final List<User> users = db.getAllUsers();
        final ArrayList<Float> Distances = new ArrayList<>();
        final ArrayList<String> usernames = new ArrayList<>();
        Log.d("Distance size", String.valueOf(Distances.size()));
        Log.d("Userlist size with user", String.valueOf(users.size()));
        for (int i = 0; i < users.size(); i++) {
            Log.d("User", users.get(i).getUsername());
            Log.d("User", username);
            Log.d("Username same",String.valueOf(users.get(i).equals(username)));
            if(users.get(i).getUsername().equals(username)){
                users.remove(i);
                --i;
                continue;
            }
            usernames.add(users.get(i).getUsername());
        }
        Log.d("Userlist size without user", String.valueOf(usernames.size()));
        for(int i = 0; i<users.size(); i++){
            Distances.add(location.distanceTo(users.get(i).getLocation()));
        }
        Log.d("Number of distances", String.valueOf(Distances.size()));
        CustomListAdapter adapter = new CustomListAdapter(this, usernames, Distances);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i2 = getIntent();
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                TextView user = findViewById(R.id.user);
                intent.putExtra("Name", i2.getStringExtra("Name"));
                Log.d("User in app", i2.getStringExtra("Name"));
                intent.putExtra("NameM", usernames.get(position));
                Log.d("Position", String.valueOf(position));
                Log.d("User clicked", user.getText().toString());
                startActivity(intent);
            }
        });
    }
}
