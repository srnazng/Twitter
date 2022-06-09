package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    public static final String TAG = "Timeline Activity";
    private final int REQUEST_CODE = 20;
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;

    private SwipeRefreshLayout swipeContainer;

    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        // find recycler view
        rvTweets = findViewById(R.id.rvTweets);

        // init list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);

        // recycler view setup: layout manager and adapter
        rvTweets.setLayoutManager(new LinearLayoutManager((this)));
        rvTweets.setAdapter(adapter);

        populateHomeTimeline();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.d(TAG, "refresh");
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void fetchTimelineAsync(int page) {
        adapter.clear();
        populateHomeTimeline();
        adapter.addAll(tweets);
        swipeContainer.setRefreshing(false);
    }

    private void populateHomeTimeline() {
        if(miActionProgressItem != null){
            showProgressBar();
        }

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    e.printStackTrace();
                }
                if(miActionProgressItem != null){
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure " + response, throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.compose:
                return compose();
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Log.i("onActivityResult", "inside");
            // get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            // update recycler view with new tweet
            tweets.add(0, tweet);// modify data source of tweets
            adapter.notifyItemInserted(0);// update adapter
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TimelineActivity.java
    void onLogoutButton() {
        // forget who's logged in
        TwitterApp.getRestClient(this).clearAccessToken();

        // navigate backwards to Login screen
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
        startActivity(i);
    }

    public void finish(View view) {
        onLogoutButton();
        client.clearAccessToken();
    }

    public boolean compose(){
        // navigate to the compose activity
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);

        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
        Log.d(TAG, "show progress bar");
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }
}