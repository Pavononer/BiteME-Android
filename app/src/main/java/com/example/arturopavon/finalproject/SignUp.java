package com.example.arturopavon.finalproject;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturopavon on 4/16/18.
 */

public class SignUp extends Activity {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Context context;
    private int exist;
    private int empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        exist =0;
        setContentView(R.layout.signup);
        EditText user = this.findViewById(R.id.userName);
        EditText password = this.findViewById(R.id.userPass);
        Button button = this.findViewById(R.id.submitBtn);
        button.setText("Sign Up");
        ImageView logo = this.findViewById(R.id.imageView);
        logo.setImageResource(R.drawable.logo);
        context = this;

    }

    public void login(View view) {

        final SqlHelper db = new SqlHelper(this);
        final EditText user = this.findViewById(R.id.userName);
        EditText password = this.findViewById(R.id.userPass);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        final String usern = user.getText().toString();
        final String passn = password.getText().toString();
        if (usern.toString().toString().equals("") || passn.toString().toString().equals("")){
            empty = 1;
        }
        Log.d("Empty field", String.valueOf(empty));
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

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (usern.toString().toString().equals("") || passn.toString().toString().equals("")){
                            empty = 1;
                            return;
                        }
                        if(empty == 0){
                            List<User> u = db.getAllUsers();
                            Log.d("Empty is", String.valueOf(empty));
                            for(int i = 0; i<u.size(); i++){
                                if(u.get(i).getUsername().toString().equals(usern.toString())){
                                    Log.d("Repeated", String.valueOf(u.get(i).getUsername().toString().equals(usern.toString())));
                                    exist = 1;
                                }
                            }
                            if(exist ==1){
                                Context context = getApplicationContext();
                                CharSequence text = "User already exists";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                return;
                            }else {
                                Log.d("Exist is", String.valueOf(exist));
                                db.addUser(new User(usern, passn, location), context);
                            }
                        }else{
                            return;
                        }

                    }
                });
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }

            ;
        };
        if(empty == 1) {
            Context context = getApplicationContext();
            CharSequence text = "Blank user or password";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            empty = 0;
        }else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        /**
         EditText user = this.findViewById(R.id.userName);
         EditText password = this.findViewById(R.id.userPass);
         SqlHelper db = new SqlHelper(this);
         db.addUser(user, password);
         Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
         startActivity(intent);
         */
    }
}

