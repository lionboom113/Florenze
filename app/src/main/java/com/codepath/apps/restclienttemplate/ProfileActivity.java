package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.Adapter.HomeTweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {
    private enum ProfileType{
        Self,
        Other
    }

    @BindView(R.id.tvProfileUsername)
    TextView tvUsername;

    @BindView(R.id.tvProfileDescription)
    TextView tvDescription;

    @BindView(R.id.tvProfileInfo)
    TextView tvFollowInfo;

    @BindView(R.id.rvProfile)
    RecyclerView rvHomeTweet;

    @BindView(R.id.ivProfileAvatar)
    ImageView ivProfileAvatar;


    private HomeTweetAdapter homeTweetAdapter;
    private List<Tweet> tweets;
    private RestClient client;
    private String userId;
    private ProfileType profileType;
    private final Activity thisActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("userId");
        if (userId.equals("self")){
            profileType = ProfileType.Self;
        } else {
            profileType = ProfileType.Other;
        }
        homeTweetAdapter = new HomeTweetAdapter();
        tweets = new ArrayList<Tweet>();
        homeTweetAdapter.setmListTweet(tweets);
        homeTweetAdapter.setmContext(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvHomeTweet.setLayoutManager(layoutManager);
        rvHomeTweet.setAdapter(homeTweetAdapter);
        client = RestApplication.getRestClient();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvHomeTweet.getContext(),
                layoutManager.getOrientation());
        rvHomeTweet.addItemDecoration(dividerItemDecoration);
        if (profileType == ProfileType.Self){
            client.getCurrentUser(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();
                    Type userType = new TypeToken<User>(){}.getType();
                    User user = gson.fromJson(response.toString(), userType);
                    tvUsername.setText(user.getName());
                    tvDescription.setText(user.getDescription());
                    tvFollowInfo.setText(String.format("Follower: %1$d | Following: %2$d",
                            user.getFollowersCount(), user.getFriendsCount()));
                    Glide.with(getApplicationContext())
                            .load(user.getProfileImageUrl())
                            .bitmapTransform( new RoundedCornersTransformation(getApplicationContext(),10,10))
                            .placeholder(R.drawable.placeholder_male_superhero_c)
                            .into(ivProfileAvatar);
                    thisActivity.setTitle(user.getName());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } else {
            client.getUserShow(userId, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();
                    Type userType = new TypeToken<User>(){}.getType();
                    User user = gson.fromJson(response.toString(), userType);
                    tvUsername.setText(user.getName());
                    tvDescription.setText(user.getDescription());
                    tvFollowInfo.setText(String.format("Follower: %1$d | Following: %2$d",
                            user.getFollowersCount(), user.getFriendsCount()));
                    Glide.with(getApplicationContext())
                            .load(user.getProfileImageUrl())
                            .bitmapTransform( new RoundedCornersTransformation(getApplicationContext(),10,10))
                            .placeholder(R.drawable.placeholder_male_superhero_c)
                            .into(ivProfileAvatar);
                    thisActivity.setTitle(user.getName());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

        client.getUserTimeline(userId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                Type listType = new TypeToken<List<Tweet>>(){}.getType();
                List<Tweet> myModelList = gson.fromJson(response.toString(), listType);
                //homeTweetAdapter.getmListTweet().addAll(tweets);
                homeTweetAdapter.addTweet(myModelList);
                homeTweetAdapter.notifyDataSetChanged();
            }
        });
    }
}
