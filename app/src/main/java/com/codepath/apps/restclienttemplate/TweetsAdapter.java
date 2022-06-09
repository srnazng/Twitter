package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    private final int REQUEST_CODE = 20;
    private final String TAG = "TweetsAdapter";

    Context context;
    List<Tweet> tweets;
    TwitterClient client;

    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
        client = TwitterApp.getRestClient(context);
    }

    // for each row inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // bind values based on position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data at position
        Tweet tweet = tweets.get(position);
        // bind tweet with viewholder
        holder.bind(tweet);
    }

    // pass in context and list of tweets
    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivMedia;
        TextView tvTimestamp;

        // bottom buttons
        ImageButton btnReply;
        ImageButton btnFavorite;
        ImageButton btnRetweet;

        // header
        ImageView ivIcon;
        TextView tvLabel;

        // reply to
        TextView tvReply;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnReply = itemView.findViewById(R.id.btnReply);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvReply = itemView.findViewById(R.id.tvReply);
        }

        public void bind(Tweet tweet){
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCorners(100)).into(ivProfileImage);

            if(!tweet.media_url.isEmpty()){
                ivMedia.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.media_url).into(ivMedia);
            }
            else{
                ivMedia.setVisibility(View.GONE);
            }

            if(!tweet.in_reply_to_screen_name.isEmpty()){
                tvReply.setText("Reply to @" + tweet.in_reply_to_screen_name);
                tvReply.setVisibility(View.VISIBLE);
            }
            else{
                tvReply.setVisibility(View.GONE);
            }

            if(tweet.retweeted){
                ivIcon.setImageResource(R.drawable.ic_vector_retweet);
                ivIcon.setVisibility(View.VISIBLE);
                tvLabel.setText("You retweeted");
                tvLabel.setVisibility(View.VISIBLE);
            }
            else{
                ivIcon.setVisibility(View.GONE);
                tvLabel.setVisibility(View.GONE);
            }

            tvTimestamp.setText(getRelativeTimeAgo(tweet.created_at));

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ComposeReplyActivity.class);
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(intent);
                }
            });

            if(tweet.favorited){
                btnFavorite.setImageResource(R.drawable.ic_vector_heart);
            }
            else{
                btnFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
            }

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tweet.isFavorited()){
                        client.unfavoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "onSuccess to unfavorite tweet" + statusCode);
                                tweet.setFavorited(false);
                                btnFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e(TAG, "onFailure to unfavorite tweet", throwable);
                            }
                        });
                    }
                    else{
                        client.favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "onSuccess to favorite tweet" + statusCode);
                                tweet.setFavorited(true);
                                btnFavorite.setImageResource(R.drawable.ic_vector_heart);
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e(TAG, "onFailure to favorite tweet", throwable);
                            }
                        });
                    }
                }
            });

            if(tweet.retweeted){
                btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
            }
            else{
                btnRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
            }

            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tweet.isRetweeted()){
                        client.unretweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "onSuccess unretweet" + statusCode);
                                tweet.setRetweeted(false);
                                btnRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                                ivIcon.setVisibility(View.GONE);
                                tvLabel.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e(TAG, "onFailure unretweet", throwable);
                            }
                        });
                    }
                    else{
                        client.retweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "onSuccess retweet" + statusCode);
                                tweet.setRetweeted(true);
                                btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
                                ivIcon.setImageResource(R.drawable.ic_vector_retweet);
                                ivIcon.setVisibility(View.VISIBLE);
                                tvLabel.setText("You retweeted");
                                tvLabel.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e(TAG, "onFailure retweet", throwable);
                            }
                        });
                    }
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
