package com.example.arturopavon.finalproject;

import android.location.Location;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturopavon on 4/16/18.
 */

public class User {

    private String username;
    private int id;
    private String password;
    private Location location;
    private ImageView userimage;
    private List<String> usertime = new ArrayList<String>();
    private List<String> restaurants = new ArrayList<String>();

    public User(String username, String password ){
        this.username = username;
        this.password = password;
        this.location = getLocation();
    }
    public User(String username, String password, Location location, ImageView userimage, List<String> usertime, List<String> restaurants) {
        this.username = username;
        this.password = password;
        this.location = location;
        this.userimage = userimage;
        this.usertime = usertime;
        this.restaurants = restaurants;
    }

    public User(){

    }

    public User(String name, String password, Location location) {
        this.username = name;
        this.password = password;
        this.location = location;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setUserimage(ImageView userimage) {
        this.userimage = userimage;
    }

    public void setUsertime(List<String> usertime) {
        this.usertime = usertime;
    }

    public void setRestaurants(List<String> restaurants) {
        this.restaurants = restaurants;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Location getLocation() {
        return location;
    }

    public ImageView getUserimage() {
        return userimage;
    }

    public List<String> getUsertime() {
        return usertime;
    }

    public List<String> getRestaurants() {
        return restaurants;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
