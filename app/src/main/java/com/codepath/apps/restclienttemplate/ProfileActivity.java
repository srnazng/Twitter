package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {
    User profile;

    ImageView ivProfile;
    TextView tvProfileName;
    TextView tvProfileScreenName;
    TextView tvFollowingValue;
    TextView tvFollowersValue;
    TextView tvFollowing;
    TextView tvFollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        profile = tweet.getUser();

        ivProfile = findViewById(R.id.ivProfile);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileScreenName = findViewById(R.id.tvProfileScreenName);
        tvFollowersValue = findViewById(R.id.tvFollowersValue);
        tvFollowingValue = findViewById(R.id.tvFollowingValue);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);

        Glide.with(this).load(profile.profileImageUrl).transform(new RoundedCorners(200)).into(ivProfile);

        tvProfileName.setText(profile.getName());
        tvProfileScreenName.setText("@" + profile.getScreenName());
        tvFollowersValue.setText(withSuffix(profile.getFollowersCount()));
        tvFollowingValue.setText(withSuffix(profile.getFriendsCount()));

        tvFollowingValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followingList();
            }
        });

        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followingList();
            }
        });

        tvFollowersValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followerList();
            }
        });

        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followerList();
            }
        });
    }

    public void followerList(){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("User", Parcels.wrap(profile));
        intent.putExtra("label", "Followers");
        startActivity(intent);
    }

    public void followingList(){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("User", Parcels.wrap(profile));
        intent.putExtra("label", "Following");
        startActivity(intent);
    }

    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }
}