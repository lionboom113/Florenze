package com.codepath.apps.restclienttemplate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.Adapter.HomeTweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HomeTweet extends AppCompatActivity {
    RestClient client;
    @BindView(R.id.rvHomeTweet)
    RecyclerView rvHomeTweet;
    HomeTweetAdapter homeTweetAdapter;
    List<Tweet> tweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tweet);
        ButterKnife.bind(this);
        final HomeTweetAdapter homeTweetAdapter = new HomeTweetAdapter();
        tweets = new ArrayList<Tweet>();
        homeTweetAdapter.setmListTweet(tweets);
        homeTweetAdapter.setmContext(getApplicationContext());
        rvHomeTweet.setAdapter(homeTweetAdapter);
        client = RestApplication.getRestClient();
        client.getHomeTimeline(1,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Tweet>>(){}.getType();
                List<Tweet> myModelList = gson.fromJson(response.toString(), listType);
                tweets.clear();
                tweets.addAll(myModelList);
                homeTweetAdapter.getmListTweet().addAll(tweets);
                Log.d("chubby", "onSuccess: ");
                homeTweetAdapter.notifyDataSetChanged();
                Log.d("response", response.toString());
            }
        });
    }
}
