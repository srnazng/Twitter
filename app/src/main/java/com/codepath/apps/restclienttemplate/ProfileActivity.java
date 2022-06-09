package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

        Glide.with(this).load(profile.profileImageUrl).transform(new RoundedCorners(200)).into(ivProfile);

        tvProfileName.setText(profile.getName());
        tvProfileScreenName.setText("@" + profile.getScreenName());
        tvFollowersValue.setText(withSuffix(profile.getFollowersCount()));
        tvFollowingValue.setText(withSuffix(profile.getFriendsCount()));
    }

    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp-1));
    }
}