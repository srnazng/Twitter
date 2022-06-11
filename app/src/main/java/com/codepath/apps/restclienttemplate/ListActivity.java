package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ListActivity extends AppCompatActivity {
    User user;
    String label;
    TextView tvListTitle;
    TextView tvListScreenName;
    RecyclerView rvUsers;
    UserAdapter adapter;
    List<User> followers;
    List<User> following;

    public final String TAG = "ListActivity";

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        client = TwitterApp.getRestClient(this);

        tvListTitle = findViewById(R.id.tvListTitle);
        tvListScreenName = findViewById(R.id.tvListScreenName);
        rvUsers = findViewById(R.id.rvUsers);

        user = Parcels.unwrap(getIntent().getParcelableExtra("User"));
        tvListScreenName.setText("@" + user.getScreenName());
        label = getIntent().getStringExtra("label");
        tvListTitle.setText(label);

        if(label.equals("Following")){
            following = new ArrayList<>();
            adapter = new UserAdapter(this, following);
            populateFollowing();
        }
        else{
            followers = new ArrayList<>();
            adapter = new UserAdapter(this, followers);
            populateFollowers();
        }

        rvUsers.setLayoutManager(new LinearLayoutManager((this)));
        rvUsers.setAdapter(adapter);
    }

    public void populateFollowers(){
        client.getFollowers(user.getScreenName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    followers.addAll(User.fromJsonArray(jsonObject.getJSONArray("users")));
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "onSuccess followers" + jsonObject);
                } catch (JSONException e) {
                    Log.e(TAG, "get followers error", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure followers", throwable);
            }
        });
    }

    public void populateFollowing(){
        client.getFollowing(user.getScreenName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    following.addAll(User.fromJsonArray(jsonObject.getJSONArray("users")));
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "onSuccess following");
                } catch (JSONException e) {
                    Log.e(TAG, "get following error", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure following", throwable);
            }
        });
    }
}