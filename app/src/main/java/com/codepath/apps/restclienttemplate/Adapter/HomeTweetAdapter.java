package com.codepath.apps.restclienttemplate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.ProfileActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Tuan on 2017/03/04.
 */

public class HomeTweetAdapter  extends RecyclerView.Adapter<HomeTweetAdapter.ViewHolder>{
    private List<Tweet> mListTweet;
    private Context mContext;
    private Activity activity;

    public List<Tweet> getmListTweet() {
        return mListTweet;
    }

    public void setmListTweet(List<Tweet> mListTweet) {
        this.mListTweet = mListTweet;
        notifyDataSetChanged();
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void addTweet(List<Tweet> tweets){

        mListTweet.addAll(tweets);
        notifyDataSetChanged();
    }
    public void clearTweet(){
        mListTweet.clear();
        notifyDataSetChanged();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.tweet_home_adapter, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        holder.tvTweetContent.setText(mListTweet.get(position).getText());
        holder.tvTweetUsername.setText(mListTweet.get(position).getUser().getName());
        Glide.with(mContext)
                .load(mListTweet.get(position).getUser().getProfileImageUrl())
                .bitmapTransform( new RoundedCornersTransformation(mContext,10,10))
                .centerCrop()
                .placeholder(R.drawable.placeholder_male_superhero_c)
                .into(holder.ivAvatar);
        holder.tvTimestamp.setText(getRelativeTimeAgo(mListTweet.get(position).getCreatedAt()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity != null){
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("userId",mListTweet.get(position).getUser().getIdStr());
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListTweet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTweetContent)
        TextView tvTweetContent;
        @BindView(R.id.tvTweetUserName)
        TextView tvTweetUsername;
        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;
        @BindView(R.id.tvTimestamp)
        TextView tvTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
