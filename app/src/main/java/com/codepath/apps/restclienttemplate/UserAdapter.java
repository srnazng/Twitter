package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<User> users;
    TwitterClient client;

    public UserAdapter(Context context, List<User> users){
        this.context = context;
        this.users = users;
        client = TwitterApp.getRestClient(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivUserProfile;
        TextView tvUserScreenName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserProfile = itemView.findViewById(R.id.ivUserProfile);
            tvUserScreenName = itemView.findViewById(R.id.tvUserScreenName);
        }

        public void bind(User user){
            tvUserScreenName.setText("@" + user.getScreenName());
            Glide.with(context).load(user.getProfileImageUrl()).transform(new RoundedCorners(100)).into(ivUserProfile);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<User> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }
}
