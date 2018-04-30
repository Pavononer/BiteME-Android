package com.example.arturopavon.finalproject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.accounts.AccountManager.KEY_PASSWORD;

/**
 * Created by arturopavon on 4/16/18.
 */

public class SqlHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 16;
    // Database Name
    private static final String DATABASE_NAME = "UserDB";
    // Books table name
    private static final String KEY_ID = "id";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_RESTAURANTS = "restaurants";
    private static final String TABLE_TIMES = "times";
    private static final String TABLE_NOTIFICATIONS = "notifications";
    private static final String TABLE_IMAGES = "images";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_RESTAURANT = "restaurant";
    private static final String KEY_TIME = "time";
    private static final String KEY_IMAGE = "image";





    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE users ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "username TEXT, "+
                "password TEXT, "+"longitude TEXT,"+"latitude TEXT"+" )";
        db.execSQL(create);
        create = "CREATE TABLE restaurants ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "userid TEXT, "+
                "restaurants TEXT"+" )";
        db.execSQL(create);
        create = "CREATE TABLE times ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "userid TEXT, "+
                "times TEXT"+" )";
        db.execSQL(create);
        create = "CREATE TABLE notifications ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "userR TEXT, "+
                "userS TEXT, "+"timeM TEXT, "+"restM TEXT "+" )";
        db.execSQL(create);
        create = "CREATE TABLE images ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "user TEXT, "+
                "image TEXT"+" )";
        db.execSQL(create);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS restaurants");
        db.execSQL("DROP TABLE IF EXISTS times");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        db.execSQL("DROP TABLE IF EXISTS images");
        this.onCreate(db);
    }

    public void addUser(User user, Context context){
        Log.d("addUser", user.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_LONGITUDE, String.valueOf(user.getLocation().getLongitude()));
        values.put(KEY_LATITUDE, String.valueOf(user.getLocation().getLatitude()));
        //values.put(KEY_IMAGE, user.getUserimage().toString());
        db.insert(TABLE_USERS, null, values);

        SqlHelper dbl = new SqlHelper(context);
        int id = dbl.getIdbyusername(user.getUsername());
        Log.d("Id", String.valueOf(id));

        ContentValues valuesR = new ContentValues();
        valuesR.put("restaurants", "No restaurants selected!");
        valuesR.put("userid", String.valueOf(id));
        db.insert(TABLE_RESTAURANTS, null, valuesR);

        ContentValues valuesT = new ContentValues();
        valuesT.put("times", "No times selected!");
        valuesT.put("userid", String.valueOf(id));
        db.insert(TABLE_TIMES, null, valuesT);

        ContentValues valuesI = new ContentValues();
        Resources resources = context.getResources();
        Uri uri = Uri.parse("android.resource://"+context.getPackageName()+"/drawable/logo.png");
        Drawable drawable = resources.getDrawable(context.getResources()
                .getIdentifier("imageuser", "drawable", context.getPackageName()));
        String image = drawable.toString();
        valuesI.put("image", String.valueOf(R.drawable.imageuser));
        valuesI.put("user", user.getUsername());
        db.insert(TABLE_IMAGES, null, valuesI);

        db.close();
    }

    public int updateUser(User user, String newusername, String newpassword, Location newlocation, ImageView newuserimage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newusername);
        values.put("password", newpassword);
        values.put("latitude", String.valueOf(newlocation.getLatitude()));
        values.put("longitude", String.valueOf(newlocation.getLongitude()));
        //values.put(KEY_IMAGE, newuserimage.toString());
        int i = db.update(TABLE_USERS, values, KEY_ID+" = ?", new String[] {String.valueOf(user.getId())});
        db.close();
        Log.d("UpdateUser", user.toString());
        return i;
    }

    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID+" = ?", new String[] {String.valueOf(user.getId())});
        db.close();
        Log.d("deleteUser", user.getUsername());
    }

    public int getIds() {
        String selectQuery = "SELECT id FROM users";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        c.moveToFirst();
        int total = c.getCount();
        return total;
    }

    public int getIdbyusername(String userName){
        String selectQuery = "SELECT id FROM users WHERE username=?";
        Cursor cursor = null;
        int userid = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            cursor = database.rawQuery(selectQuery, new String[] {userName + ""});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                userid = cursor.getInt(cursor.getColumnIndex("id"));
            }
            return userid;
        }finally {
            cursor.close();
        }

    }
    public void addRest(String userid, String restaurant){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesr = new ContentValues();
        valuesr.put("restaurants", restaurant);
        valuesr.put("userid", userid);
        db.insert(TABLE_RESTAURANTS, null, valuesr);
        db.close();

    }
    public void addTime(String userid, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuest = new ContentValues();
        valuest.put("times", time);
        valuest.put("userid",userid);

        db.insert(TABLE_TIMES, null, valuest);
        db.close();

    }
    public void deleteR(String userid, String restaurant){
        SQLiteDatabase db = this.getWritableDatabase();
        String useridr = "userid";
        String restaurantd = "restaurants";
        db.delete(TABLE_RESTAURANTS, useridr + "=? AND " + restaurantd + "=?", new String[] {userid, restaurant});
        db.close();

    }

    public void deleteT(String userid, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        String useridt = "userid";
        String timed = "times";
        db.delete(TABLE_TIMES, useridt + "=? AND " + timed + "=?", new String[] {userid, time});
        db.close();

    }

    public ArrayList<String> getRest(String userid){
        String selectQuery = "SELECT restaurants FROM restaurants WHERE userid =?";
        Cursor cursor = null;
        ArrayList<String> resto= new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            cursor = database.rawQuery(selectQuery, new String[] {userid+ ""});
            Log.d("Cursor length", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                Log.d("Cursor rest",cursor.getString(0) );
                resto.add(cursor.getString(0));
            }while(cursor.moveToNext());
            cursor.close();
            database.close();
        }catch(Exception e){
            Log.d("Tag", "I failed");
        }
        finally {
            Log.d("Tag", "I ended");
            return resto;
        }

    }
    public ArrayList<String> getTime(String userid){
        String selectQuery = "SELECT times FROM times WHERE userid =?";
        Cursor cursor = null;
        ArrayList<String> resto= new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            cursor = database.rawQuery(selectQuery, new String[] {userid+ ""});
            Log.d("Cursor length", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                Log.d("Cursor tim",cursor.getString(0) );
                resto.add(cursor.getString(0));
            }while(cursor.moveToNext());
            cursor.close();
            database.close();
        }catch(Exception e){
            Log.d("Tag", "I failed");
        }
        finally {
            Log.d("Tag", "I ended");
            return resto;
        }
    }

    public final List<User> getAllUsers() {
        List<User> users = new LinkedList<User>();
        String query = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                Double longitude = Double.parseDouble(cursor.getString(3));
                Double latitude =Double.parseDouble(cursor.getString(4));
                Location l = new Location(cursor.getString(1));
                l.setLongitude(longitude);
                l.setLatitude(latitude);
                user.setLocation(l);
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("getAllUsers()", users.toString());
        return users;
    }

    public void addNotification(String userR, String userS, String timeM, String restM){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timeM", timeM);
        values.put("userR",userR);
        values.put("userS", userS);
        values.put("restM", restM);
        db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();

    }
    public void deleteNotification(String userR, String userS, String timeM, String restM){
        SQLiteDatabase db = this.getWritableDatabase();
        String userRid = "userR";
        String userSid = "userS";
        String restMid = "restM";
        String timeMid = "timeM";
        db.delete(TABLE_NOTIFICATIONS, userRid + "=? AND " + userSid + "=? AND " + restMid + "=? AND " + timeMid + "=?", new String[] {userR, userS, restM, timeM});
        db.close();

    }
    public ArrayList<String> getNotifications(String user){
        String selectQuery = "SELECT id FROM notifications WHERE userR =?";
        Cursor cursor = null;
        ArrayList<String> resto= new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            cursor = database.rawQuery(selectQuery, new String[] {user+ ""});
            Log.d("CursorN length", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                Log.d("Cursor N",cursor.getString(0) );
                resto.add(cursor.getString(0));
            }while(cursor.moveToNext());
            cursor.close();
            database.close();
        }catch(Exception e){
            Log.d("Tag", "I failed");
        }
        finally {
            Log.d("Tag", "I ended");
            return resto;
        }
    }
    public String[] getInformation(String notificationid){
        String[] values = new String[3];
        String selectQuery = "SELECT userS, restM, timeM FROM notifications WHERE id =?";
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            cursor = database.rawQuery(selectQuery, new String[] {notificationid+ ""});
            Log.d("CursorN length", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                Log.d("Cursor U",cursor.getString(0) );
                Log.d("Cursor R",cursor.getString(1) );
                Log.d("Cursor T",cursor.getString(2) );
                values[0] = cursor.getString(0);
                values[1] = cursor.getString(1);
                values[2] = cursor.getString(2);
            }while(cursor.moveToNext());
            cursor.close();
            database.close();
        }catch(Exception e){
            Log.d("Tag", "I failed");
        }
        finally {
            Log.d("Tag", "I ended");
            return values;
        }
    }
    public ArrayList<String> getNotUsers(String userM){
        ArrayList<String> values = new ArrayList<String>();
        String selectQuery = "SELECT userS FROM notifications WHERE userR =?";
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();

        try {
            cursor = database.rawQuery(selectQuery, new String[] {userM+ ""});
            Log.d("CursorN length", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                Log.d("Cursor U",cursor.getString(0) );
                values.add(cursor.getString(0));
            }while(cursor.moveToNext());
            cursor.close();
            database.close();
        }catch(Exception e){
            Log.d("Tag", "I failed");
        }
        finally {
            Log.d("Tag", "I ended");
            return values;
        }
    }
    public ArrayList<String> getNotRestaurants(String userM){
        ArrayList<String> values = new ArrayList<String>();
        String selectQuery = "SELECT restM FROM notifications WHERE userR =?";
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();

        try {
            cursor = database.rawQuery(selectQuery, new String[] {userM+ ""});
            Log.d("CursorN length", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                Log.d("Cursor U",cursor.getString(0) );
                values.add(cursor.getString(0));
            }while(cursor.moveToNext());
            cursor.close();
            database.close();
        }catch(Exception e){
            Log.d("Tag", "I failed");
        }
        finally {
            Log.d("Tag", "I ended");
            return values;
        }
    }
    public ArrayList<String> getNotTimes(String userM){
        ArrayList<String> values = new ArrayList<String>();
        String selectQuery = "SELECT timeM FROM notifications WHERE userR =?";
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();

        try {
            cursor = database.rawQuery(selectQuery, new String[] {userM+ ""});
            Log.d("CursorN length", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                Log.d("Cursor U",cursor.getString(0) );
                values.add(cursor.getString(0));
            }while(cursor.moveToNext());
            cursor.close();
            database.close();
        }catch(Exception e){
            Log.d("Tag", "I failed");
        }
        finally {
            Log.d("Tag", "I ended");
            return values;
        }
    }
    public void addImage(String user, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user", user);
        values.put("image",image);
        db.insert(TABLE_IMAGES, null, values);
        db.close();
    }
    public void updateImage(String user, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String userId = "user";
        values.put("user", user);
        values.put("image",image);
        db.update(TABLE_IMAGES, values, userId+" = ?", new String[] {user+""});
        db.close();
    }
    public String getImage(String user){
        String image = "";
        String selectQuery = "SELECT image FROM images WHERE user =?";
        Cursor cursor = null;
        SQLiteDatabase database = this.getReadableDatabase();
        try {
            cursor = database.rawQuery(selectQuery, new String[] {user+ ""});
            Log.d("CursorIM length", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                Log.d("Cursor IM",cursor.getString(0) );
                image = cursor.getString(0);
            }while(cursor.moveToNext());
            cursor.close();
            database.close();
        }catch(Exception e){
            Log.d("Tag", "I failed");
        }
        finally {
            Log.d("Tag", "I ended");
            return image;
        }
    }
}


