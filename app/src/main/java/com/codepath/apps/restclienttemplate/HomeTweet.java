package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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
    public enum TabType{
        Timeline,
        Mention
    }

    @BindView(R.id.rvHomeTweet)
    RecyclerView rvHomeTweet;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private HomeTweetAdapter homeTweetAdapter;
    private List<Tweet> tweets;
    private int page = 0;
    private TabType tabType;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RestClient client;
    private final String TAG = "tuandebug";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabType = TabType.Timeline;
        setContentView(R.layout.activity_home_tweet);
        ButterKnife.bind(this);
        setTitle("Florenze");
        homeTweetAdapter = new HomeTweetAdapter();
        homeTweetAdapter.setActivity(this);
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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals(getResources().getString(R.string.timeline))){
                    homeTweetAdapter.clearTweet();
                    tabType = TabType.Timeline;
                    page = 0;
                    loadPage(page++);
                } else {
                    homeTweetAdapter.clearTweet();
                    tabType = TabType.Mention;
                    page = 0;
                    loadPage(page++);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        if (tabType.equals(TabType.Timeline)){
            client.getHomeTimeline(_page,new JsonHttpResponseHandler(){
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
        } else {
            client.getMentionTimeline(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();
                    Type listType = new TypeToken<List<Tweet>>(){}.getType();
                    List<Tweet> myModelList = gson.fromJson(response.toString(), listType);
                    //homeTweetAdapter.getmListTweet().addAll(tweets);
                    homeTweetAdapter.clearTweet();
                    homeTweetAdapter.addTweet(myModelList);
                    homeTweetAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }

    }
    public void postTweet(String text){
        client.postTweet(text, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                page = 0;
                homeTweetAdapter.clearTweet();
                loadPage(++page);
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
            case R.id.miProfile:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("userId","self");
                startActivity(intent);
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
