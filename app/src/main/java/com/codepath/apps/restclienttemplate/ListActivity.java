package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    String cursor;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        cursor = "-1";

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvUsers.addOnScrollListener(scrollListener);
    }

    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.

        if(label.equals("Following")){
            populateFollowing();
        }
        else{
            populateFollowers();
        }
    }

    public void populateFollowers(){
        client.getFollowers(user.getScreenName(), cursor, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    followers.addAll(User.fromJsonArray(jsonObject.getJSONArray("users")));
                    adapter.notifyDataSetChanged();
                    cursor = jsonObject.getString("next_cursor_str");
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
        client.getFollowing(user.getScreenName(), cursor, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    following.addAll(User.fromJsonArray(jsonObject.getJSONArray("users")));
                    adapter.notifyDataSetChanged();
                    cursor = jsonObject.getString("next_cursor_str");
                    Log.i(TAG, "onSuccess following" + jsonObject);
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