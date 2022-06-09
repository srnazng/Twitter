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
    public boolean favorited;
    public boolean retweeted;

    // empty constructor needed by Parceler library
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.created_at = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getString("id");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");

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

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }
}
