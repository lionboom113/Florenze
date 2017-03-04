package com.codepath.apps.restclienttemplate;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.Adapter.HomeTweetAdapter;
import com.codepath.apps.restclienttemplate.Fragment.PostTweetFragment;
import com.codepath.apps.restclienttemplate.Utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    int page = 0;
    private EndlessRecyclerViewScrollListener scrollListener;
    final String TAG = "tuandebug";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tweet);
        ButterKnife.bind(this);
        homeTweetAdapter = new HomeTweetAdapter();
        tweets = new ArrayList<Tweet>();
        homeTweetAdapter.setmListTweet(tweets);
        homeTweetAdapter.setmContext(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvHomeTweet.setLayoutManager(layoutManager);
        rvHomeTweet.setAdapter(homeTweetAdapter);
        client = RestApplication.getRestClient();
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadPage(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvHomeTweet.addOnScrollListener(scrollListener);
        loadPage(++page);
    }
    private void loadPage(final int _page){
        client.getHomeTimeline(_page,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, "go on page:" + _page);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                showEditDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showEditDialog() {
        android.app.FragmentManager fm = getFragmentManager();
        PostTweetFragment editNameDialogFragment = PostTweetFragment.newInstance("Post Tweet");
        editNameDialogFragment.show(fm, "pt");
    }

}
