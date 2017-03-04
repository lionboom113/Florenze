package com.codepath.apps.restclienttemplate.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tuan on 2017/03/04.
 */

public class HomeTweetAdapter  extends RecyclerView.Adapter<HomeTweetAdapter.ViewHolder>{
    private List<Tweet> mListTweet;
    private Context mContext;

    public List<Tweet> getmListTweet() {
        return mListTweet;
    }

    public void setmListTweet(List<Tweet> mListTweet) {
        this.mListTweet = mListTweet;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTweetContent.setText(mListTweet.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return mListTweet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTweetContent)
        TextView tvTweetContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
