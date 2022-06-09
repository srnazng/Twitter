package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeReplyActivity extends AppCompatActivity {
    EditText etCompose;
    Button btnTweet;
    TwitterClient client;
    Tweet origTweet;

    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "ComposeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        Log.e(TAG, getIntent().toString());
        origTweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet")) ;

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        client = TwitterApp.getRestClient(this);

        etCompose.setText("@" + origTweet.getUser().screenName);

        // set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeReplyActivity.this, "Tweet empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeReplyActivity.this, "Tweet too long", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeReplyActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                client.publishTweetReply(tweetContent, origTweet.id, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet reply " + statusCode);
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet reply says: " + tweet);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish(); // closes activity, pass data to parent
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                });
            }
        });
        // make api call to twitter to publish tweet
    }
}