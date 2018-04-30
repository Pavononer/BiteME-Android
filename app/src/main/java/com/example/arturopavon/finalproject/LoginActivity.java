package com.example.arturopavon.finalproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * Created by arturopavon on 4/9/18.
 */

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
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

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    public void login(View view)
    {
        EditText user = this.findViewById(R.id.userName);
        EditText password = this.findViewById(R.id.userPass);
        SqlHelper db = new SqlHelper(this);
        List<User> users = db.getAllUsers();
        String [] usernames = new String[users.size()];
        String [] Password = new String[users.size()];
        int failed = 0;
        for (int i = 0; i<users.size();i++){
            usernames[i] = users.get(i).getUsername();
            Password[i] = users.get(i).getPassword();
            if (user.getText().toString().equals(usernames[i].toString()) && password.getText().toString().equals(Password[i].toString())){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("Name", user.getText().toString());
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
            else{
                failed = failed + 1;
            }
        }
        if(failed == usernames.length){
            Context context = getApplicationContext();
            CharSequence text = "Incorrect username or password";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
    public void register(View view){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}

