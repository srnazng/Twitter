package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public String created_at;
    public User user;
    public String media_url;
    public String id;
    public String in_reply_to_screen_name;

    // empty constructor needed by Parceler library
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.created_at = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getString("id");

        if(jsonObject.getJSONObject("entities").has("media")){
            tweet.media_url = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
        }
        else{
            tweet.media_url = "";
        }

        if(jsonObject.has("in_reply_to_screen_name") && !jsonObject.get("in_reply_to_screen_name").toString().equals("null")){
            tweet.in_reply_to_screen_name = jsonObject.get("in_reply_to_screen_name").toString();
            Log.i("Tweet", tweet.in_reply_to_screen_name);
        }
        else{
            tweet.in_reply_to_screen_name = "";
        }

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public User getUser() {
        return user;
    }

    public String getId() { return id; }

    public String getMedia_url() {
        return media_url;
    }
}
