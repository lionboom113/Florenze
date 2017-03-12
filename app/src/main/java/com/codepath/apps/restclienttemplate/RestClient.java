package com.codepath.apps.restclienttemplate;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class RestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "cj6SV88BgXim4fuQaq9ospWmQ";       // Change this
	public static final String REST_CONSUMER_SECRET = "R5EwaOfzPymYfg8dzdOj09Gpc1U3ArlW22reka9x8LXWHb7Yop"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://iwlac"; // Change this (here and in manifest)

	public RestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}
	public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}

	public void getCurrentUser(AsyncHttpResponseHandler handler) {
		String currentUserApiUrl = getApiUrl("account/verify_credentials.json");
		RequestParams params = new RequestParams();
		client.get(currentUserApiUrl, handler);
	}

	public void postTweet(String text, AsyncHttpResponseHandler handler) {
		String postTweetApiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", text);
		client.post(postTweetApiUrl, params, handler);
	}

	public void getMentionTimeline(AsyncHttpResponseHandler handler) {
		String currentUserApiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		client.get(currentUserApiUrl, params, handler);
	}

	public void getUserTimeline(String userId, AsyncHttpResponseHandler handler) {
		String currentUserApiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		params.put("count",200);
		client.get(currentUserApiUrl, params, handler);
	}
	public void getUserShow(String id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("user_id", id);
		getClient().get(apiUrl, params, handler);
	}


}
