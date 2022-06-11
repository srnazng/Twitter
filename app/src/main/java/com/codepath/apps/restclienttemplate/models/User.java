package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class User {
    public String name;
    public String screenName;
    public String profileImageUrl;
    public int followersCount;
    public int friendsCount;

    public User(){}

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.followersCount = jsonObject.getInt("followers_count");
        user.friendsCount = jsonObject.getInt("friends_count");

        return user;
    }

    public static List<User> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<User> users = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            users.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return users;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }
}
